package com.supernova.bkashmanager.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.supernova.bkashmanager.R
import com.supernova.bkashmanager.databinding.ActivityLoginBinding
import com.supernova.bkashmanager.dialog.CustomDialog
import com.supernova.bkashmanager.dialog.CustomProgress
import com.supernova.bkashmanager.util.Constants
import com.supernova.bkashmanager.util.SharedPrefs
import com.supernova.bkashmanager.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var prefs: SharedPrefs
    private lateinit var dialog: CustomDialog
    private lateinit var progress: CustomProgress

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        this.binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialize()
    }

    private fun initialize() {

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        prefs = SharedPrefs(this)
        dialog = CustomDialog()
        progress = CustomProgress()

        binding.buttonSignIn.setOnClickListener(this)
    }

    private fun showCustomDialog(title: String, message: String) {

        if (!dialog.isVisible) {

            dialog.dialogTitle = title
            dialog.dialogContent = message
            dialog.isCancelable = false

            dialog.show(supportFragmentManager, "dialog")
        }
    }

    private fun showCustomProgress(title: String, message: String): CustomProgress {

        if (!progress.isVisible) {

            progress.dialogTitle = title
            progress.dialogContent = message
            progress.isCancelable = false

            progress.show(supportFragmentManager, "progress")
        }

        return progress
    }

    private fun switchToDashboard() {

        startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left)
        finishAffinity()
    }

    override fun onClick(v: View?) {

        if (v?.id == R.id.button_sign_in) {

            if (binding.loginEmail.text.toString().isEmpty()) {

                binding.loginEmail.error = getString(R.string.required)

            } else if (binding.loginPassword.text.toString().isEmpty()) {

                binding.loginPassword.error = getString(R.string.required)

            } else {

                val email = binding.loginEmail.text.toString()
                val password = binding.loginPassword.text.toString()

                if (viewModel.validateEmail(email)) {

                    val p = showCustomProgress(getString(R.string.please_wait), getString(R.string.authenticating))

                    viewModel.authenticateUser(email, password).observe(this, {

                        if (it != null) {

                            p.dismiss()

                            if (it.status == Constants.STATUS_SUCCESS) {

                                prefs.adminName = it.user?.userName ?: ""
                                prefs.adminToken = it.user?.userToken ?: ""
                                prefs.currentPoints = it.user?.currentPoints ?: 0
                                prefs.initialPoints = it.user?.initialPoints ?: 0
                                prefs.adminEmail = email
                                prefs.rememberUser = binding.rememberMe.isChecked
                                prefs.hasLoggedIn = true

                                switchToDashboard()

                            } else {

                                showCustomDialog(getString(R.string.error), it.failReason ?: getString(R.string.something_went_wrong))
                            }

                        } else {

                            p.dismiss()
                            showCustomDialog(getString(R.string.error), getString(R.string.something_went_wrong))
                        }
                    })

                } else {

                    showCustomDialog(getString(R.string.error), getString(R.string.invalid_email))
                }
            }
        }
    }
}
package com.supernova.bkashmanager.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.google.firebase.messaging.FirebaseMessaging
import com.supernova.bkashmanager.R
import com.supernova.bkashmanager.adapter.HistoryAdapter
import com.supernova.bkashmanager.adapter.SettingsAdapter
import com.supernova.bkashmanager.databinding.ActivityDashboardBinding
import com.supernova.bkashmanager.dialog.*
import com.supernova.bkashmanager.listener.DialogInteractionListener
import com.supernova.bkashmanager.listener.FragmentInteractionListener
import com.supernova.bkashmanager.listener.UserClickListener
import com.supernova.bkashmanager.model.History
import com.supernova.bkashmanager.model.SettingsItem
import com.supernova.bkashmanager.model.User
import com.supernova.bkashmanager.model.UserHistory
import com.supernova.bkashmanager.service.DataService
import com.supernova.bkashmanager.service.HistoryService
import com.supernova.bkashmanager.service.UserService
import com.supernova.bkashmanager.util.Constants
import com.supernova.bkashmanager.util.SharedPrefs
import com.supernova.bkashmanager.view.fragment.HistoryFragment
import com.supernova.bkashmanager.view.fragment.HomeFragment
import com.supernova.bkashmanager.view.fragment.SettingsFragment
import com.supernova.bkashmanager.viewmodel.DashboardViewModel
import java.util.*


class DashboardActivity : AppCompatActivity(), AHBottomNavigation.OnTabSelectedListener, FragmentInteractionListener,
    SettingsAdapter.OnClickListener, UserClickListener, CustomListDialog.ListItemClickListener, HistoryAdapter.OnClickListener,
    CustomInputDialog.InputListener, CustomUserDialog.OnCreateListener {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel
    private lateinit var prefs: SharedPrefs
    private lateinit var dialog: CustomDialog
    private lateinit var progress: CustomProgress
    private lateinit var inputDialog: CustomInputDialog

    private lateinit var listDialog: CustomListDialog

    private var doubleBackPressed: Boolean = false

    private var banText: String = ""
    private var userId: Int = 0
    private var userPoints: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        this.binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
    }

    override fun onStart() {

        super.onStart()
        FirebaseMessaging.getInstance().subscribeToTopic("bkash_manager")
    }

    override fun onResume() {

        super.onResume()

        startProfileService()
        startUserService()
        startHistoryService()
    }

    override fun onBackPressed() {

        if (doubleBackPressed) {

            super.onBackPressed()

        } else {

            doubleBackPressed = true

            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({ doubleBackPressed = false }, 1500)
        }
    }

    private fun initialize() {

        setSupportActionBar(binding.toolbar)

        val adapter = AHBottomNavigationAdapter(this, R.menu.bottom_navigation)

        prefs = SharedPrefs(this)
        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        listDialog = CustomListDialog()
        inputDialog = CustomInputDialog()
        dialog = CustomDialog()
        progress = CustomProgress()

        adapter.setupWithBottomNavigation(binding.bottomNavigation)

        binding.bottomNavigation.setOnTabSelectedListener(this)
        binding.bottomNavigation.currentItem = 0
    }

    private fun loadFragment(position: Int) {

        val transaction = supportFragmentManager.beginTransaction()
        val fragment: Fragment = when (position) {

            0 -> HomeFragment.getInstance()
            1 -> HistoryFragment.getInstance(this)
            2 -> SettingsFragment.getInstance()
            else -> HomeFragment.getInstance()
        }

        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    private fun startProfileService() {

        val service = Intent(this, DataService::class.java)

        Looper.myLooper()?.let {

            Handler(it).postDelayed( {

                DataService.enqueueWork(this, Constants.JOB_FETCH_PROFILE, service)

            }, 500)
        }
    }

    private fun startUserService() {

        val service = Intent(this, UserService::class.java)
        UserService.enqueueWork(this, Constants.JOB_FETCH_USERS, service)
    }

    private fun startHistoryService() {

        val service = Intent(this, HistoryService::class.java)
        HistoryService.enqueueWork(this, Constants.JOB_FETCH_HISTORIES, service)
    }

    private fun showCustomDialog(title: String, message: String, isConfirmVisible: Boolean = false, listener: DialogInteractionListener? = null) {

        if (!dialog.isVisible) {

            dialog.dialogTitle = title
            dialog.dialogContent = message
            dialog.isConfirmVisible = isConfirmVisible
            dialog.listener = listener
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

    private fun showInputDialog(title: String?, text: String?, hint: String? = "", id: Int = 0, type: Int = 0, listener: CustomInputDialog.InputListener? = null) {

        if (inputDialog.isAdded || inputDialog.isVisible) {

            inputDialog.dismiss()
        }

        inputDialog.dialogTitle = title
        inputDialog.dialogContent = text
        inputDialog.textHint = hint
        inputDialog.dialogId = id
        inputDialog.dialogType = type
        inputDialog.listener = listener
        inputDialog.isCancelable = false

        inputDialog.show(supportFragmentManager, "input")
    }

    override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {

        loadFragment(position)
        return true
    }

    override fun getManagerName(): String {

        return prefs.adminName
    }

    override fun getInitialPoints(): Int {

        return prefs.initialPoints
    }

    override fun getCurrentPoints(): Int {

        return prefs.currentPoints
    }

    override fun getStorageListener(): LiveData<Pair<String, String>> {

        return viewModel.createStorageListener()
    }

    override fun getUsers(): LiveData<List<User>> {

        return viewModel.getUsers()
    }

    override fun getHistories(date: String): LiveData<List<UserHistory>> {

        return viewModel.getHistories(date)
    }

    override fun getItems(): LiveData<List<SettingsItem>> {

        return viewModel.parseSettingsItems(this)
    }

    override fun onUserClicked(user: User?) {

        if (listDialog.isAdded || listDialog.isVisible) {

            listDialog.dismiss()
        }

        banText = if (user != null && user.banStatus == 1) getString(R.string.unban) else getString(R.string.ban)
        val items = mutableListOf<String>()

        items.addAll(resources.getStringArray(R.array.user_operation))
        items[0] = items[0].replace("%1", banText)

        listDialog.user = user
        listDialog.items = items
        listDialog.listType = Constants.LIST_TYPE_USER
        listDialog.listener = this
        listDialog.show(supportFragmentManager, "list")
    }

    override fun onItemClick(position: Int, user: User?) {

        if (listDialog.isAdded || listDialog.isVisible) {

            listDialog.dismiss()
        }

        if (position == Constants.TYPE_USER_STATUS) {

            showCustomDialog(getString(R.string.warning), getString(R.string.ban_warning, banText.toLowerCase(Locale.ROOT)), true, object: DialogInteractionListener {

                override fun onConfirm(type: Int) {

                    val progress = showCustomProgress(getString(R.string.please_wait), getString(R.string.communicating))

                    viewModel.updateUser(prefs.adminEmail, prefs.adminToken, Constants.USER_BLOCK, user?.userId ?: 0).observe(this@DashboardActivity, {

                        if (it != null) {

                            progress.dismiss()

                            if (it.status == Constants.STATUS_SUCCESS) {

                                Toast.makeText(this@DashboardActivity, getString(R.string.user_status_updated), Toast.LENGTH_SHORT).show()

                            } else {

                                showCustomDialog(getString(R.string.error), it.failReason ?: "")
                            }
                        }
                    })
                }
            })

        } else if (position == Constants.TYPE_USER_POINTS) {

            if (user != null) {

                val points = user.currentPoints
                userId = user.userId
                userPoints = points

                showInputDialog(getString(R.string.update_points), points.toString(), getString(R.string.total_points), Constants.TYPE_USER_POINTS, Constants.DIALOG_TYPE_NUMBER, this)
            }
        }
    }

    override fun onItemClick(position: Int, history: History?) {

        val listener = object: CustomInputDialog.InputListener {

            override fun onInput(id: Int, text: String?, text2: String?) {

                if (history != null && history.id > 0) {

                    val progress = showCustomProgress(getString(R.string.please_wait), getString(R.string.communicating))

                    viewModel.updateHistory(prefs.adminEmail, prefs.adminToken, history.id, text).observe(this@DashboardActivity, {

                        if (it != null) {

                            progress.dismiss()

                            if (it.status == Constants.STATUS_SUCCESS) {

                                Toast.makeText(this@DashboardActivity, getString(R.string.transaction_status_updated), Toast.LENGTH_SHORT).show()

                            } else {

                                showCustomDialog(getString(R.string.error), it.failReason ?: "")
                            }

                        } else {

                            progress.dismiss()
                            showCustomDialog(getString(R.string.error), getString(R.string.something_went_wrong))
                        }
                    })

                } else {

                    showCustomDialog(getString(R.string.error), getString(R.string.something_went_wrong))
                }
            }
        }

        showInputDialog("", "", "", 0, Constants.DIALOG_TYPE_TEXT, listener)
    }

    override fun onClick(item: UserHistory?) {

        if (item != null) {

            val history = item.history ?: return

            if (history.completionFlag == 1) {

                Toast.makeText(this, getString(R.string.history_processed), Toast.LENGTH_SHORT).show()

            } else {

                val items = mutableListOf<String>()
                items.addAll(resources.getStringArray(R.array.history_operation))

                listDialog.history = history
                listDialog.items = items
                listDialog.listType = Constants.LIST_TYPE_HISTORY
                listDialog.listener = this
                listDialog.show(supportFragmentManager, "list")
            }
        }
    }

    override fun onClick(id: Int) {

        when (id) {

            Constants.SETTINGS_ITEM_NAME -> showInputDialog(getString(R.string.update_name), prefs.adminName, getString(R.string.user_name), id, Constants.DIALOG_TYPE_TEXT, this)
            Constants.SETTINGS_ITEM_PASSWORD -> showInputDialog(getString(R.string.update_password), "", getString(R.string.current_password), id, Constants.DIALOG_TYPE_PASSWORD, this)
            Constants.SETTINGS_ITEM_POINTS -> showInputDialog(getString(R.string.update_points), prefs.initialPoints.toString(), getString(R.string.total_points), id, Constants.DIALOG_TYPE_NUMBER, this)
            Constants.SETTINGS_ITEM_NEW_USER -> {

                val userDialog = CustomUserDialog()

                userDialog.listener = this
                userDialog.isCancelable = false

                userDialog.show(supportFragmentManager, "user")
            }
        }
    }

    override fun onInput(id: Int, text: String?, text2: String?) {

        val progress = showCustomProgress(getString(R.string.please_wait), getString(R.string.communicating))
        val type = if (id >= Constants.SETTINGS_ITEM_BASE) id - Constants.SETTINGS_ITEM_BASE else id

        if (id >= Constants.SETTINGS_ITEM_BASE) {

            viewModel.updateProfile(prefs.adminEmail, prefs.adminToken, type, text, text2).observe(this, {

                if (it != null) {

                    progress.dismiss()

                    if (it.status == Constants.STATUS_SUCCESS) {

                        val user = it.user

                        if (user != null) {

                            prefs.adminName = it.user?.userName ?: ""
                            prefs.currentPoints = it.user?.currentPoints ?: 0
                            prefs.initialPoints = it.user?.initialPoints ?: 0
                        }

                        Toast.makeText(this@DashboardActivity, getString(R.string.profile_updated), Toast.LENGTH_SHORT).show()

                    } else {

                        showCustomDialog(getString(R.string.error), it.failReason ?: "")
                    }

                } else {

                    progress.dismiss()
                    showCustomDialog(getString(R.string.error), getString(R.string.something_went_wrong))
                }
            })

        } else {

            if (userId == Constants.INVALID_USER_ID) {

                progress.dismiss()
                showCustomDialog(getString(R.string.error), getString(R.string.something_went_wrong))

            } else {

                var number = -2

                try {

                    if (text != null) {

                        number = text.toInt()
                    }

                } catch (ex: Exception) {

                    ex.printStackTrace()
                }

                if (number != -2) {

                    val diff = number - userPoints
                    val curr = prefs.currentPoints

                    if (curr - diff < 0 || curr - diff > prefs.initialPoints) {

                        progress.dismiss()
                        showCustomDialog(getString(R.string.error), getString(R.string.point_error))

                    } else {

                        viewModel.updateUser(prefs.adminEmail, prefs.adminToken, type, userId, text).observe(this, {

                            if (it != null) {

                                progress.dismiss()

                                if (it.status == Constants.STATUS_SUCCESS) {

                                    val user = it.user

                                    if (user != null) {

                                        val rem = user.currentPoints - userPoints
                                        prefs.currentPoints -= rem
                                    }

                                    userPoints = 0
                                    userId = Constants.INVALID_USER_ID

                                    Toast.makeText(this@DashboardActivity, getString(R.string.user_points_updated), Toast.LENGTH_SHORT).show()

                                } else {

                                    userId = Constants.INVALID_USER_ID
                                    showCustomDialog(getString(R.string.error), it.failReason ?: "")
                                }

                            } else {

                                progress.dismiss()
                                showCustomDialog(getString(R.string.error), getString(R.string.something_went_wrong))

                                userId = Constants.INVALID_USER_ID
                            }
                        })
                    }

                } else {

                    progress.dismiss()
                    showCustomDialog(getString(R.string.error), getString(R.string.point_error))
                }
            }
        }
    }

    override fun onNewUserCreated(user: User) {

        val progress = showCustomProgress(getString(R.string.please_wait), getString(R.string.communicating))

        viewModel.addNewUser(prefs.adminEmail, prefs.adminToken, user).observe(this, {

            if (it != null) {

                progress.dismiss()

                if (it.status == Constants.STATUS_SUCCESS) {

                    Toast.makeText(this@DashboardActivity, getString(R.string.user_created), Toast.LENGTH_SHORT).show()

                } else {

                    showCustomDialog(getString(R.string.error), it.failReason ?: "")
                }

            } else {

                progress.dismiss()
                showCustomDialog(getString(R.string.error), getString(R.string.something_went_wrong))
            }
        })
    }
}
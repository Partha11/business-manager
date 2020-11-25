package com.supernova.bkashmanager.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.supernova.bkashmanager.databinding.ActivitySplashBinding
import com.supernova.bkashmanager.util.SharedPrefs

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        this.binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
    }

    private fun initialize() {

        val prefs = SharedPrefs(this)

        Handler(Looper.getMainLooper()).postDelayed({

            val intent: Intent = if (prefs.hasLoggedIn) {

                if (prefs.rememberUser) {

                    Intent(this@SplashActivity, DashboardActivity::class.java)

                } else {

                    Intent(this@SplashActivity, LoginActivity::class.java)
                }

            } else {

                Intent(this@SplashActivity, LoginActivity::class.java)
            }

            startActivity(intent)
            finishAffinity()
            overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left)

        }, 1500)
    }
}
package com.supernova.bkashmanager.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.supernova.bkashmanager.R
import com.supernova.bkashmanager.databinding.ActivityDashboardBinding
import com.supernova.bkashmanager.listener.FragmentInteractionListener
import com.supernova.bkashmanager.listener.UpdateListener
import com.supernova.bkashmanager.model.History
import com.supernova.bkashmanager.model.User
import com.supernova.bkashmanager.service.DataService
import com.supernova.bkashmanager.service.HistoryService
import com.supernova.bkashmanager.service.UserService
import com.supernova.bkashmanager.util.Constants
import com.supernova.bkashmanager.util.SharedPrefs
import com.supernova.bkashmanager.view.fragment.HistoryFragment
import com.supernova.bkashmanager.view.fragment.HomeFragment
import com.supernova.bkashmanager.viewmodel.DashboardViewModel


class DashboardActivity : AppCompatActivity(), AHBottomNavigation.OnTabSelectedListener, FragmentInteractionListener {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel
    private lateinit var prefs: SharedPrefs

    companion object {

        var updateListener: UpdateListener? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        this.binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
    }

    override fun onResume() {

        super.onResume()

        startProfileService()
        startUserService()
        startHistoryService()
    }

    private fun initialize() {

        setSupportActionBar(binding.toolbar)

        val adapter = AHBottomNavigationAdapter(this, R.menu.bottom_navigation)
        prefs = SharedPrefs(this)
        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        adapter.setupWithBottomNavigation(binding.bottomNavigation)

        binding.bottomNavigation.setOnTabSelectedListener(this)
        binding.bottomNavigation.currentItem = 0

        prefs.prefs.registerOnSharedPreferenceChangeListener { _, key ->

            when (key) {

                Constants.PREF_ADMIN_NAME -> updateListener?.onNameUpdated(prefs.adminName)
            }
        }
    }

    private fun loadFragment(position: Int) {

        val transaction = supportFragmentManager.beginTransaction()
        val fragment: Fragment = when (position) {

            0 -> HomeFragment.getInstance()
            1 -> HistoryFragment.getInstance()
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

            }, 2000)
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

    override fun getHistories(date: String): LiveData<List<History>> {

        return viewModel.getHistories(date)
    }
}
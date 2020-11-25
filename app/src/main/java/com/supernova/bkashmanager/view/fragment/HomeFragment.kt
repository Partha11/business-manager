package com.supernova.bkashmanager.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supernova.bkashmanager.R
import com.supernova.bkashmanager.adapter.UserAdapter
import com.supernova.bkashmanager.databinding.FragmentHomeBinding
import com.supernova.bkashmanager.listener.FragmentInteractionListener
import com.supernova.bkashmanager.listener.UpdateListener
import com.supernova.bkashmanager.util.Utils
import com.supernova.bkashmanager.view.activity.DashboardActivity

class HomeFragment : Fragment(), UpdateListener {

    private lateinit var binding: FragmentHomeBinding
    private var listener: FragmentInteractionListener? = null

    companion object {

        @JvmStatic
        fun getInstance() = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        this.binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    override fun onAttach(context: Context) {

        super.onAttach(context)

        if (context is FragmentInteractionListener) {

            listener = context
        }

        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    private fun initialize() {

        binding.wishText.text = Utils.getGreetings()
        binding.managerName.text = listener?.getManagerName()
        binding.initialBalance.text = context?.getString(R.string.initial_points, listener?.getInitialPoints())
        binding.currentBalance.text = context?.getString(R.string.current_points, listener?.getCurrentPoints())
        binding.usersTitle.text = context?.getString(R.string.users, 1)

        DashboardActivity.updateListener = this

        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        val adapter = UserAdapter(context)

        binding.userRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.userRecycler.itemAnimator = DefaultItemAnimator()
        binding.userRecycler.adapter = adapter

        listener?.getUsers()?.observe(viewLifecycleOwner, {

            adapter.users = it
            binding.usersTitle.text = context?.getString(R.string.users, it.size)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onNameUpdated(name: String) {

        binding.managerName.text = name
    }
}
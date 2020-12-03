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
import com.supernova.bkashmanager.adapter.SettingsAdapter
import com.supernova.bkashmanager.databinding.FragmentSettingsBinding
import com.supernova.bkashmanager.listener.FragmentInteractionListener

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private var listener: FragmentInteractionListener? = null
    private var adapter: SettingsAdapter? = null

    companion object {

        @JvmStatic
        fun getInstance() = SettingsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        this.binding = FragmentSettingsBinding.inflate(inflater)
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

        (activity as AppCompatActivity).supportActionBar?.show()

        activity?.title = "Settings"
        adapter = SettingsAdapter(context)
    }

    private fun initialize() {

        setupRecyclerView()
        getItems()
    }

    private fun setupRecyclerView() {

        binding.settingsRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.settingsRecycler.itemAnimator = DefaultItemAnimator()
        binding.settingsRecycler.adapter = adapter
    }

    private fun getItems() {

        listener?.getItems()?.observe(viewLifecycleOwner, {

            adapter?.items = it
            adapter?.notifyDataSetChanged()
        })
    }
}
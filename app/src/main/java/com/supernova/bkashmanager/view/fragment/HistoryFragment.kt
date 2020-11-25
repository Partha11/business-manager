package com.supernova.bkashmanager.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.theme.DarkThemeFactory
import com.supernova.bkashmanager.R
import com.supernova.bkashmanager.adapter.HistoryAdapter
import com.supernova.bkashmanager.databinding.FragmentHistoryBinding
import com.supernova.bkashmanager.dialog.CustomPicker
import com.supernova.bkashmanager.listener.DateChangeListener
import com.supernova.bkashmanager.listener.FragmentInteractionListener
import java.util.*


class HistoryFragment : Fragment(), DateChangeListener {

    private lateinit var binding: FragmentHistoryBinding

    private var listener: FragmentInteractionListener? = null
    private var adapter: HistoryAdapter? = null
    private var date: String = ""

    companion object {

        @JvmStatic
        fun getInstance() = HistoryFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentHistoryBinding.inflate(inflater)
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
        activity?.title = "Histories"
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_calendar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_calendar) {

            showDatePickerDialog()
            return true
        }

        return false
    }

    private fun initialize() {

        setupRecyclerView()
        onDateChanged("")
    }

    private fun setupRecyclerView() {

        adapter = HistoryAdapter(context)

        binding.historyRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.historyRecycler.itemAnimator = DefaultItemAnimator()
        binding.historyRecycler.adapter = adapter
    }

    private fun showDatePickerDialog() {

//        val multipleDaysPickCallback = MultipleDaysPickCallback { multipleDays ->
//            // TODO
//        }

        val themeFactory = DarkThemeFactory()
        val today = CivilCalendar()

        val datePicker = PrimeDatePicker.bottomSheetWith(today)
            .applyTheme(themeFactory)                    // Optional
            .build()

        datePicker.show(supportFragmentManager, "SOME_TAG")

//        val dialog = CustomPicker()
//
//        if (activity != null) {
//
//            dialog.isCancelable = false
//            dialog.selectedDate = date
//            dialog.listener = this
//
//            dialog.show(activity!!.supportFragmentManager, "picker")
//        }
    }

    override fun onDateChanged(date: String) {

        this.date = date

        listener?.getHistories(date)?.observe(viewLifecycleOwner, {

            adapter?.histories = it
            adapter?.notifyDataSetChanged()
        })
    }
}
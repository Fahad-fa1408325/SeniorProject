package com.cmps312.seniorproject

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.cmps312.seniorproject.model.entity.Pill
import com.cmps312.seniorproject.ui.viewmodel.PillViewModel
import kotlinx.android.synthetic.main.fragment_dispense_on_demand.*

class DispenseOnDemandFragment : Fragment(R.layout.fragment_dispense_on_demand) {

    private val pillViewModel: PillViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var tempPillsList = mutableListOf<Pill>()
        val options = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        chamber1SP.adapter =
            context?.let { ArrayAdapter<Int>(it, android.R.layout.simple_list_item_1, options) }
        chamber2SP.adapter =
            context?.let { ArrayAdapter<Int>(it, android.R.layout.simple_list_item_1, options) }
        chamber3SP.adapter =
            context?.let { ArrayAdapter<Int>(it, android.R.layout.simple_list_item_1, options) }

        var firstChoice: Int = 0
        var secondChoice: Int = 0
        var thirdChoice: Int = 0

        var firstPillFlag = false
        var secondPillFlag = false
        var thirdPillFlag = false

        pillViewModel.pills.value?.let {
            it.forEach { pill ->
                if (pill.mainUserEmail == "Main Device" && !pill.name.isNullOrEmpty() && !pill.time.isNullOrEmpty()) {
                    tempPillsList.add(pill)
                }
            }
        }

        if (tempPillsList.isNullOrEmpty()) {
            Toast.makeText(context, "No pills registered on device yet", Toast.LENGTH_SHORT)
                .show()
            activity?.onBackPressed()
        }

        chamber1SP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                firstChoice = options[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        chamber2SP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                secondChoice = options[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        chamber3SP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                thirdChoice = options[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        tempPillsList.forEach {
            if (it.uid == "chamber_1") {
                dispenseChamber1ET.hint = "${it.name}"
                dispenseChamber1ET.visibility = VISIBLE
                chamber1SP.visibility = VISIBLE
            }
            if (it.uid == "chamber_2") {
                dispenseChamber2ET.hint = "${it.name}"
                dispenseChamber2ET.visibility = VISIBLE
                chamber2SP.visibility = VISIBLE
            }
            if (it.uid == "chamber_3") {
                dispenseChamber3ET.hint = "${it.name}"
                dispenseChamber3ET.visibility = VISIBLE
                chamber3SP.visibility = VISIBLE
            }

        }

        dispenseBTN.setOnClickListener {

            if (dispenseChamber1ET.visibility == VISIBLE) {
                tempPillsList.forEach {
                    if (it.uid == "chamber_1") {
                        it.demanded = firstChoice
                        it.readFromMain = false
                        it.editFromMain = false
                        it.mainUserFlag = true
                        pillViewModel.updatePill(it)
                    }
                }
            }

            if (dispenseChamber2ET.visibility == VISIBLE) {
                tempPillsList.forEach {
                    if (it.uid == "chamber_2") {
                        it.demanded = secondChoice
                        it.readFromMain = false
                        it.editFromMain = false
                        it.mainUserFlag = true
                        pillViewModel.updatePill(it)
                    }
                }
            }

            if (dispenseChamber3ET.visibility == VISIBLE) {
                tempPillsList.forEach {
                    if (it.uid == "chamber_3") {
                        it.demanded = thirdChoice
                        it.readFromMain = false
                        it.editFromMain = false
                        it.mainUserFlag = true
                        pillViewModel.updatePill(it)
                    }
                }
            }

            activity?.onBackPressed()
        }

        dispenseCancelBTN.setOnClickListener {
            activity?.onBackPressed()
        }

    }
}
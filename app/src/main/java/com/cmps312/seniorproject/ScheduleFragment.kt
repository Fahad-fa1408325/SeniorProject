package com.cmps312.seniorproject

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_schedule.*


class ScheduleFragment : Fragment(R.layout.fragment_schedule) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addScheduleBTN.setOnClickListener {
            findNavController().navigate(R.id.action_scheduleFragment_to_addScheduleFragment)
        }
        
    }
}
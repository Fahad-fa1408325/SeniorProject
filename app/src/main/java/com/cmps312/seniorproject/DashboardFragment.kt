package com.cmps312.seniorproject

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_dashboard.*


class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val auth = FirebaseAuth.getInstance()

        logOutCardView.setOnClickListener {
            activity?.onBackPressed()
            Toast.makeText(context, "Logged out Successfully", Toast.LENGTH_SHORT).show()
            auth.signOut()
        }

        nfcCardView.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_NFCFragment)
        }

        scheduleCardView.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_scheduleFragment)
        }

        addUserCardView.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addGuestUserFragment)
        }

        statisticsCardView.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_statFragment)
        }

        dispenseOnDemandCardView.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_dispenseOnDemandFragment)
        }

    }

}
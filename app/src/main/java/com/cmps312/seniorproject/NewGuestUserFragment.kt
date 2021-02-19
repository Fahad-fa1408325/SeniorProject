package com.cmps312.seniorproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_new_guest_user.*


class NewGuestUserFragment : Fragment(R.layout.fragment_new_guest_user) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addUserBTN.setOnClickListener {

            //Here the implementation of adding guest user to the list
            activity?.onBackPressed()

        }

        cancelBTN.setOnClickListener {

            activity?.onBackPressed()

        }
    }
}
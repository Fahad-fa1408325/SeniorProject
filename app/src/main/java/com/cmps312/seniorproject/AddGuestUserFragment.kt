package com.cmps312.seniorproject

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cmps312.seniorproject.model.entity.GuestUser
import kotlinx.android.synthetic.main.fragment_add_guest_user.*


class AddGuestUserFragment : Fragment(R.layout.fragment_add_guest_user) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addNewGusetUserBTN.setOnClickListener {
            findNavController().navigate(R.id.action_addGuestUserFragment_to_newGuestUserFragment)
        }

    }


    private fun deleteGuestUser(user : GuestUser){




    }

}
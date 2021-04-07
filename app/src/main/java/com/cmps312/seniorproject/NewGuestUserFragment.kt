package com.cmps312.seniorproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.cmps312.seniorproject.model.entity.GuestUser
import com.cmps312.seniorproject.ui.viewmodel.PillViewModel
import kotlinx.android.synthetic.main.fragment_new_guest_user.*


class NewGuestUserFragment : Fragment(R.layout.fragment_new_guest_user) {

    private val pillViewModel: PillViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addUserBTN.setOnClickListener {

            //Here the implementation of adding guest user to the list
            if (!gusetUserEmailET.text.toString().isNullOrEmpty()) {
                if (gusetUserEmailET.text.toString() != pillViewModel.currentUser.email) {
                    var user = GuestUser()

                    user.email = gusetUserEmailET.text.toString()
                    user.uid = pillViewModel.currentUser.uid
                    user.mainUserEmail = pillViewModel.currentUser.email

                    pillViewModel.addGuestUser(user)

                    activity?.onBackPressed()
                } else {
                    Toast.makeText(
                        context,
                        "Can't use the current email as guest email",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                Toast.makeText(
                    context,
                    "Guest User Email Field is Required***",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        cancelBTN.setOnClickListener {

            activity?.onBackPressed()

        }
    }
}
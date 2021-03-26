package com.cmps312.seniorproject

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmps312.seniorproject.model.entity.GuestUser
import com.cmps312.seniorproject.ui.account.GuestUserAdapter
import com.cmps312.seniorproject.ui.viewmodel.PillViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_guest_user.*


class AddGuestUserFragment : Fragment(R.layout.fragment_add_guest_user) {

    private val pillViewModel: PillViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val guestUserAdapter = GuestUserAdapter(::deleteGuestUserListener)

        GuestRecyclerView.apply {
            adapter = guestUserAdapter
            layoutManager = LinearLayoutManager(context)
        }

        pillViewModel.guestUsers.observe(viewLifecycleOwner) {
            guestUserAdapter.guestUsers = it
        }


        addNewGusetUserBTN.setOnClickListener {
            findNavController().navigate(R.id.action_addGuestUserFragment_to_newGuestUserFragment)
        }

    }

    private fun deleteGuestUserListener(user: GuestUser) {

        pillViewModel.guestUsers.value?.let {
            pillViewModel.deleteGuestUser(user)
            Snackbar.make(requireView(), "${user.email} removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO") {
                    pillViewModel.addGuestUser(user)
                }.show()
        }

    }

}
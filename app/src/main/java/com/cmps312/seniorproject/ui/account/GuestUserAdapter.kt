package com.cmps312.seniorproject.ui.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmps312.seniorproject.R
import com.cmps312.seniorproject.databinding.GuestItemBinding
import com.cmps312.seniorproject.model.entity.GuestUser
import com.cmps312.seniorproject.ui.viewmodel.PillViewModel

class GuestUserAdapter(val deleteGuestUserListener: (GuestUser) -> Unit) :
    RecyclerView.Adapter<GuestUserAdapter.GuestUserViewHolder>() {

    var guestUsers = listOf<GuestUser>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class GuestUserViewHolder(private val binding: GuestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: GuestUser) {
            binding.user = user
            binding.guestUserDeleteBTN.setOnClickListener { deleteGuestUserListener(user) }
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GuestUserAdapter.GuestUserViewHolder {
        val binding: GuestItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.guest_item,
            parent,
            false
        )
        return GuestUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuestUserAdapter.GuestUserViewHolder, position: Int) {
        holder.bind(guestUsers[position])
    }

    override fun getItemCount(): Int = guestUsers.size


}
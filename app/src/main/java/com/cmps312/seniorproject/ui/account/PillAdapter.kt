package com.cmps312.seniorproject.ui.account

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmps312.seniorproject.R
import com.cmps312.seniorproject.databinding.PillItemBinding
import com.cmps312.seniorproject.model.entity.Pill
import com.cmps312.seniorproject.repository.PillRepo

class PillAdapter(val editPillListener: (Pill) -> Unit, val deletePillListener: (Pill) -> Unit) :
    RecyclerView.Adapter<PillAdapter.PillViewHolder>() {

    var pills = listOf<Pill>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class PillViewHolder(private val binding: PillItemBinding) :

        RecyclerView.ViewHolder(binding.root) {

        fun bind(pill: Pill) {

            binding.pill = pill


            if (pill.mainUserFlag) {

                binding.editBTN.setOnClickListener { editPillListener(pill) }
                binding.deleteBTN.setOnClickListener { deletePillListener(pill) }
                binding.editBTN.visibility = View.VISIBLE
                binding.deleteBTN.visibility = View.VISIBLE
                binding.belongsToET.visibility = View.INVISIBLE

                Log.d(
                    "adapter",
                    "name :${pill.name} main flag"
                )
            } else if (pill.editFromMain) {
                binding.editBTN.setOnClickListener { editPillListener(pill) }
                binding.deleteBTN.visibility = View.INVISIBLE
                val string = "${pill.uid} on ${pill.mainUserEmail}"
                binding.mainUserET.text = string.toString()
                binding.belongsToET.visibility = View.VISIBLE
                Log.d(
                    "adapter",
                    "name :${pill.name} edit flag"
                )

            } else if (pill.readFromMain) {
                val string = "${pill.uid} on ${pill.mainUserEmail}"
                binding.mainUserET.text = string.toString()
                binding.belongsToET.visibility = View.VISIBLE
                binding.editBTN.visibility = View.INVISIBLE
                binding.deleteBTN.visibility = View.INVISIBLE
                Log.d(
                    "adapter",
                    "name :${pill.name} + read flag"
                )
            } else {

                binding.mainUserET.text = pill.mainUserEmail
                binding.belongsToET.visibility = View.VISIBLE
                binding.editBTN.visibility = View.INVISIBLE
                binding.deleteBTN.visibility = View.INVISIBLE

                Log.d(
                    "adapter",
                    "name :${pill.name} passes through else"
                )
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PillViewHolder {
        val binding: PillItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.pill_item,
            parent,
            false
        )
        return PillViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PillAdapter.PillViewHolder, position: Int) {
        holder.bind(pills[position])
    }

    override fun getItemCount(): Int = pills.size

}

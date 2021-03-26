package com.cmps312.seniorproject.ui.account

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.cmps312.seniorproject.R
import com.cmps312.seniorproject.databinding.PillItemBinding
import com.cmps312.seniorproject.model.entity.Pill

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

                binding.editBTN.visibility = View.VISIBLE
                binding.deleteBTN.visibility = View.VISIBLE

                binding.editBTN.setOnClickListener { editPillListener(pill) }
                binding.deleteBTN.setOnClickListener { deletePillListener(pill) }
                binding.belongsToET.visibility = View.INVISIBLE

            } else {

                binding.editBTN.visibility = View.INVISIBLE
                binding.deleteBTN.visibility = View.INVISIBLE
                binding.mainUserET.text = pill.mainUserEmail

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

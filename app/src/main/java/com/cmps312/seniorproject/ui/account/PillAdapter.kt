package com.cmps312.seniorproject.ui.account

import android.view.LayoutInflater
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
            binding.editBTN.setOnClickListener { editPillListener(pill) }
            binding.deleteBTN.setOnClickListener { deletePillListener(pill) }
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

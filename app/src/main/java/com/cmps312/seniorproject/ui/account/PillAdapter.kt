package com.cmps312.seniorproject.ui.account

import android.graphics.Color
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
            Log.d("pillAdapter", "${pill.name} + ${pill.mainUserEmail}")

            if (pill.mainUserFlag) {

                binding.itemRepatidlyTV.text = "Every ${pill.repeadtly} Hour(s)"
                binding.editBTN.setOnClickListener { editPillListener(pill) }
                binding.deleteBTN.setOnClickListener { deletePillListener(pill) }
                binding.editBTN.visibility = View.VISIBLE
                binding.deleteBTN.visibility = View.VISIBLE
                binding.pillImageView.setImageResource(R.drawable.ic_medicine)

            } else if (pill.editFromMain) {

                binding.itemRepatidlyTV.text = "Every ${pill.repeadtly} Hour(s)"
                binding.editBTN.setOnClickListener { editPillListener(pill) }
                binding.deleteBTN.visibility = View.INVISIBLE
                val string = "${pill.uid} on ${pill.mainUserEmail}"
                binding.mainUserET.text = string.toString()
                binding.pillImageView.setImageResource(R.drawable.ic_medicine3)
                binding.editBTN.setBackgroundColor(Color.RED)

            } else if (pill.readFromMain) {

                binding.itemRepatidlyTV.text = "Every ${pill.repeadtly} Hour(s)"
                val string = "${pill.uid} on ${pill.mainUserEmail}"
                binding.mainUserET.text = string.toString()
                binding.belongsToET.visibility = View.VISIBLE
                binding.editBTN.visibility = View.INVISIBLE
                binding.deleteBTN.visibility = View.INVISIBLE
                binding.pillImageView.setImageResource(R.drawable.ic_medicine3)

            } else {

                binding.itemRepatidlyTV.text = "Every ${pill.repeadtly} Hour(s)"
                binding.mainUserET.text = pill.mainUserEmail
                binding.editBTN.visibility = View.INVISIBLE
                binding.deleteBTN.visibility = View.INVISIBLE
                binding.pillImageView.setImageResource(R.drawable.ic_medicine2)

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

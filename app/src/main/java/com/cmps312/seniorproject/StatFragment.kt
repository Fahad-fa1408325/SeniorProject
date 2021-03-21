package com.cmps312.seniorproject

import android.os.Build
import android.os.Bundle
import android.util.Half.toFloat
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.fragment_stat.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class StatFragment : Fragment(R.layout.fragment_stat) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLineChartData()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setLineChartData() {

        val xValue = mutableListOf<String>()
        xValue.add("11.0 AM")
        xValue.add("12.0 AM")
        xValue.add("1.0 PM")
        xValue.add("3.0 PM")
        xValue.add("6.0 PM")

        //LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))

        val lineEntry = mutableListOf<Entry>()
        lineEntry.add(Entry(1F, 0F))
        lineEntry.add(Entry(2F, 1F))
        lineEntry.add(Entry(3F, 0F))
        lineEntry.add(Entry(4F, 1F))
        lineEntry.add(Entry(5F, 1F))

        val lineDataSet = LineDataSet(lineEntry, "Panadol")
        lineDataSet.circleRadius = 0f
        lineDataSet.color = resources.getColor(R.color.black)

        lineChart.data = LineData(lineDataSet)
        lineChart.setBackgroundColor(resources.getColor(R.color.white))
        lineChart.animateXY(3000, 3000)


    }

}
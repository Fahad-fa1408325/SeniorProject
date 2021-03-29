package com.cmps312.seniorproject

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmps312.seniorproject.model.alarm.AlarmReceiver
import com.cmps312.seniorproject.model.entity.Pill
import com.cmps312.seniorproject.ui.account.PillAdapter
import com.cmps312.seniorproject.ui.viewmodel.PillViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_schedule.*
import java.util.*


class ScheduleFragment : Fragment(R.layout.fragment_schedule) {
    private val pillViewModel: PillViewModel by activityViewModels()

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pillAdapter = PillAdapter(::editPillListener, ::deletePillListener)

        scheduleRecyclerView.apply {
            adapter = pillAdapter
            layoutManager = LinearLayoutManager(context)
        }

        pillViewModel.pills.observe(viewLifecycleOwner) {
            pillAdapter.pills = it
        }

        addScheduleBTN.setOnClickListener {
            findNavController().navigate(R.id.action_scheduleFragment_to_addScheduleFragment)
        }

    }

    private fun editPillListener(pill: Pill) {
        pillViewModel.selectedPill = pill
        findNavController().navigate(R.id.action_scheduleFragment_to_addScheduleFragment)
    }

    private fun deletePillListener(pill: Pill) {

        var time = pill.time.split(":")
        var hours = time[0].toInt()
        var minutes = time[1].toInt()

        var alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent.putExtra("message", pill.name)
        alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        PendingIntent.getBroadcast(
            context,
            pill.requestKey,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        this.alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                pill.requestKey,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val cal: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, minutes)
            set(Calendar.SECOND, 0)
        }


         pillViewModel.pills.value?.let {
            pillViewModel.deletePill(pill)
            alarmMgr?.cancel(this.alarmIntent)

            Snackbar.make(requireView(), "${pill.name} removed", Snackbar.LENGTH_LONG)
                .setAction("UNDO") {

                    alarmMgr?.set(
                        AlarmManager.RTC_WAKEUP, cal.timeInMillis,
                        this.alarmIntent
                    )
                    alarmMgr?.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        cal.timeInMillis,
                        3600000 * pill.dosage.toString().toLong(),
                        this.alarmIntent
                    )

                    pillViewModel.addPill(pill)
                }.show()
        }
    }

}
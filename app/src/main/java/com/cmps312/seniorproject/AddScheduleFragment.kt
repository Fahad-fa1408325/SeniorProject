package com.cmps312.seniorproject

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.cmps312.seniorproject.model.alarm.AlarmReceiver
import com.cmps312.seniorproject.model.entity.Pill
import com.cmps312.seniorproject.ui.viewmodel.PillViewModel
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import java.text.SimpleDateFormat
import java.util.*

class AddScheduleFragment : Fragment(R.layout.fragment_add_schedule) {

    private val pillViewModel: PillViewModel by activityViewModels()

    var hours: Int = 66
    var minutes: Int = 66
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var randomValue = (1..100000).random()
        var previousPill = pillViewModel.selectedPill
        var pill = Pill()
        var newPill = Pill()

        if (!previousPill.name.isNullOrEmpty()) {
            pillNameET.setText(previousPill.name)
            timeTV.text = previousPill.time
            dosageETN.setText(Integer.toString(previousPill.dosage))
            repeadtlyETN.setText(Integer.toString(previousPill.repeadtly))
            randomValue = previousPill.requestKey
            addNewScheduleBTN.text = "Edit"

            var time = previousPill.time.split(":")
            hours = time[0].toInt()
            minutes = time[1].toInt()
        }

        pickTimeBTN.setOnClickListener {

            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                timeTV.text = SimpleDateFormat("HH:mm").format(cal.time)
                hours = hour
                minutes = minute

            }
            TimePickerDialog(
                view.context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                    Calendar.MINUTE
                ), false
            ).show()
        }

        addNewScheduleBTN.setOnClickListener {
            if (hours == 66 || minutes == 66 || pillNameET.text.isNullOrEmpty() || dosageETN.text.toString()
                    .isNullOrEmpty() || repeadtlyETN.text.toString()
                    .isNullOrEmpty()
            ) {
                Toast.makeText(
                    context,
                    "Filling All Fields is Required ***",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                if (!previousPill.name.isNullOrEmpty()) {

                    previousPill.name = pillNameET.text.toString()
                    previousPill.time = timeTV.text.toString()
                    previousPill.dosage = dosageETN.text.toString().toInt()
                    previousPill.repeadtly = repeadtlyETN.text.toString().toInt()
                    previousPill.uid = previousPill.uid
                    previousPill.mainUserFlag = true
                    previousPill.editFromMain = false
                    previousPill.readFromMain = false

                    pillViewModel?.selectedPill?.let { it1 -> pillViewModel.updatePill(it1) }
                    pillViewModel.selectedPill = pill
                    addNewScheduleBTN.text = "Edit"

                    alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                        intent.putExtra("message", pillNameET.text.toString())
                        PendingIntent.getBroadcast(
                            context,
                            randomValue,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    }

                    val cal: Calendar = Calendar.getInstance().apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.HOUR_OF_DAY, hours)
                        set(Calendar.MINUTE, minutes)
                        set(Calendar.SECOND, 0)
                    }

                    alarmMgr?.set(
                        AlarmManager.RTC_WAKEUP, cal.timeInMillis,
                        alarmIntent
                    )

                    alarmMgr?.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        cal.timeInMillis,
                        3600000 * dosageETN.text.toString().toLong(),
                        alarmIntent
                    )

                    Toast.makeText(context, "Alarm edited Successfully", Toast.LENGTH_SHORT).show()

                    activity?.onBackPressed()

                } else {

                    newPill.name = pillNameET.text.toString()
                    newPill.time = timeTV.text.toString()
                    newPill.dosage = dosageETN.text.toString().toInt()
                    newPill.repeadtly = repeadtlyETN.text.toString().toInt()
                    newPill.uid = pillViewModel.currentUser.uid
                    newPill.requestKey = randomValue
                    newPill.mainUserFlag = true
                    newPill.editFromMain = false
                    newPill.readFromMain = false

                    pillViewModel.addPill(newPill)
                    pillViewModel.selectedPill = pill

                    alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                        intent.putExtra("message", pillNameET.text.toString())
                        PendingIntent.getBroadcast(
                            context,
                            randomValue,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    }

                    val cal: Calendar = Calendar.getInstance().apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.HOUR_OF_DAY, hours)
                        set(Calendar.MINUTE, minutes)
                        set(Calendar.SECOND, 0)
                    }

                    alarmMgr?.set(
                        AlarmManager.RTC_WAKEUP, cal.timeInMillis,
                        alarmIntent
                    )

                    alarmMgr?.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        cal.timeInMillis,
                        3600000 * dosageETN.text.toString().toLong(),
                        alarmIntent
                    )

                    Toast.makeText(context, "Alarm set Successfully", Toast.LENGTH_SHORT).show()
                    activity?.onBackPressed()
                }
            }
        }

        scheduleCancelBTN.setOnClickListener {
            pillViewModel.selectedPill = pill
            activity?.onBackPressed()
        }

    }

}
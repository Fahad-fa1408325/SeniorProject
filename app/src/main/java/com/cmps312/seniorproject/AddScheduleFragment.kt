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
import com.cmps312.seniorproject.model.alarm.AlarmReceiver
import kotlinx.android.synthetic.main.fragment_add_schedule.*
import java.text.SimpleDateFormat
import java.util.*

class AddScheduleFragment : Fragment(R.layout.fragment_add_schedule) {
    var hours: Int = 0
    var minutes: Int = 0
    var alarmStartTime: Long = 0
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notificationId = 1

        pickTimeBTN.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                timeTV.text = SimpleDateFormat("HH:mm").format(cal.time)
                hours = hour
                minutes = minute
                alarmStartTime = cal.timeInMillis
            }
            TimePickerDialog(
                view.context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                    Calendar.MINUTE
                ), false
            ).show()
        }

        addNewScheduleBTN.setOnClickListener {
            if (hours == 0 && minutes == 0 && alarmStartTime.equals(0)) {
                Toast.makeText(
                    context,
                    "Please Choose a time",
                    Toast.LENGTH_SHORT
                ).show()
            }

            var intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtra("notificationId", 1)
            intent.putExtra("message", pillNameET.text)
            alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }
            //var pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            /*alarmMgr?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                alarmStartTime,
                24 * 60 * 60 * 1000,
                pendingIntent
            )*/
            Toast.makeText(context, "Alarm set Successfully", Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()
        }




        scheduleCancelBTN.setOnClickListener {
            activity?.onBackPressed()
        }

    }

}
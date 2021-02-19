package com.cmps312.seniorproject.model.alarm

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.cmps312.seniorproject.AddScheduleFragment
import com.cmps312.seniorproject.MainActivity
import com.cmps312.seniorproject.R
import kotlinx.android.synthetic.main.fragment_add_schedule.*

class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {

        var notificationId = intent?.getIntExtra("notificationId", 0)
        var message = intent?.getStringExtra("message")

        var mainIntent = Intent(context, MainActivity::class.java)

        var contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0)

        var notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var builder = Notification.Builder(context, "channelId")

        builder.setSmallIcon(R.drawable.ic_pills)
            .setContentTitle("It's Time")
            .setContentText(message)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setPriority(Notification.PRIORITY_MAX)
            .setDefaults(Notification.DEFAULT_ALL)

        if (notificationId != null) {
            notificationManager.notify(notificationId, builder.build())
        }
    }
}
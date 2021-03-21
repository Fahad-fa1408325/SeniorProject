package com.cmps312.seniorproject.model.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.cmps312.seniorproject.MainActivity
import com.cmps312.seniorproject.R


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        var message = intent?.getStringExtra("message")

        var notificationManager1 = context?.let { NotificationManagerCompat.from(it) };
        val notification = context?.let {
            NotificationCompat.Builder(it, "channel1")
                .setSmallIcon(R.drawable.ic_pills)
                .setContentTitle("Pill's Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build()
        }
        if (notification != null) {
            notificationManager1?.notify(1, notification)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel 1"
            val descriptionText = "Pill's Notification channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("channel1", name, importance).apply {
                description = descriptionText
            }

            var notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            /*
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
             */
        }
    }
}
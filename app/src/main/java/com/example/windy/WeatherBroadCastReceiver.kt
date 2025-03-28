package com.example.windy

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class WeatherBroadCastReceiver : BroadcastReceiver() {

    companion object{
        const val CHANNEL_ID = "1"
        const val NOTIFICATION_ID = 10

    }

    override fun onReceive(context: Context, intent: Intent) {

        val intent = Intent(context, MainActivity::class.java)
            .apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                )
            }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )



        val name = "Weather Alarm"
        val notificationDesc = "Weather Notification"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
              description = notificationDesc
        }
        val notificationManager : NotificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)


        val builder = NotificationCompat.Builder(context,CHANNEL_ID)
            .setSmallIcon(R.drawable.clouds)
            .setContentTitle("Weather Title")
            .setContentText("Click ya 3rs click")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

       notificationManager.notify(NOTIFICATION_ID,builder.build())
    }
}
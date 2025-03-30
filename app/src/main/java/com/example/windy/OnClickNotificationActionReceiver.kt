package com.example.windy

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.windy.data.model.Alarm
import com.example.windy.utils.AlarmScheduler
import java.util.Calendar

class OnClickNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getIntExtra("alarmId",0)
        val notificationManager = NotificationManagerCompat.from(context)

        when(intent.action){
            "Dismiss" -> notificationManager.cancel(alarmId)
            "Snooze" -> {
                notificationManager.cancel(alarmId)
                val cityName = intent.getStringExtra("cityName")
                val calendar = Calendar.getInstance().apply {
                    add(Calendar.MINUTE,1)
                }

                val rescheduledAlarm = Alarm(
                    alarmId,
                    cityName.toString(),
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE)
                )
                AlarmScheduler(context).setAlarm(rescheduledAlarm)
            }
        }
    }
}
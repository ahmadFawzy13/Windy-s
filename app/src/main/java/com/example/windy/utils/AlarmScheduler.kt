package com.example.windy.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import com.example.windy.alarm.WeatherBroadCastReceiver
import com.example.windy.data.model.Alarm

class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setAlarm(alarm: Alarm){
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY,alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND,0)
        }
        val intent = Intent(context, WeatherBroadCastReceiver::class.java).apply {
            putExtra("alarmId",alarm.id)
            putExtra("cityName",alarm.cityName)
        }

        val pendingIntent = PendingIntent.getBroadcast(context,
            alarm.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE)

        if(alarmManager.canScheduleExactAlarms()){
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelAlarm(alarmId:Int){
        val intent = Intent(context, WeatherBroadCastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,
            alarmId,
            intent,
            PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
    }
}
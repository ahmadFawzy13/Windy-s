package com.example.windy.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.example.windy.MainActivity
import com.example.windy.R
import com.example.windy.data.repo.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherBroadCastReceiver: BroadcastReceiver() {

    companion object{
        const val CHANNEL_ID = "1"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationSong: Uri = "android.resource://com.example.windy/${R.raw.mario}".toUri()
        val cityName = intent.getStringExtra("cityName")
        val alarmId = intent.getIntExtra("alarmId",0)

        val generalIntent = Intent(context, MainActivity::class.java)
            .apply {
                addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                )
            }

        val dismissIntent = Intent(
            context,
            OnClickNotificationActionReceiver::class.java
        ).apply {
                action = "Dismiss"
                putExtra("alarmId",alarmId)
        }


        val snoozeIntent = Intent(
            context,
            OnClickNotificationActionReceiver::class.java
        ).apply {
                action = "Snooze"
            putExtra("alarmId",alarmId)
            putExtra("cityName",cityName)

        }


        val pendingGeneralIntent = PendingIntent.getActivity(
            context,
            alarmId,
            generalIntent,
            PendingIntent.FLAG_IMMUTABLE
        )


        val pendingDismissIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            dismissIntent,
            PendingIntent.FLAG_IMMUTABLE
        )


        val pendingSnoozeIntent = PendingIntent.getBroadcast(
            context,
            alarmId+1,
            snoozeIntent,
            PendingIntent.FLAG_IMMUTABLE
        )


        val name = "Weather Alarm"
        val notificationDesc = "Weather Notification"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channelSound = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()

        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
              description = notificationDesc
            setSound(notificationSong,channelSound)
        }
        val notificationManager : NotificationManager = context.getSystemService(NotificationManager::class.java).apply {
            createNotificationChannel(channel)
        }
        //notificationManager.createNotificationChannel(channel)


        val builder = NotificationCompat.Builder(context,CHANNEL_ID)
            .setSmallIcon(R.drawable.clouds)
            .setContentTitle(context.getString(R.string.weather_is_ready_for_you, cityName))
            .setContentText(context.getString(R.string.tap_to_see_forecast))
            .setContentIntent(pendingGeneralIntent)
            .setAutoCancel(true)
            .setSound(notificationSong)
            .addAction(
                R.drawable.close,
                "Dismiss",
                pendingDismissIntent
            )
            .addAction(
                    R.drawable.snooze,
                    "Snooze",
                pendingSnoozeIntent
            )



       notificationManager.notify(alarmId,builder.build())

        CoroutineScope(Dispatchers.IO).launch {
            deleteAlarm(context,alarmId)
        }

    }

    suspend fun deleteAlarm(context: Context, alarmId:Int){
        val repo = Repository.Companion.getInstance(context)
        repo.deleteAlarmById(alarmId)
    }
}
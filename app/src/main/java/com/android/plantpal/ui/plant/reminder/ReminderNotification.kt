package com.android.plantpal.ui.plant.reminder

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.plantpal.R

const val notificationID = 1
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class ReminderNotification : BroadcastReceiver()
{
    companion object {
        private var ringtone: Ringtone? = null

        fun stopAlarm(context: Context) {
            ringtone?.stop()
            ringtone = null
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        playAlarmSound(context)
        createNotification(context, intent.getStringExtra(titleExtra), intent.getStringExtra(messageExtra))
    }

    private fun playAlarmSound(context: Context) {
        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        ringtone = RingtoneManager.getRingtone(context, alarmSound)
        ringtone?.play()
    }

    private fun createNotification(context: Context, title: String?, message: String?) {
        val deepLinkIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("plantpal://reminders")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationID,
            deepLinkIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title ?: "Reminder")
            .setContentText(message ?: "It's time!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        manager.notify(notificationID, notification)
    }

}
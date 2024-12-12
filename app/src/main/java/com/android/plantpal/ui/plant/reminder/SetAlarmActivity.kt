package com.android.plantpal.ui.plant.reminder

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.plantpal.R
import com.android.plantpal.data.di.DatabaseProvider
import com.android.plantpal.data.remote.ReminderEntity
import com.android.plantpal.databinding.ActivitySetAlarmBinding
import android.provider.Settings
import android.util.Log
import kotlinx.coroutines.launch
import java.sql.Date
import java.util.Calendar

class SetAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetAlarmBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.timePicker.setIs24HourView(false)

        initDropdown()

        createNotificationChannel()
        binding.submitButton.setOnClickListener { scheduleNotification() }
    }

    private fun initDropdown() {
        val titleAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.title_alarm_list,
            android.R.layout.simple_dropdown_item_1line
        )
        binding.titleAlarmDropdown.setAdapter(titleAdapter)

        val messageAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.message_alarm_list,
            android.R.layout.simple_dropdown_item_1line
        )
        binding.messageAlarmDropdown.setAdapter(messageAdapter)
    }

    private fun saveReminderToDatabase(title: String, message: String, time: Long) {
        val reminder = ReminderEntity(title = title, message = message, time = time)

        lifecycleScope.launch {
            val db = DatabaseProvider.getDatabase(applicationContext)
            db.reminderDao().insert(reminder)
        }
    }

    private fun scheduleNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !checkExactAlarmPermission()) {
            requestExactAlarmPermission()
            return
        }

        val title = binding.titleAlarmDropdown.selectedItem.toString()
        val message = binding.messageAlarmDropdown.selectedItem.toString()

        if (title.isBlank() || message.isBlank()) {
            AlertDialog.Builder(this)
                .setTitle("Input Error")
                .setMessage("Please select a title and message from the dropdown.")
                .setPositiveButton("OK", null)
                .show()
            return
        }

        val time = getTime()

        saveReminderToDatabase(title, message, time)

        val intent = Intent(applicationContext, ReminderNotification::class.java)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        showAlert(time, title, message)
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: $title\nMessage: $message\nAt: ${dateFormat.format(date)} ${timeFormat.format(date)}"
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun getTime(): Long {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour

        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute, 0)
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.timeInMillis
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(
            channelID,
            name, importance
        ).apply {
            description = desc
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            setSound(soundUri, null)
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkExactAlarmPermission(): Boolean {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return alarmManager.canScheduleExactAlarms()
    }

    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                Log.d("PermissionCheck", "Exact alarm permission not granted. Showing dialog...")
                showPermissionDialog()
            } else {
                Log.d("PermissionCheck", "Exact alarm permission already granted.")
            }
        }
    }


    private fun showPermissionDialog() {
        Log.d("PermissionCheck", "Checking exact alarm permission...")
        AlertDialog.Builder(this)
            .setTitle("Izinkan Pengaturan Alarm")
            .setMessage("Untuk menggunakan fitur ini, Anda perlu mengaktifkan izin untuk menjadwalkan alarm yang tepat. Pergi ke Pengaturan?")
            .setPositiveButton("Ya") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}

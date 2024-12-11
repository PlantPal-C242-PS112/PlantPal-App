package com.android.plantpal.ui.plant.reminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.plantpal.R
import com.android.plantpal.data.di.DatabaseProvider
import com.android.plantpal.data.remote.ReminderEntity
import com.android.plantpal.databinding.FragmentRemindersBinding
import com.android.plantpal.ui.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RemindersFragment : Fragment() {

    private lateinit var binding: FragmentRemindersBinding
    private lateinit var reminderAdapter: ReminderItemAdapter
    private lateinit var reminderViewModel: ReminderViewModel
    private val reminderNotification = ReminderNotification()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRemindersBinding.inflate(inflater, container, false)

        reminderViewModel = ViewModelProvider(this)[ReminderViewModel::class.java]

        reminderAdapter = ReminderItemAdapter(
            onCancelClick = { reminder ->
                handleCancelClick(reminder)
            },
            onDoneClick = { reminder ->
                handleDoneClick(reminder)
            }
        )

        binding.recyclerViewReminders.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewReminders.adapter = reminderAdapter

        reminderViewModel.getAllReminders().observe(viewLifecycleOwner) { reminders ->
            reminderAdapter.submitList(reminders)
        }

        return binding.root
    }

    private fun handleCancelClick(reminder: ReminderEntity) {
        lifecycleScope.launch {
            val db = DatabaseProvider.getDatabase(requireContext())
            withContext(Dispatchers.IO) {
                db.reminderDao().deleteReminder(reminder)
            }
            Toast.makeText(requireContext(), "Reminder Canceled: ${reminder.title}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleDoneClick(reminder: ReminderEntity) {
        lifecycleScope.launch {
            val db = DatabaseProvider.getDatabase(requireContext())
            withContext(Dispatchers.IO) {
                reminder.isDone = true
                db.reminderDao().updateReminder(reminder)
            }
            val completedTextView: TextView = binding.root.findViewById(R.id.completedTextView)
            completedTextView.visibility = View.VISIBLE

            ReminderNotification.stopAlarm()
            Toast.makeText(requireContext(), "Reminder Done: ${reminder.title}", Toast.LENGTH_SHORT).show()        }
    }
}



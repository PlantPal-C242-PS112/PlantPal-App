package com.android.plantpal.ui.plant.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.plantpal.databinding.FragmentRemindersBinding


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
                ReminderNotification.stopAlarm(requireContext())
                reminderViewModel.deleteReminder(reminder)
                Toast.makeText(requireContext(), "Reminder Canceled: ${reminder.title}", Toast.LENGTH_SHORT).show()
            },
            onDoneClick = { reminder ->
                ReminderNotification.stopAlarm(requireContext())
                reminderViewModel.markAsDone(reminder)
                Toast.makeText(requireContext(), "Reminder Done: ${reminder.title}", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recyclerViewReminders.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewReminders.adapter = reminderAdapter

        reminderViewModel.getAllReminders().observe(viewLifecycleOwner) { reminders ->
            reminderAdapter.submitList(reminders)
        }

        return binding.root
    }
}



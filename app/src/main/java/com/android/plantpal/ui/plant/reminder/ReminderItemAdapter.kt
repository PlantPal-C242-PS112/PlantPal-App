package com.android.plantpal.ui.plant.reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.data.remote.ReminderEntity
import com.android.plantpal.databinding.ItemReminderBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ReminderItemAdapter (
    private val onCancelClick: (ReminderEntity) -> Unit,
    private val onDoneClick: (ReminderEntity) -> Unit
): ListAdapter<ReminderEntity, ReminderItemAdapter.ReminderViewHolder>(ReminderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val binding = ItemReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReminderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val reminder = getItem(position)
        holder.bind(reminder)
    }

    inner class ReminderViewHolder(private val binding: ItemReminderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reminder: ReminderEntity) {
            binding.titleTextView.text = reminder.title
            binding.messageTextView.text = reminder.message

            val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
            val formattedTime = dateFormat.format(reminder.time) // Pastikan reminder.time bertipe Date atau Long
            binding.timeTextView.text = formattedTime

            binding.cancelButton.setOnClickListener {
                onCancelClick(reminder)
            }

            binding.doneButton.setOnClickListener {
                onDoneClick(reminder)
            }
        }
    }

    class ReminderDiffCallback : DiffUtil.ItemCallback<ReminderEntity>() {
        override fun areItemsTheSame(oldItem: ReminderEntity, newItem: ReminderEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ReminderEntity, newItem: ReminderEntity): Boolean {
            return oldItem == newItem
        }
    }
}


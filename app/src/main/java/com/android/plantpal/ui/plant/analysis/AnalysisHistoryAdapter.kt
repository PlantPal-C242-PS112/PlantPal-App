package com.android.plantpal.ui.plant.analysis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.R
import com.android.plantpal.data.remote.response.DiagnosisItem
import com.android.plantpal.databinding.ItemAnalysisBinding
import com.bumptech.glide.Glide
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class AnalysisHistoryAdapter : RecyclerView.Adapter<AnalysisHistoryAdapter.AnalysisHistoryViewHolder>() {

    private var diagnosisList: List<DiagnosisItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalysisHistoryViewHolder {
        val binding = ItemAnalysisBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnalysisHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnalysisHistoryViewHolder, position: Int) {
        val diagnosisItem = diagnosisList[position]
        holder.bind(diagnosisItem)
    }

    override fun getItemCount(): Int = diagnosisList.size

    fun submitList(list: List<DiagnosisItem>) {
        diagnosisList = list
        notifyDataSetChanged()
    }

    inner class AnalysisHistoryViewHolder(private val binding: ItemAnalysisBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(diagnosisItem: DiagnosisItem) {
            binding.tvDiseaseName.text = diagnosisItem.result.message

            val formattedDate = formatDate(diagnosisItem.result.plantId)
            binding.tvDate.text = formattedDate

            Glide.with(binding.root.context)
                .load(diagnosisItem.result.imageUrl)
                .placeholder(R.drawable.ic_image)
                .into(binding.ivIcon)
        }

        private fun formatDate(plantId: Int): String {
            val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return sdf.format(Date(plantId.toLong()))
        }
    }
}
package com.android.plantpal.ui.plant.analysis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.R
import com.android.plantpal.data.remote.response.DiagnosisItem
import com.android.plantpal.databinding.ItemAnalysisBinding
import com.android.plantpal.ui.utils.formatToLocalDateTime
import com.bumptech.glide.Glide

class AnalysisHistoryAdapter(
    private val onClick: (DiagnosisItem) -> Unit
) : RecyclerView.Adapter<AnalysisHistoryAdapter.AnalysisHistoryViewHolder>() {

    private var diagnosisList: List<DiagnosisItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalysisHistoryViewHolder {
        val binding = ItemAnalysisBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnalysisHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnalysisHistoryViewHolder, position: Int) {
        val diagnosisItem = diagnosisList[position]
        holder.bind(diagnosisItem)

        holder.itemView.setOnClickListener {
            onClick(diagnosisItem)
        }
    }

    override fun getItemCount(): Int = diagnosisList.size

    fun submitList(list: List<DiagnosisItem>) {
        diagnosisList = list
    }

    inner class AnalysisHistoryViewHolder(private val binding: ItemAnalysisBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(diagnosisItem: DiagnosisItem) {

            val formattedDate = formatToLocalDateTime(diagnosisItem.createdAt)
            binding.tvDate.text = formattedDate

            Glide.with(binding.root.context)
                .load(diagnosisItem.result.imageUrl)
                .placeholder(R.drawable.ic_image)
                .into(binding.ivIcon)

            if (diagnosisItem.result.message.startsWith("We are not sure", ignoreCase = true)) {
                binding.tvDiseaseName.text = diagnosisItem.result.message
                binding.plantType1.visibility = View.GONE
                binding.plantType2.visibility = View.VISIBLE
                binding.iconNext.visibility = View.GONE
            } else{
                binding.tvDiseaseName.text = diagnosisItem.result.className
                    .split("__")
                    .getOrNull(1)
                    .orEmpty()
                binding.plantType2.visibility = View.GONE
                binding.plantType1.visibility = View.VISIBLE
                binding.iconNext.visibility = View.VISIBLE
            }
        }
    }
}
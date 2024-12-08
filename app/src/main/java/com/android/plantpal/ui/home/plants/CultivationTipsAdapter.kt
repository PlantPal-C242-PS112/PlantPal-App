package com.android.plantpal.ui.home.plants

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.databinding.ItemCultivationTipBinding

class CultivationTipsAdapter(private val tipsList: List<String>) : RecyclerView.Adapter<CultivationTipsAdapter.TipsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipsViewHolder {
        val binding = ItemCultivationTipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TipsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TipsViewHolder, position: Int) {
        holder.bind(tipsList[position])
    }

    override fun getItemCount(): Int = tipsList.size

    inner class TipsViewHolder(private val binding: ItemCultivationTipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tip: String) {
            binding.webView.loadData(tip, "text/html", "UTF-8")
        }
    }
}


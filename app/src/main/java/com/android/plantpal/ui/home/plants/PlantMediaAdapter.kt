package com.android.plantpal.ui.home.plants

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.data.remote.response.CultivationPlantMedia
import com.android.plantpal.databinding.ItemCultivationTipBinding

class PlantMediaAdapter(private val items: List<CultivationPlantMedia>) :
    RecyclerView.Adapter<PlantMediaAdapter.PlantMediaViewHolder>() {

    inner class PlantMediaViewHolder(private val binding: ItemCultivationTipBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(media: CultivationPlantMedia) {
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.loadUrl(media.url)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantMediaViewHolder {
        val binding = ItemCultivationTipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlantMediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantMediaViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}


package com.android.plantpal.ui.home.plants

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.R
import com.android.plantpal.data.remote.response.ListPlantDisease
import com.android.plantpal.databinding.ItemDiseaseBinding
import com.bumptech.glide.Glide

class PlantDiseaseAdapter(private val listDisease: List<ListPlantDisease>) : RecyclerView.Adapter<PlantDiseaseAdapter.PlantDiseaseViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    class PlantDiseaseViewHolder(private val binding: ItemDiseaseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: ListPlantDisease, callback: OnItemClickCallback?) {
            binding.tvItemDisease.text = disease.name
            binding.diseaseTypeText.visibility = View.GONE
            Glide.with(binding.ivItemPhotoDisease.context)
                .load(disease.image)
                .placeholder(R.drawable.ic_place_holder) // Gambar placeholder
                .error(R.drawable.ic_place_holder)
                .into(binding.ivItemPhotoDisease)

            binding.root.setOnClickListener {
                callback?.onItemClicked(disease)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PlantDiseaseViewHolder {
        val binding = ItemDiseaseBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return PlantDiseaseViewHolder(binding)
    }

    override fun getItemCount(): Int = listDisease.size

    override fun onBindViewHolder(holder: PlantDiseaseViewHolder, position: Int) {
        val event = listDisease[position]
        holder.bind(event, onItemClickCallback)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListPlantDisease)
    }

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        onItemClickCallback = callback
    }
}
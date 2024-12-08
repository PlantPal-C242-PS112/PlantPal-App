package com.android.plantpal.ui.home.disease

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.R
import com.android.plantpal.data.remote.response.ListItemDisease
import com.android.plantpal.databinding.ItemDiseaseBinding
import com.bumptech.glide.Glide

class DiseaseAdapter(private val listDisease: List<ListItemDisease>) : RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    class DiseaseViewHolder(private val binding: ItemDiseaseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: ListItemDisease, callback: OnItemClickCallback?) {
            binding.tvItemDisease.text = disease.name
            binding.diseaseTypeText.text = disease.plant.name
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

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DiseaseViewHolder {
        val binding = ItemDiseaseBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return DiseaseViewHolder(binding)
    }

    override fun getItemCount(): Int = listDisease.size

    override fun onBindViewHolder(holder: DiseaseViewHolder, position: Int) {
        val event = listDisease[position]
        holder.bind(event, onItemClickCallback)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListItemDisease)
    }

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        onItemClickCallback = callback
    }
}
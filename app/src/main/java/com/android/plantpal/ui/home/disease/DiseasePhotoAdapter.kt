package com.android.plantpal.ui.home.disease

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.data.remote.response.DiseaseMediaItem
import com.android.plantpal.databinding.ItemPictureDiseaseBinding
import com.bumptech.glide.Glide

class DiseasePhotoAdapter(private val disease: List<DiseaseMediaItem>) : RecyclerView.Adapter<DiseasePhotoAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(private val binding: ItemPictureDiseaseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: DiseaseMediaItem) {
            Glide.with(binding.diseaseImage.context)
                .load(disease.url)
                .into(binding.diseaseImage)

        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPictureDiseaseBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return PhotoViewHolder(binding)
    }

    override fun getItemCount(): Int = disease.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val disease = disease[position]
        holder.bind(disease)
    }
}
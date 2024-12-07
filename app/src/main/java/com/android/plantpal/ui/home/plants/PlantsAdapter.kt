package com.android.plantpal.ui.home.plants

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.data.remote.response.ListItemComment
import com.android.plantpal.data.remote.response.ListItemPlant
import com.android.plantpal.databinding.ItemPlantsBinding
import com.bumptech.glide.Glide

class PlantsAdapter(private val listPlants: List<ListItemPlant>) : RecyclerView.Adapter<PlantsAdapter.PlantViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    class PlantViewHolder(private val binding: ItemPlantsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(plant: ListItemPlant, callback: OnItemClickCallback?) {
            binding.tvItemNamePlants.text = plant.name
            Glide.with(binding.ivItemPhotoPlants.context)
                .load(plant.icon)
                .into(binding.ivItemPhotoPlants)

            binding.root.setOnClickListener {
                callback?.onItemClicked(plant)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantsBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return PlantViewHolder(binding)
    }

    override fun getItemCount(): Int = listPlants.size

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val event = listPlants[position]
        holder.bind(event, onItemClickCallback)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListItemPlant)
    }

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        onItemClickCallback = callback
    }
}
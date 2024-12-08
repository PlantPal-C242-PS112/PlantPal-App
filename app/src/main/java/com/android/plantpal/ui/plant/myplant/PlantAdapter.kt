package com.android.plantpal.ui.plant.myplant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.data.remote.response.UserPlant
import com.android.plantpal.databinding.ItemPlantBinding
import com.bumptech.glide.Glide

class PlantAdapter(
    private var plantList: List<UserPlant>,
    private val onItemClick: (UserPlant) -> Unit
) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plantList[position]
        holder.bind(plant)
    }

    override fun getItemCount(): Int = plantList.size

    fun updateData(data: List<UserPlant>) {
        plantList = data
        notifyDataSetChanged()
    }

    inner class PlantViewHolder(private val binding: ItemPlantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(plant: UserPlant) {
            binding.tvPlantName.text = plant.plant.name
            val sowingDateText = plant.sowingDate ?: "Tanggal tanam tidak tersedia"
            binding.tvPlantReminder.text = "Pengingat tanam: $sowingDateText"

            Glide.with(binding.root.context)
                .load(plant.plant.icon)
                .into(binding.imgPlant)

            binding.btnSetReminder.setOnClickListener {
                onItemClick(plant)
            }
        }
    }
    
}

package com.android.plantpal.ui.plant.myplant

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.data.remote.response.UserPlant
import com.android.plantpal.databinding.ItemPlantBinding
import com.android.plantpal.ui.plant.reminder.SetAlarmActivity
import com.bumptech.glide.Glide

class PlantAdapter(
    private val onItemClick: (UserPlant) -> Unit
) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    private var plantList: List<UserPlant> = listOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemPlantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val userPlant = plantList[position]
        holder.bind(userPlant)

        holder.itemView.setOnClickListener {
            onItemClick(userPlant)
        }
    }

    override fun getItemCount(): Int = plantList.size

    fun updateData(data: List<UserPlant>) {
        plantList = data
        notifyDataSetChanged()
    }

    inner class PlantViewHolder(private val binding: ItemPlantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userPlant: UserPlant) {
            binding.tvPlantName.text = userPlant.plant.name

            Glide.with(binding.root.context)
                .load(userPlant.plant.icon)
                .into(binding.imgPlant)

            binding.btnPlantReminder.setOnClickListener {
                val intent = Intent(binding.root.context, SetAlarmActivity::class.java)
                intent.putExtra("plantData", userPlant) // Pass the plant data to SetAlarmActivity
                binding.root.context.startActivity(intent)
            }
        }
    }
    
}

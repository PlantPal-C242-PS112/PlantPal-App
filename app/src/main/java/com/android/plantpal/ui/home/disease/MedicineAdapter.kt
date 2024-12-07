package com.android.plantpal.ui.home.disease

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.data.remote.response.MedicineLinksItem
import com.android.plantpal.databinding.ItemMedicineBinding

class MedicineAdapter(private val disease: List<MedicineLinksItem>) : RecyclerView.Adapter<MedicineAdapter.DetailDiseaseViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    class DetailDiseaseViewHolder(private val binding: ItemMedicineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(disease: MedicineLinksItem, callback: OnItemClickCallback?) {
            binding.medicineName.text = disease.name

            binding.link.setOnClickListener {
                callback?.onItemClicked(disease)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DetailDiseaseViewHolder {
        val binding = ItemMedicineBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return DetailDiseaseViewHolder(binding)
    }

    override fun getItemCount(): Int = disease.size

    override fun onBindViewHolder(holder: DetailDiseaseViewHolder, position: Int) {
        val disease = disease[position]
        holder.bind(disease, onItemClickCallback)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: MedicineLinksItem)
    }

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        onItemClickCallback = callback
    }
}
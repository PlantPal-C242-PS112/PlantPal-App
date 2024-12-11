package com.android.plantpal.ui.home.plants

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.R
import com.android.plantpal.databinding.ItemCultivationBinding
import com.android.plantpal.ui.utils.Section
import com.bumptech.glide.Glide

class CultTipsAdapter(private val sectionList: List<Section>) : RecyclerView.Adapter<CultTipsAdapter.SectionViewHolder>() {

    class SectionViewHolder(private val binding: ItemCultivationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(section: Section) {
            val cleanedHeading = section.heading.replace(Regex("^\\d+\\.\\s*"), "")

            binding.titleText.text = cleanedHeading
            binding.content.text = HtmlCompat.fromHtml(
                section.content,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            section.img?.let {
                Glide.with(binding.ivImage.context)
                    .load(it)
                    .into(binding.ivImage)
                binding.ivImage.visibility = View.VISIBLE
            } ?: run {
                binding.ivImage.visibility = View.GONE
            }


            var appear = false
            binding.hiddenLayout.visibility = View.GONE

            binding.chevron.setOnClickListener {
                if(appear){
                    appear = false
                    binding.chevron.setImageResource(R.drawable.baseline_expand_more_24)
                    binding.hiddenLayout.visibility = View.GONE
                }else{
                    appear = true
                    binding.chevron.setImageResource(R.drawable.baseline_expand_less_24)
                    binding.hiddenLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding = ItemCultivationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sectionList[position]
        holder.bind(section)
    }

    override fun getItemCount(): Int = sectionList.size
}


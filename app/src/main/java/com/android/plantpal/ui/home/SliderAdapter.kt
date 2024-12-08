package com.android.plantpal.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.plantpal.R
import com.android.plantpal.data.database.SliderData
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(
    private val sliderItems: List<SliderData>
) : SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slider_layout, parent, false)
        return SliderAdapterViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterViewHolder, position: Int) {
        val sliderItem = sliderItems[position]

        Glide.with(viewHolder.itemView)
            .load(sliderItem.imgResId)
            .fitCenter()
            .into(viewHolder.imageViewBackground)
    }

    override fun getCount(): Int {
        return sliderItems.size
    }

    class SliderAdapterViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        val imageViewBackground: ImageView = itemView.findViewById(R.id.myimage)
    }
}

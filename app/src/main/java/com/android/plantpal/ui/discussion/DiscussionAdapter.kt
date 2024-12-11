package com.android.plantpal.ui.discussion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.R
import com.android.plantpal.data.remote.response.ListItemDiscussions
import com.android.plantpal.databinding.ItemDiscussionBinding
import com.android.plantpal.ui.utils.calculateTimeDifference
import com.bumptech.glide.Glide

class DiscussionAdapter: PagingDataAdapter <ListItemDiscussions, DiscussionAdapter.DiscussionViewHolder>(DIFF_CALLBACK){

    private var onItemClickCallback: OnItemClickCallback? = null

    class DiscussionViewHolder(private val binding: ItemDiscussionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(discussion: ListItemDiscussions, callback: OnItemClickCallback?) {
            binding.username.text = discussion.user.username
            binding.title.text = discussion.title
            binding.desc.text = discussion.content
            binding.countLikes.text = discussion.totalLikes.toString()
            binding.plantTypeText.text = discussion.plant.name
            Glide.with(binding.ivItemPhoto.context)
                .load(discussion.mediaUrl)
                .error(R.drawable.ic_place_holder)
                .into(binding.ivItemPhoto)

            Glide.with(binding.profilePic.context)
                .load(discussion.user.profilePhoto)
                .error(R.drawable.person_pc)
                .into(binding.profilePic)

            val updatedAt = discussion.createdAt
            val hoursDifference = updatedAt?.let { calculateTimeDifference(it) }
            binding.hour.text = "$hoursDifference"

            binding.root.setOnClickListener {
                callback?.onItemClicked(discussion)
            }

            if(discussion.isLiked){
                binding.like.setImageResource(R.drawable.baseline_favorite_24)
            }else{
                binding.like.setImageResource(R.drawable.baseline_favorite_border_24)
            }

            binding.like.setOnClickListener {
                val updatedDiscussion = discussion.copy(isLiked = !discussion.isLiked)
                callback?.onLikeClicked(updatedDiscussion)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DiscussionViewHolder {
        val binding = ItemDiscussionBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return DiscussionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiscussionViewHolder, position: Int) {
        val discussion = getItem(position)
        if (discussion != null) {
            holder.bind(discussion, onItemClickCallback)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListItemDiscussions)
        fun onLikeClicked(discussion: ListItemDiscussions)
    }

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        onItemClickCallback = callback
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListItemDiscussions>() {
            override fun areItemsTheSame(oldItem: ListItemDiscussions, newItem: ListItemDiscussions): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListItemDiscussions,
                newItem: ListItemDiscussions
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
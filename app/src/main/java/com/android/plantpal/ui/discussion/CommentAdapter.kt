package com.android.plantpal.ui.discussion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.plantpal.R
import com.android.plantpal.data.remote.response.ListItemComment
import com.android.plantpal.databinding.ItemCommentBinding
import com.android.plantpal.ui.utils.calculateTimeDifference
import com.bumptech.glide.Glide

class CommentAdapter(private val listComments: List<ListItemComment>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    class CommentViewHolder(private val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: ListItemComment, callback: OnItemClickCallback?) {
            binding.desc.text = comment.content
            binding.username.text = comment.user.username
            Glide.with(binding.profilePic.context)
                .load(comment.user.profilePhoto)
                .error(R.drawable.person_pc)
                .into(binding.profilePic)

            val updatedAt = comment.updatedAt
            val hoursDifference = calculateTimeDifference(updatedAt)
            binding.hour.text = "$hoursDifference"

            binding.root.setOnClickListener {
                callback?.onItemClicked(comment)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return CommentViewHolder(binding)
    }

    override fun getItemCount(): Int = listComments.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val event = listComments[position]
        holder.bind(event, onItemClickCallback)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListItemComment)
    }

    fun setOnItemClickCallback(callback: OnItemClickCallback) {
        onItemClickCallback = callback
    }
}
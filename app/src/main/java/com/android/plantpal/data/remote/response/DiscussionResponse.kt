package com.android.plantpal.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class DiscussionResponse(

	@field:SerializedName("metadata")
	val metadata: Metadata,

	@field:SerializedName("data")
	val data: List<ListItemDiscussions>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

@Entity(tableName = "discussion")
data class ListItemDiscussions(

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("plant")
	val plant: Plant,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("total_likes")
	val totalLikes: Int? = null,

	@field:SerializedName("media_url")
	val mediaUrl: String? = null,

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("is_liked")
	val isLiked: Boolean
)

data class Metadata(

	@field:SerializedName("first_page")
	val firstPage: Int,

	@field:SerializedName("last_page")
	val lastPage: Int,

	@field:SerializedName("total_data")
	val totalData: Int,

	@field:SerializedName("total_page")
	val totalPage: Int,

	@field:SerializedName("current_page")
	val currentPage: Int
)

@Entity(tableName = "user")
data class User(

	@field:SerializedName("profile_photo")
	val profilePhoto: String? = null,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

@Entity(tableName = "plant")
data class Plant(

	@field:SerializedName("name")
	val name: String? = null,

	@PrimaryKey
	@field:SerializedName("id")
	val id: Int
)

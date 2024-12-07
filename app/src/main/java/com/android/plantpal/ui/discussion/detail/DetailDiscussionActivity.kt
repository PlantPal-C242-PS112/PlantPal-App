package com.android.plantpal.ui.discussion.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.plantpal.MainViewModel
import com.android.plantpal.R
import com.android.plantpal.data.remote.response.DetailDiscussionData
import com.android.plantpal.data.remote.response.ListItemComment
import com.android.plantpal.databinding.ActivityDetailDiscussionBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.discussion.CommentAdapter
import com.android.plantpal.ui.discussion.DiscussionViewModel
import com.android.plantpal.ui.utils.Result
import com.android.plantpal.ui.utils.calculateTimeDifference
import com.bumptech.glide.Glide

@Suppress("DEPRECATION")
class DetailDiscussionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDiscussionBinding
    private lateinit var viewModel: DiscussionViewModel
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDiscussionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory)[DiscussionViewModel::class.java]
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        val discId = intent.getIntExtra(KEY_DISCUSSION_ID, -1)

        observeDiscussionDetails(discId)
        setupAction()
    }

    private fun observeComments(discId: Int) {
        viewModel.getComments(discId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    val discussion = result.data
                    setCommentDiscussion(discussion)
                }
                is Result.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setCommentDiscussion(discussion: List<ListItemComment>) {
        binding.rvComment.layoutManager = LinearLayoutManager(this)
        val adapter = CommentAdapter(discussion)
        binding.rvComment.adapter = adapter
    }

    private fun setupAction() {


    }

    private fun observeDiscussionDetails(eventId: Int) {
        viewModel.getDetailDiscussion(eventId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val discussion = result.data
//                    checkFavoriteEvent(event.id.toString())
                    setDetailDiscussion(discussion)
                    observeComments(discussion.id)
//                    setupFavoriteButton(event)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setDetailDiscussion(discussion: DetailDiscussionData) {
        supportActionBar?.title = "Detail Discussion"

        binding.username.text = discussion.user.username
        binding.title.text = discussion.title
        binding.desc.text = discussion.content
        binding.countLikes.text = discussion.totalLikes.toString()
        binding.plantTypeText.text = discussion.plant.name
        Glide.with(binding.ivItemPhoto.context)
            .load(discussion.mediaUrl)
            .into(binding.ivItemPhoto)

        Glide.with(binding.profilePic.context)
            .load(discussion.user.profilePhoto)
            .into(binding.profilePic)

        val updatedAt = discussion.updatedAt
        val hoursDifference = calculateTimeDifference(updatedAt)
        binding.hour.text = hoursDifference

        updateLikeButtonUI(discussion.isLiked)
        Log.d("Islike", "${discussion.isLiked}")

        binding.like.setOnClickListener {
            handleLikeClick(discussion.id, discussion.isLiked)
        }

        binding.share.setOnClickListener {
            val discussionLink = "https://plantpal-api-454827052627.asia-southeast1.run.app/api/v1/discussions/${discussion.id}"

            val sharingIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, discussionLink)  // Share the link
                putExtra(Intent.EXTRA_SUBJECT, "Check out this discussion!")  // Set the subject (optional)
            }
            startActivity(Intent.createChooser(sharingIntent, "Share using"))
        }


        val userIdDisc = discussion.user.id.toString()
        mainViewModel.getSession().observe(this) { userModel ->
            val userIdSession = userModel.userId
            Log.d("UserCheck", "userIdSession: $userIdSession, userIdDisc: $userIdDisc")

            if (userIdDisc == userIdSession) {
                binding.moreAction.visibility = View.VISIBLE
                binding.moreAction.setOnClickListener {
                    showPupUpMenu(discussion.id)
                }
            }
        }



        binding.buttonSend.setOnClickListener{
            uploadComment(discussion.id)
        }

//        binding.like.setOnClickListener {
//            viewModel.likeOrDislike(discussion.id).observe(this){result ->
//                when (result) {
//                    is Result.Loading -> {
//                    }
//                    is Result.Success -> {
//                        if(result.data.data.is)
//                    }
//                    is Result.Error -> {
//                        binding.progressBar.visibility = View.GONE
//                        Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
    }

    private fun handleLikeClick(discussionId: Int, isLiked: Boolean) {
        viewModel.likeOrDislike(discussionId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    val newIsLiked = !isLiked
                    binding.countLikes.text = result.data.data.totalLikes.toString()
                    updateLikeButtonUI(newIsLiked)
                }
                is Result.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateLikeButtonUI(isLiked: Boolean) {
        if (isLiked) {
            binding.like.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            binding.like.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun showPupUpMenu(id: Int) {
        val popupMenu = PopupMenu(this, binding.moreAction)
        popupMenu.menuInflater.inflate(R.menu.pop_up_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    Toast.makeText(this, "Edit Discussion", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.delete -> {
                    showAlertDialog(
                        title = "Apakah Anda Yakin?",
                        message = "Apakah Anda Ingin Menghapus Diskusi Ini?",
                        positiveButtonText = "Ya",
                        negativeButtonText = "Batal",
                        onPositive = { deleteDiscussion(id) }
                    )
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showAlertDialog(
        title: String,
        message: String?,
        positiveButtonText: String,
        negativeButtonText: String,
        onPositive: (() -> Unit)? = null,
        onNegative: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveButtonText) { dialog, _ ->
                onPositive?.invoke()
                dialog.dismiss()
            }
            setNegativeButton(negativeButtonText) { dialog, _ ->
                onNegative?.invoke()
                dialog.dismiss()
            }
            create()
            show()
        }
    }
    private fun deleteDiscussion(id: Int) {
        viewModel.deleteDiscussion(id).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    finish()
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun uploadComment(id: Int) {
        val content = binding.edComment.text.toString()
        viewModel.addComment(id, content).observe(this){result ->
            when (result) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    binding.edComment.text?.clear()
                    observeComments(id)
                }
                is Result.Error -> {

                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val KEY_DISCUSSION_ID = "key_discussion_id"
    }
}
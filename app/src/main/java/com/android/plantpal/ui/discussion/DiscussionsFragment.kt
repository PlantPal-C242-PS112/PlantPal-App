package com.android.plantpal.ui.discussion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.plantpal.data.remote.response.ListItemDiscussions
import com.android.plantpal.databinding.FragmentDiscussionsBinding
import com.android.plantpal.ui.LoadingStateAdapter
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.discussion.add.AddDiscussionActivity
import com.android.plantpal.ui.discussion.detail.DetailDiscussionActivity
import com.android.plantpal.ui.utils.Result

class DiscussionsFragment : Fragment() {

    private var _binding: FragmentDiscussionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DiscussionViewModel
    private lateinit var adapter: DiscussionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscussionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModelFactory = ViewModelFactory.getInstance(requireContext().applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory)[DiscussionViewModel::class.java]

        setupAction()
        setupRecyclerView()
    }

    private fun setupAction() {
        binding.buttonAddDiscussion.setOnClickListener {
            val intent = Intent(requireContext(), AddDiscussionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        binding.rvListDiscussion.layoutManager = LinearLayoutManager(requireContext())

        adapter = DiscussionAdapter()

        binding.rvListDiscussion.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter { adapter.retry() }
        )

        viewModel.discussions.observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }

        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                showLoading(true)
            } else {
                showLoading(false)
            }
        }

        adapter.setOnItemClickCallback(object : DiscussionAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListItemDiscussions) {
                sendSelectedDiscussion(data)
            }

            override fun onLikeClicked(discussion: ListItemDiscussions) {
                handleLikeClick(discussion.id)
            }
        })
    }

    private fun handleLikeClick(id: Int) {
        viewModel.likeOrDislike(id).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    if(::adapter.isInitialized){
                        adapter.refresh()
                    }
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun sendSelectedDiscussion(data: ListItemDiscussions) {
        val intent = Intent(requireActivity(), DetailDiscussionActivity::class.java)
        intent.putExtra(DetailDiscussionActivity.KEY_DISCUSSION_ID, data.id)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if(::adapter.isInitialized){
            adapter.refresh()
        }
    }
}


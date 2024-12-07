package com.android.plantpal.ui.home.disease

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.plantpal.R
import com.android.plantpal.databinding.FragmentDiseaseBinding

class DiseaseFragment : Fragment() {

    private var _binding: FragmentDiseaseBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DiseaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiseaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvDisease.layoutManager = GridLayoutManager(requireContext(), 2)
    }


}
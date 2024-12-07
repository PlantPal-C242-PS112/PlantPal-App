package com.android.plantpal.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.plantpal.R
import com.android.plantpal.databinding.FragmentHomeBinding
import com.android.plantpal.ui.home.analyze.AnalyzeActivity
import com.android.plantpal.ui.home.plants.PlantActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
    }

    private fun setupAction() {
        binding.cardAnalyze.setOnClickListener{
            navigateToAnalyze()
        }
        binding.cardPlantMenu.setOnClickListener {
            navigateToPlant()
        }
        binding.cardDiseaseMenu.setOnClickListener{

        }
    }

    private fun navigateToPlant() {
        val intent = Intent(requireContext(), PlantActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAnalyze() {
        val intent = Intent(requireContext(), AnalyzeActivity::class.java)
        startActivity(intent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
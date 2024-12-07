package com.android.plantpal.ui.plant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.plantpal.R
import com.android.plantpal.databinding.FragmentPlantBinding
import com.google.android.material.tabs.TabLayoutMediator

class PlantFragment : Fragment() {

    private var _binding: FragmentPlantBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up ViewPager2 dengan Adapter
        val adapter = PlantPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // Hubungkan TabLayout dengan ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_my_plants)
                1 -> getString(R.string.tab_reminders)
                2 -> getString(R.string.tab_analysis_history)
                else -> null
            }
        }.attach()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

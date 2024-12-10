package com.android.plantpal.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.android.plantpal.R
import com.android.plantpal.data.database.SliderData
import com.android.plantpal.databinding.FragmentHomeBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.account.AccountViewModel
import com.android.plantpal.ui.home.analyze.AnalyzeActivity
import com.android.plantpal.ui.home.disease.DiseasesActivity
import com.android.plantpal.ui.home.plants.PlantActivity
import com.android.plantpal.ui.utils.Result
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: AccountViewModel
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
        val viewModelFactory = ViewModelFactory.getInstance(requireContext().applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory)[AccountViewModel::class.java]
        setupAction()
        setupSlider()
        setupData()
    }

    private fun setupData() {
        viewModel.getUserDetails().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.textName.visibility = View.GONE
                }
                is Result.Success -> {
                    binding.textName.visibility = View.VISIBLE
                    binding.textName.text = result.data.data.fullname
                    Glide.with(binding.profilePic.context)
                        .load(result.data.data.profilePhoto)
                        .error(R.drawable.person_pc)
                        .into(binding.profilePic)
                }
                is Result.Error -> {
                    binding.textName.visibility = View.GONE
                }
            }
        }
    }

    private fun setupAction() {
        binding.cardAnalyze.setOnClickListener{
            navigateToAnalyze()
        }
        binding.cardPlantMenu.setOnClickListener {
            navigateToPlant()
        }
        binding.cardDiseaseMenu.setOnClickListener{
            navigateToDisease()
        }
        binding.cardDiscussion.setOnClickListener {
            navigateToDiscussion()
        }
    }

    private fun navigateToDiscussion() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.navigation_home, inclusive = false, saveState = true)
            .setLaunchSingleTop(true)
            .build()
        findNavController().navigate(R.id.navigation_discussion, null, navOptions)
    }

    private fun setupSlider() {
        val sliderDataList = mutableListOf<SliderData>()

        val banners = resources.obtainTypedArray(R.array.banners)
        for (i in 0 until banners.length()) {
            val resId = banners.getResourceId(i, 0)
            sliderDataList.add(SliderData(resId))
        }
        banners.recycle()

        val sliderAdapter = SliderAdapter(sliderDataList)

        binding.imageSlider.apply {
            setIndicatorAnimation(IndicatorAnimationType.WORM)
            setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
            autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
            setSliderAdapter(sliderAdapter)
            scrollTimeInSec = 10
            isAutoCycle = true
            startAutoCycle()
        }
    }


    private fun navigateToDisease() {
        val intent = Intent(requireContext(), DiseasesActivity::class.java)
        startActivity(intent)
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
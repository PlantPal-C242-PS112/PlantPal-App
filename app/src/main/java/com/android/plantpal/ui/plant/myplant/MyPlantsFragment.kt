package com.android.plantpal.ui.plant.myplant

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.plantpal.databinding.FragmentMyPlantsBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.home.plants.DetailPlantActivity
import com.android.plantpal.ui.utils.Result
import com.android.plantpal.ui.utils.dialog.LoadingDialog

class MyPlantsFragment : Fragment() {

    private var _binding: FragmentMyPlantsBinding? = null
    private val binding get() = _binding!!
    private lateinit var myPlantsViewModel: MyPlantsViewModel
    private lateinit var plantAdapter: PlantAdapter
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPlantsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadingDialog = LoadingDialog(requireActivity())

        val factory = ViewModelFactory.getInstance(requireContext())
        myPlantsViewModel = ViewModelProvider(this, factory)[MyPlantsViewModel::class.java]

        binding.rvMyPlants.layoutManager = LinearLayoutManager(context)
        val plantAdapter = PlantAdapter { userPlant ->
            val intent = Intent(requireContext(), DetailPlantActivity::class.java)
            intent.putExtra(DetailPlantActivity.KEY_PLANT_ID, userPlant.plantId)
            startActivity(intent)
        }

        binding.rvMyPlants.adapter = plantAdapter

        myPlantsViewModel.getUserPlants().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    plantAdapter.updateData(result.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

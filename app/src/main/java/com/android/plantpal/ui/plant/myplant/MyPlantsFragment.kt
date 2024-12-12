package com.android.plantpal.ui.plant.myplant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.plantpal.databinding.FragmentMyPlantsBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.home.plants.DetailPlantActivity
import com.android.plantpal.ui.home.plants.PlantActivity
import com.android.plantpal.ui.plant.MyPlantsViewModel
import com.android.plantpal.ui.plant.myplantdetail.DetailMyPlantsActivity
import com.android.plantpal.ui.plant.reminder.SetAlarmActivity
import com.android.plantpal.ui.utils.dialog.LoadingDialog
import com.android.plantpal.ui.utils.Result

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
    ): View? {
        _binding = FragmentMyPlantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog = LoadingDialog(requireActivity())

        val factory = ViewModelFactory.getInstance(requireContext())
        myPlantsViewModel = ViewModelProvider(this, factory).get(MyPlantsViewModel::class.java)

        val plantAdapter = PlantAdapter { userPlant ->
            val intent = Intent(requireContext(), DetailPlantActivity::class.java)
            intent.putExtra(DetailPlantActivity.KEY_PLANT_ID, userPlant.plantId)
            startActivity(intent)

            val alarmIntent = Intent(requireContext(), SetAlarmActivity::class.java)
            alarmIntent.putExtra("PLANT_ID", userPlant.plantId)
            Log.d("MyPlantsFragment", "Sending PLANT_ID: $userPlant")
            startActivity(alarmIntent)
        }

        binding.rvMyPlants.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMyPlants.adapter = plantAdapter

        loadingDialog = LoadingDialog(requireActivity())

        binding.fabAddPlant.setOnClickListener {
            val intent = Intent(requireContext(), PlantActivity::class.java)
            startActivity(intent)
        }


        myPlantsViewModel.getUserPlants().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("MyPlantsFragment", "Data received: ${result.data}")
                    plantAdapter.updateData(result.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

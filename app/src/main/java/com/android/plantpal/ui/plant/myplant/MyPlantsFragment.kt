package com.android.plantpal.ui.plant.myplant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.plantpal.R
import com.android.plantpal.databinding.FragmentMyPlantsBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.plant.MyPlantsViewModel
import com.android.plantpal.ui.utils.dialog.FailedDialog
import com.android.plantpal.ui.utils.dialog.LoadingDialog
import com.android.plantpal.ui.utils.Result

class MyPlantsFragment : Fragment() {

    private var _binding: FragmentMyPlantsBinding? = null
    private val binding get() = _binding!!
    private lateinit var myPlantsViewModel: MyPlantsViewModel
    private lateinit var plantAdapter: PlantAdapter
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPlantsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadingDialog = LoadingDialog(requireActivity())

        val factory = ViewModelFactory.getInstance(requireContext())
        myPlantsViewModel = ViewModelProvider(this, factory).get(MyPlantsViewModel::class.java)

        binding.rvMyPlants.layoutManager = LinearLayoutManager(context)
        plantAdapter = PlantAdapter(emptyList()) { userPlant ->

        }
        binding.rvMyPlants.adapter = plantAdapter

        // Observe the LiveData for plants
        myPlantsViewModel.getUserPlants("Bearer YourTokenHere").observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    loadingDialog.startLoadingDialog()
                }
                is Result.Success -> {
                    loadingDialog.dismissDialog()
                    plantAdapter.updateData(result.data) // Assuming updateData() exists in PlantAdapter
                }
                is Result.Error -> {
                    loadingDialog.dismissDialog()
                    val failedDialog = FailedDialog(requireContext())
                    failedDialog.startFailedDialog(getString(R.string.data_failed))
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

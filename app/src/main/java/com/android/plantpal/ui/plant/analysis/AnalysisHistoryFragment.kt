package com.android.plantpal.ui.plant.analysis

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.plantpal.R
import com.android.plantpal.databinding.FragmentAnalysisHistoryBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.home.analyze.DetailHistoryActivity
import com.android.plantpal.ui.home.analyze.ResultActivity
import com.android.plantpal.ui.utils.dialog.LoadingDialog
import com.android.plantpal.ui.utils.Result

class AnalysisHistoryFragment : Fragment() {

    private var _binding: FragmentAnalysisHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var analysisHistoryViewModel: AnalysisHistoryViewModel
    private lateinit var analysisAdapter: AnalysisHistoryAdapter
    private lateinit var loadingDialog: LoadingDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalysisHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        analysisHistoryViewModel = ViewModelProvider(this, factory)[AnalysisHistoryViewModel::class.java]

        val analysisAdapter = AnalysisHistoryAdapter { diagnosisItem ->
            if(diagnosisItem.result.message.startsWith("We are not sure", ignoreCase = true)){
                Toast.makeText(requireContext(), "Detail history analisis tidak tersedia", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(requireContext(), DetailHistoryActivity::class.java)
                intent.putExtra("data", diagnosisItem)
                startActivity(intent)
            }
        }

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = analysisAdapter

        loadingDialog = LoadingDialog(requireActivity())

        analysisHistoryViewModel.getDiagnosisHistory().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    analysisAdapter.submitList(result.data)
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

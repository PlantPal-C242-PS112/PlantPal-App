package com.android.plantpal.ui.plant.analysis

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.plantpal.data.remote.response.DiagnosisItem
import com.android.plantpal.databinding.FragmentAnalysisHistoryBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.utils.Result
import com.android.plantpal.ui.utils.dialog.LoadingDialog

class AnalysisHistoryFragment : Fragment() {

    private var _binding: FragmentAnalysisHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var analysisHistoryViewModel: AnalysisHistoryViewModel
    private lateinit var analysisAdapter: AnalysisHistoryAdapter
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalysisHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        analysisHistoryViewModel = ViewModelProvider(this, factory)[AnalysisHistoryViewModel::class.java]

        analysisAdapter = AnalysisHistoryAdapter { diagnosisItem ->
            handleItemClick(diagnosisItem)
        }

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = analysisAdapter

        loadingDialog = LoadingDialog(requireActivity())

        observeHistory()

    }

    private fun deleteAll() {
        analysisHistoryViewModel.deleteAllHistory().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    observeHistory()
                }
                is Result.Error -> {
                    showLoading(false)
                }
            }
        })
    }

    private fun observeHistory() {
        analysisHistoryViewModel.getDiagnosisHistory().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    if (result.data.isNotEmpty()) {
                        binding.noHistory.visibility = View.GONE
                        binding.buttonDeleteAll.visibility = View.VISIBLE
                        binding.buttonDeleteAll.setOnClickListener {
                            showAlertDialog(
                                title = "Apakah Anda Yakin?",
                                message = "Apakah Anda Ingin Menghapus Semua Riwayat Diagnosa?",
                                positiveButtonText = "Ya",
                                negativeButtonText = "Batal",
                                onPositive = { deleteAll() }
                            )
                        }
                    } else {
                        binding.noHistory.visibility = View.VISIBLE
                        binding.buttonDeleteAll.visibility = View.GONE
                    }
                    analysisAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    showLoading(false)
                    binding.buttonDeleteAll.visibility = View.GONE
                    Toast.makeText(requireContext(), "Terjadi kesalahan: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }



    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun handleItemClick(diagnosisItem: DiagnosisItem) {
        if (diagnosisItem.result.message.startsWith("We are not sure", ignoreCase = true)) {
            Toast.makeText(requireContext(), "Detail history analisis tidak tersedia", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(requireContext(), DetailHistoryActivity::class.java)
            intent.putExtra("data", diagnosisItem)
            startActivity(intent)
        }
    }

    private fun showAlertDialog(
        title: String,
        message: String?,
        positiveButtonText: String,
        negativeButtonText: String,
        onPositive: (() -> Unit)? = null,
        onNegative: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(requireContext()).apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        observeHistory()
    }
}


package com.android.plantpal.ui.plant.analysis

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.plantpal.R
import com.android.plantpal.data.remote.response.DiagnosisItem
import com.android.plantpal.databinding.ActivityDetailHistoryBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.home.disease.DetailDiseaseActivity
import com.android.plantpal.ui.utils.Result
import com.android.plantpal.ui.utils.formatToLocalDateTime
import com.android.plantpal.ui.utils.showAlertDialog
import com.bumptech.glide.Glide

@Suppress("DEPRECATION")
class DetailHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHistoryBinding
    private lateinit var analysisHistoryViewModel: AnalysisHistoryViewModel
    private var idDiagnosis: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        analysisHistoryViewModel = ViewModelProvider(this, factory)[AnalysisHistoryViewModel::class.java]

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val data = intent.getParcelableExtra<DiagnosisItem>("data")

        val formattedDate = data?.createdAt?.let { formatToLocalDateTime(it) }

        Glide.with(binding.ivPhoto)
            .load(data?.result?.imageUrl)
            .placeholder(R.drawable.ic_image)
            .into(binding.ivPhoto)

        supportActionBar?.title = formattedDate

        binding.textResult1.text = data?.result?.className
            ?.split("__")
            ?.getOrNull(1)
            .orEmpty()

        binding.plantType1Text.text = data?.result?.className
            ?.split("__")
            ?.getOrNull(0)
            .orEmpty()

        binding.textConfidenceScore.text = data?.result?.confidenceScore.toString()

        binding.iconNext.setOnClickListener {
            sendSelectedDisease(data?.result?.diseaseId)
        }

        idDiagnosis = data?.id.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.delete_menu, menu)
        return true
    }

    private fun sendSelectedDisease(id: Int?) {
        val intent = Intent(this, DetailDiseaseActivity::class.java)
        intent.putExtra(DetailDiseaseActivity.KEY_DISEASE_ID, id)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.delete -> {
                showAlertDialog(
                    this,
                    title = "Apakah Anda Yakin?",
                    message = "Apakah Anda Ingin Menghapus Diagnosa Ini?",
                    positiveButtonText = "Ya",
                    negativeButtonText = "Batal",
                    onPositive = { deleteDiagnosis(idDiagnosis) }
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteDiagnosis(id: String) {
        analysisHistoryViewModel.deleteHistory(id).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    finish()
                }
                is Result.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
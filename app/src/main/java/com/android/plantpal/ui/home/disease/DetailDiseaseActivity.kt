package com.android.plantpal.ui.home.disease

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.plantpal.R
import com.android.plantpal.data.remote.response.DetailDiseaseData
import com.android.plantpal.data.remote.response.DiseaseMediaItem
import com.android.plantpal.data.remote.response.MedicineLinksItem
import com.android.plantpal.data.remote.response.MedicinesItem
import com.android.plantpal.databinding.ActivityDetailDiscussionBinding
import com.android.plantpal.databinding.ActivityDetailDiseaseBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.discussion.DiscussionViewModel
import com.android.plantpal.ui.discussion.detail.DetailDiscussionActivity
import com.android.plantpal.ui.utils.Result

@Suppress("DEPRECATION")
class DetailDiseaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDiseaseBinding
    private lateinit var viewModel: DiseaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDiseaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory)[DiseaseViewModel::class.java]

        val disId = intent.getIntExtra(DetailDiseaseActivity.KEY_DISEASE_ID, -1)

        observeDiseaseDetails(disId)
    }

    private fun observeDiseaseDetails(disId: Int) {
        viewModel.getDetailDisease(disId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    val disease = result.data
                    setDiseaseDetail(disease)
                }
                is Result.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setDiseaseDetail(disease: DetailDiseaseData) {
        supportActionBar?.title = "Detail Penyakit ${disease.name}"

        setDiseasePicture(disease.diseaseMedia)

        val allMedicineLinks = disease.medicines.flatMap { it.medicineLinks }
        setMedicines(allMedicineLinks)

        binding.diseaseName.text = disease.name
        binding.diseaseDescText.text = HtmlCompat.fromHtml(
            disease.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.diseasePreventionText.text = HtmlCompat.fromHtml(
            disease.prevention,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        binding.diseaseTypeText.text = HtmlCompat.fromHtml(
            disease.plant.name,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        binding.diseaseTreatmentText.text = HtmlCompat.fromHtml(
            disease.treatment,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        binding.diseaseCureText.text = HtmlCompat.fromHtml(
            disease.medicines.toString(),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        val medicinesText = disease.medicines.joinToString(separator = "\n") { medicine ->
            medicine.description
        }

        if (medicinesText.isEmpty()) {
            binding.diseaseCureLayout.visibility = View.GONE
        }

        binding.diseaseCureText.text = HtmlCompat.fromHtml(
            medicinesText,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

    }

    private fun setMedicines(allMedicineLinks: List<MedicineLinksItem>) {
        binding.rvCureDiseases.layoutManager = LinearLayoutManager(this)
        val adapter = MedicineAdapter(allMedicineLinks)
        binding.rvCureDiseases.adapter = adapter

        adapter.setOnItemClickCallback(object : MedicineAdapter.OnItemClickCallback {
            override fun onItemClicked(data: MedicineLinksItem) {
                val medicineLink = data.url
                if (medicineLink.isNotEmpty()) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(medicineLink))
                    startActivity(browserIntent)
                } else {
                    Toast.makeText(this@DetailDiseaseActivity, "Link is not accessible", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setDiseasePicture(diseaseMedia: List<DiseaseMediaItem>) {
        binding.rvPhotoDiseases.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = DiseasePhotoAdapter(diseaseMedia)
        binding.rvPhotoDiseases.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    companion object {
        const val KEY_DISEASE_ID = "key_disease_id"
    }
}
package com.android.plantpal.ui.home.analyze

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.android.plantpal.R
import com.android.plantpal.data.remote.response.DiagnosisItem
import com.android.plantpal.databinding.ActivityDetailHistoryBinding
import com.android.plantpal.ui.home.disease.DetailDiseaseActivity
import com.android.plantpal.ui.utils.formatToLocalDateTime
import com.bumptech.glide.Glide

@Suppress("DEPRECATION")
class DetailHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            else -> super.onOptionsItemSelected(item)
        }
    }
}
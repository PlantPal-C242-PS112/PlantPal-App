package com.android.plantpal.ui.home.analyze

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.plantpal.data.remote.response.DiagnosisResponse
import com.android.plantpal.databinding.ActivityResultBinding
import com.android.plantpal.ui.home.disease.DetailDiseaseActivity

@Suppress("DEPRECATION")
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val imageUriString = intent.getStringExtra("image_uri")
        val data = intent.getParcelableExtra<DiagnosisResponse>("data")

        imageUriString?.let {
            val imageUri = Uri.parse(it)
            binding.ivPhoto.setImageURI(imageUri)
        }

        supportActionBar?.title = "Detail Diagnosa Penyakit"

        binding.textResult1.text = data?.data?.diseaseData?.name
        binding.plantType1Text.text = data?.data?.diseaseData?.plant?.name
        binding.textResult2.text = data?.data?.top3?.getOrNull(1)?.className
            ?.split("__")
            ?.getOrNull(1)
            .orEmpty()

        binding.plantType2Text.text = data?.data?.top3?.getOrNull(1)?.className
            ?.split("__")
            ?.getOrNull(0)
            .orEmpty()

        binding.textConfidenceScore.text = data?.data?.top3?.first()?.confidenceScore.toString()
        binding.textConfidenceScore2.text = data?.data?.top3?.getOrNull(1)?.confidenceScore.toString()

        binding.iconNext.setOnClickListener {
            sendSelectedDisease(data?.data?.top3?.first()?.diseaseId)
        }

        val id = data?.data?.top3?.getOrNull(1)?.diseaseId
        Log.d("IDDis2", "$id")
        if (id != 0){
            binding.iconNext2.setOnClickListener {
                sendSelectedDisease(id)
            }
        }else{
            binding.iconNext2.visibility = View.GONE
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
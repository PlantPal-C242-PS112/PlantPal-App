package com.android.plantpal.ui.home.disease

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.android.plantpal.data.remote.response.ListItemDisease
import com.android.plantpal.databinding.ActivityDiseasesBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.utils.Result

@Suppress("DEPRECATION")
class DiseasesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiseasesBinding
    private lateinit var viewModel: DiseaseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiseasesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory)[DiseaseViewModel::class.java]
        observeDisease()
    }

    private fun observeDisease() {
        viewModel.getAllDisease().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val disease = result.data
                    setDisease(disease)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setDisease(disease: List<ListItemDisease>) {
        supportActionBar?.title = "Ensiklopedia Penyakit"
        binding.rvDisease.layoutManager = GridLayoutManager(this, 2)
        val adapter = DiseaseAdapter(disease)
        binding.rvDisease.adapter = adapter

        adapter.setOnItemClickCallback(object : DiseaseAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListItemDisease) {
                sendSelectedDisease(data)
            }
        })
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

    private fun sendSelectedDisease(data: ListItemDisease) {
        val intent = Intent(this, DetailDiseaseActivity::class.java)
        intent.putExtra(DetailDiseaseActivity.KEY_DISEASE_ID, data.id)
        startActivity(intent)
    }
}
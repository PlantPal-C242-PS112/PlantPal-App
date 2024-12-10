package com.android.plantpal.ui.home.plants

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.android.plantpal.data.remote.response.ListItemPlant
import com.android.plantpal.databinding.ActivityPlantBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.utils.Result

@Suppress("DEPRECATION")
class PlantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantBinding
    private lateinit var viewModel: PlantsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory)[PlantsViewModel::class.java]

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        observePlant()
    }

    private fun observePlant() {
        viewModel.getAllPlants().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val plant = result.data
                    setPlants(plant)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setPlants(plant: List<ListItemPlant>) {
        binding.rvEvents.layoutManager = GridLayoutManager(this, 2)
        val adapter = PlantsAdapter(plant)
        binding.rvEvents.adapter = adapter

        adapter.setOnItemClickCallback(object : PlantsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListItemPlant) {
                sendSelectedDPlant(data)
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

    private fun sendSelectedDPlant(data: ListItemPlant) {
        val intent = Intent(this, DetailPlantActivity::class.java)
        intent.putExtra(DetailPlantActivity.KEY_PLANT_ID, data.id)
        startActivity(intent)
    }
}

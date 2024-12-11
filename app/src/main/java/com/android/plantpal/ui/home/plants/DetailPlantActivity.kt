package com.android.plantpal.ui.home.plants

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.plantpal.R
import com.android.plantpal.data.remote.response.DetailPlantData
import com.android.plantpal.data.remote.response.ListPlantDisease
import com.android.plantpal.data.remote.response.PlantMedia
import com.android.plantpal.databinding.ActivityDetailPlantBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.home.disease.DetailDiseaseActivity
import com.android.plantpal.ui.utils.CultivationTip
import com.android.plantpal.ui.utils.Result
import com.android.plantpal.ui.utils.cleanHtml
import com.android.plantpal.ui.utils.extract
import com.bumptech.glide.Glide

@Suppress("DEPRECATION")

class DetailPlantActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPlantBinding
    private lateinit var viewModel: PlantsViewModel

    private var isFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory)[PlantsViewModel::class.java]

        val plantId = intent.getIntExtra(KEY_PLANT_ID, -1)

        if (plantId != -1) {
            viewModel.setPlantId(plantId)
        } else {
            Log.e("DetailPlantActivity", "Invalid plantId received!")
            finish()
            return
        }

        observePlantDetails(plantId)
        observeCultivationTips(plantId)
        observeUserPlant(plantId)
        observePlantsDisease(plantId)
    }

    private fun observePlantsDisease(plantId: Int) {
        viewModel.getPlantDisease(plantId).observe(this) { addResult ->
            when (addResult) {
                is Result.Loading -> {
                }

                is Result.Success -> {
                    setPlantDiseases(addResult.data)
                }

                is Result.Error -> {
                    Log.e("DetailPlantActivity", "Error adding plant: ${addResult.error}")
                }

                else -> {}
            }
        }
    }

    private fun setPlantDiseases(data: List<ListPlantDisease>) {
        binding.rvDiseases.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = PlantDiseaseAdapter(data)
        binding.rvDiseases.adapter = adapter

        adapter.setOnItemClickCallback(object : PlantDiseaseAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListPlantDisease) {
                sendSelectedDisease(data)
            }
        })
    }

    private fun sendSelectedDisease(data: ListPlantDisease) {
        val intent = Intent(this, DetailDiseaseActivity::class.java)
        intent.putExtra(DetailDiseaseActivity.KEY_DISEASE_ID, data.id)
        startActivity(intent)
    }

    private fun observeUserPlant(plantId: Int) {
        viewModel.getUserPlant().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                }

                is Result.Success -> {
                    val userPlantsIds = result.data.map { it.plantId }

                    isFavorite = userPlantsIds.contains(plantId)
                    updateFavoriteIcon(isFavorite)

                    binding.btnFavorite.setOnClickListener {
                        if (isFavorite) {
                            deletePlant(plantId)
                            Log.e("DeleteUserPlant", "Clicked")

                        } else {
                            addPlant(plantId)
                        }
                    }
                }

                is Result.Error -> {
                    Log.e("DetailPlantActivity", "Error fetching user plants: ${result.error}")
                }

                else -> {}
            }
        }
    }

    private fun addPlant(plantId: Int) {
        viewModel.addPlant(plantId).observe(this) { addResult ->
            when (addResult) {
                is Result.Loading -> {
                }

                is Result.Success -> {
                    isFavorite = true
                    updateFavoriteIcon(isFavorite)
                }

                is Result.Error -> {
                    Log.e("DetailPlantActivity", "Error adding plant: ${addResult.error}")
                }

                else -> {}
            }
        }
    }

    private fun deletePlant(plantId: Int) {
        viewModel.deletePlant(plantId).observe(this) { deleteResult ->
            when (deleteResult) {
                is Result.Loading -> {
                }

                is Result.Success -> {
                    isFavorite = false
                    updateFavoriteIcon(isFavorite)
                }

                is Result.Error -> {
                    Log.e("DetailPlantActivity", "Error deleting plant: ${deleteResult.error}")
                }

                else -> {}
            }
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.btnFavorite.setIconResource(R.drawable.ic_favorite_filled)
        } else {
            binding.btnFavorite.setIconResource(R.drawable.ic_favorite)
        }
    }


    private fun observePlantDetails(plantId: Int) {
        viewModel.getDetailPlants(plantId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    val plant = result.data
                    setPlantDetail(plant)
                }
                is Result.Error -> {
                }

                else -> {}
            }
        }
    }

    private fun observeCultivationTips(plantId: Int) {
        viewModel.getCultivationTips(plantId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    val cultivationData = cleanHtml(result.data.cultivationTips)
                    val extractedData = extract(cultivationData)
                    setDataCultivation(extractedData)
                }
                is Result.Error -> {
                }

                else -> {}
            }
        }
    }

    private fun setDataCultivation(hello: CultivationTip) {
        val data = hello.sections

        binding.rvCult.layoutManager = LinearLayoutManager(this)

        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.rvCult.addItemDecoration(dividerItemDecoration)

        val adapter = CultTipsAdapter(data)
        binding.rvCult.adapter = adapter

    }
    private fun setPlantDetail(plant: DetailPlantData) {
        Log.d("DetailPlantActivity", "Setting plant detail for: ${plant.name}")
        supportActionBar?.title = "Detail Tanaman ${plant.name}"

        setPlantPicture(plant.plantMedia)

        binding.tvPlantName.text = plant.name
        Log.d("DetailPlantActivity", "Description: ${plant.description}")
        binding.tvDescription.text = HtmlCompat.fromHtml(
            plant.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )


    }

    private fun setPlantPicture(plantMedia: List<PlantMedia>) {
        binding.imgPlant.setImageResource(R.drawable.ic_image)
        if (plantMedia.isNotEmpty()) {
            Glide.with(this)
                .load(plantMedia[0].url)
                .into(binding.imgPlant)
        }
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
        const val KEY_PLANT_ID = "key_plant_id"
    }
}

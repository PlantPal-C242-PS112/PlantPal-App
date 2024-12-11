package com.android.plantpal.ui.home.plants

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.plantpal.R
import com.android.plantpal.data.remote.response.CultivationData
import com.android.plantpal.data.remote.response.DetailPlantData
import com.android.plantpal.data.remote.response.PlantMedia
import com.android.plantpal.databinding.ActivityDetailPlantBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.utils.Result
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


    }


//    private fun addPlantToFavorites(plantId: Int) {
//        viewModel.getDetailPlants(plantId).observe(this) { result ->
//            when (result) {
//                is Result.Loading -> {
//
//                }
//
//                is Result.Success -> {
//                    observeUserPlant(plantId)
//                }
//
//                is Result.Error -> {
//
//                }
//            }
//        }
//    }

    private fun observeUserPlant(plantId: Int) {
        viewModel.getUserPlant().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Optionally show a loading indicator
                }

                is Result.Success -> {
                    val userPlantsIds = result.data.map { it.plantId }

                    // Update the icon based on the plant's presence in userPlants
                    isFavorite = userPlantsIds.contains(plantId)
                    updateFavoriteIcon(isFavorite)

                    // Set click listener for the favorite button
                    binding.btnFavorite.setOnClickListener {
                        if (isFavorite) {
                            deletePlant(plantId)
                        } else {
                            addPlant(plantId)
                        }
                    }
                }

                is Result.Error -> {
                    // Handle error state, e.g., show a toast or log the error
                    Log.e("DetailPlantActivity", "Error fetching user plants: ${result.error}")
                }
            }
        }
    }

    private fun addPlant(plantId: Int) {
        viewModel.addPlant(plantId).observe(this) { addResult ->
            when (addResult) {
                is Result.Loading -> {
                    // Optionally show a loading indicator
                }

                is Result.Success -> {
                    // Update the icon to indicate the plant was added
                    isFavorite = true
                    updateFavoriteIcon(isFavorite)
                }

                is Result.Error -> {
                    // Handle error, e.g., show a toast or log the error
                    Log.e("DetailPlantActivity", "Error adding plant: ${addResult.error}")
                }
            }
        }
    }

    private fun deletePlant(plantId: Int) {
        viewModel.deletePlant(plantId).observe(this) { deleteResult ->
            when (deleteResult) {
                is Result.Loading -> {
                    // Optionally show a loading indicator
                }

                is Result.Success -> {
                    // Update the icon to indicate the plant was removed
                    isFavorite = false
                    updateFavoriteIcon(isFavorite)
                }

                is Result.Error -> {
                    // Handle error, e.g., show a toast or log the error
                    Log.e("DetailPlantActivity", "Error deleting plant: ${deleteResult.error}")
                }
            }
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.btnFavorite.setImageResource(R.drawable.ic_favorite_filled)
        } else {
            binding.btnFavorite.setImageResource(R.drawable.ic_favorite)
        }
    }


    private fun observePlantDetails(plantId: Int) {
        viewModel.getDetailPlants(plantId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    val plant = result.data
                    Log.d("DetailPlantActivity", "Plant Description: ${plant.description}")
                    setPlantDetail(plant)
                }
                is Result.Error -> {
                    Log.e("DetailPlantActivity", "Error loading plant details: ${result.error}")


                }
            }
        }
    }

    private fun observeCultivationTips(plantId: Int) {
        viewModel.getCultivationTips(plantId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {

                }
                is Result.Success -> {
                    val cultivationData = result.data
                    Log.d("DetailPlantActivity", "Cultivation Tips: ${cultivationData.cultivationTips}")
                    setCultivationTips(cultivationData)
                }
                is Result.Error -> {
                }
            }
        }
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

    private fun setCultivationTips(cultivationData: CultivationData) {
        supportActionBar?.title = "Kiat Budidaya ${cultivationData.name}"

        binding.tvDescriptionCultivation.text = HtmlCompat.fromHtml(
            cultivationData.cultivationTips,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        val plantMediaAdapter = PlantMediaAdapter(cultivationData.plantMedia)
        binding.rvCultivationTips.apply {
            layoutManager = LinearLayoutManager(this@DetailPlantActivity)
            adapter = plantMediaAdapter
        }
        val cultivationTipsAdapter = CultivationTipsAdapter(listOf(cultivationData.cultivationTips)) // Use list for display
        binding.rvCultivationTips.apply {
            layoutManager = LinearLayoutManager(this@DetailPlantActivity)
            adapter = cultivationTipsAdapter
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

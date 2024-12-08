package com.android.plantpal.ui.home.plants

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.plantpal.R
import com.android.plantpal.data.remote.response.CultivationData
import com.android.plantpal.data.remote.response.CultivationPlantMedia
import com.android.plantpal.data.remote.response.DetailPlantData
import com.android.plantpal.data.remote.response.PlantMedia
import com.android.plantpal.data.remote.response.UserPlant
import com.android.plantpal.data.remote.response.UserPlantDetail
import com.android.plantpal.databinding.ActivityDetailPlantBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.plant.MyPlantsViewModel
import com.android.plantpal.ui.plant.PlantViewModel
import com.bumptech.glide.Glide
import com.android.plantpal.ui.utils.Result

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

        binding.btnFavorite.setOnClickListener {
            addPlantToFavorites(plantId)
        }


    }


    private fun addPlantToFavorites(plantId: Int) {
        viewModel.getDetailPlants(plantId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {

                }

                is Result.Success -> {
                    if (isFavorite) {
                        viewModel.deletePlant(plantId).observe(this) { deleteResult ->
                            when (deleteResult) {
                                is Result.Loading -> {

                                }
                                is Result.Success -> {
                                    binding.btnFavorite.setImageResource(R.drawable.ic_favorite)
                                    isFavorite = false
                                }
                                is Result.Error -> {

                                }
                            }
                        }
                    } else {
                        viewModel.addPlant(plantId).observe(this) { addResult ->
                            when (addResult) {
                                is Result.Loading -> {
                                }

                                is Result.Success -> {
                                    binding.btnFavorite.setImageResource(R.drawable.ic_favorite_filled)
                                    isFavorite = true
                                }

                                is Result.Error -> {

                                }
                            }
                        }
                    }
                    isFavorite = !isFavorite
                }

                is Result.Error -> {

                }
            }
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

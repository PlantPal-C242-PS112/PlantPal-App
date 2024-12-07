package com.android.plantpal.ui.home.plants

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.plantpal.R

class DetailPlantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_plant)
    }

    companion object {
        const val KEY_PLANT_ID = "key_plant_id"
    }
}
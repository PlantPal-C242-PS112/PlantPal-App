@file:Suppress("DEPRECATION")

package com.android.plantpal.ui.home.analyze

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.plantpal.data.remote.response.DiagnosisResponse
import com.android.plantpal.databinding.ActivityAnalyzeBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.discussion.add.AddDiscussionActivity
import com.android.plantpal.ui.utils.Result
import com.android.plantpal.ui.utils.dialog.FailedDialog
import com.android.plantpal.ui.utils.dialog.LoadingDialog
import com.android.plantpal.ui.utils.dialog.SuccessDialog
import com.android.plantpal.ui.utils.getImageUri
import com.android.plantpal.ui.utils.reduceFileImage
import com.android.plantpal.ui.utils.showAlertDialog
import com.android.plantpal.ui.utils.uriToFile
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class AnalyzeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyzeBinding
    private lateinit var viewModel: AnalyzeViewModel
    private val loadingDialog = LoadingDialog(this)
    private val failedDialog = FailedDialog(this)
    private val successDialog = SuccessDialog(this)

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Analisis Tanaman"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory)[AnalyzeViewModel::class.java]

        viewModel.currentImageUri?.let {
            showImage()
        }

        binding.cardGallery.setOnClickListener { startGallery() }
        binding.cardCamera.setOnClickListener { startCamera() }
        binding.buttonAnalyze.setOnClickListener {
            viewModel.currentImageUri?.let {
                analyze(it)
            } ?: run {
                showToast("Tolong pilih gambar terlebih dahulu")
            }
        }
    }

    private fun analyze(it: Uri) {
        val image = uriToFile(it, this@AnalyzeActivity).reduceFileImage()
        viewModel.getDiagnose(image).observe(this){result ->
            when (result) {
                is Result.Loading -> {
                    loadingDialog.startLoadingDialog()
                }
                is Result.Success -> {
                    loadingDialog.dismissDialog()
                    val diagnose = result.data
                    if (diagnose.message.startsWith("We are not sure", ignoreCase = true)) {
                        showAlertDialog(
                            this,
                            title = "Penyakit tidak dikenal terdeteksi",
                            message = "Mohon maaf, PlantPal belum dapat mendeteksi penyakit ini",
                            positiveButtonText = "Coba foto lain",
                            negativeButtonText = "Tanyakan forum diskusi",
                            onPositive = { startCamera() },
                            onNegative = { navigateToDiscussion() }
                        )
                    } else if (diagnose.message.endsWith("No disease detected.", ignoreCase = true)){
                        showAlertDialog(
                            this,
                            title = "Tidak ada penyakit terdeteksi",
                            message = "PlantPal tidak mendeteksi penyakit, tanaman anda sehat!",
                            positiveButtonText = "Coba foto lain",
                            negativeButtonText = "Oke",
                            onPositive = { startCamera() },
                            onNegative = { }
                        )
                    } else{
                        successDialog.startSuccessDialog("Berhasil Mendeteksi Penyakit!")
                        navigateToResult(result.data, it)
                    }
                }
                is Result.Error -> {
                    loadingDialog.dismissDialog()
                    failedDialog.startFailedDialog("Gagal Mendapatkan Analisis!")
                    Log.e("Upload", "Error processing profile update: ${result.error}")
                }

                else -> {}
            }
        }
    }

    private fun navigateToDiscussion() {
        val intent = Intent(this, AddDiscussionActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToResult(data: DiagnosisResponse, it: Uri) {
        lifecycleScope.launch {
            delay(2000)
            successDialog.dismissDialog()

            val intent = Intent(this@AnalyzeActivity, ResultActivity::class.java).apply {
                putExtra("image_uri", it.toString())
                putExtra("data", data)
            }
            startActivity(intent)
        }
    }

    private fun startCamera() {
        viewModel.currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(viewModel.currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSaved: Boolean ->
        if (isSaved) {
            viewModel.currentImageUri?.let { uri ->
                startCrop(uri)
                showImage()
            }
        } else {
            Log.d("Photo Picker", "Photo was not saved")
        }
    }


    private fun startCrop(uri: Uri) {
        val croppedImg = createImageFile()
        if (croppedImg != null) {
            UCrop.of(uri, Uri.fromFile(croppedImg))
                .withAspectRatio(1f, 1f)
                .start(this)
        } else {
            Toast.makeText(this, "Failed to create image file", Toast.LENGTH_LONG).show()
        }
    }

    private fun createImageFile(): File? {
        val imageFileName = "${System.currentTimeMillis()}.jpg"
        return try {
            File(cacheDir, imageFileName).apply {
                createNewFile()
            }
        } catch (e: IOException) {
            Log.e("ImageError", "Error creating image file", e)
            null
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val croppedImg = data?.let { UCrop.getOutput(it) }
            viewModel.currentImageUri = croppedImg
            showImage()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val error = data?.let { UCrop.getError(it) }
            error?.let {
                showToast("Error: ${it.message}")
            }
        }
    }

    private fun showImage() {
        viewModel.currentImageUri?.let {
            binding.ivPhoto.setImageURI(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.currentImageUri = uri
            startCrop(uri)
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
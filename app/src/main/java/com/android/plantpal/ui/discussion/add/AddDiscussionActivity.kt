package com.android.plantpal.ui.discussion.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.plantpal.databinding.ActivityAddDiscussionBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.utils.Result
import com.android.plantpal.ui.utils.dialog.FailedDialog
import com.android.plantpal.ui.utils.dialog.LoadingDialog
import com.android.plantpal.ui.utils.dialog.SuccessDialog
import com.android.plantpal.ui.utils.reduceFileImage
import com.android.plantpal.ui.utils.uriToFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@Suppress("DEPRECATION")
class AddDiscussionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddDiscussionBinding
    private lateinit var viewModel: AddDiscussionViewModel
    private val successDialog = SuccessDialog(this)
    private val loadingDialog = LoadingDialog(this)
    private val failedDialog = FailedDialog(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDiscussionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        supportActionBar?.title = "Tambah Diskusi"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory)[AddDiscussionViewModel::class.java]

        viewModel.currentImageUri?.let {
            showImage()
        }
        setupAction()
    }

    private fun setupAction() {
        setupSpinner()

        binding.addPhoto.setOnClickListener{
            startGallery()
        }

        binding.buttonAdd.setOnClickListener{
            uploadStory()
            Log.d("AddDiscussionActivity", "Button clicked")
        }

        binding.delPic.setOnClickListener{
            viewModel.currentImageUri = null
            binding.addPhotoBulk.visibility = View.VISIBLE
            binding.picPlaceholder.visibility = View.GONE

        }
    }

    private fun setupSpinner() {
        viewModel.getAllPlants().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    val spinner = binding.spinnerCrops
                    val plants = result.data.map { it.name }.toTypedArray()

                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, plants)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter

                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            viewModel.selectedPlant = position + 1
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }
                }
                is Result.Error -> {
                    Toast.makeText(this, "Tidak dapat mengambil data jenis tanaman", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showImage() {
        viewModel.currentImageUri?.let {
            binding.addPhotoBulk.visibility = View.GONE
            binding.picPlaceholder.visibility = View.VISIBLE
            binding.ivItemPhoto.setImageURI(it)
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
            showImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun uploadStory() {
        Log.d("AddDiscussionActivity", "up clicked")
        val title = binding.edAddQuestion.getText()
        val desc = binding.edAddDesc.getText()

        if (viewModel.currentImageUri == null) {
            Toast.makeText(this, "Tolong pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        if (viewModel.selectedPlant == 0) {
            Toast.makeText(this, "Pilih salah satu tanaman terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        if (desc.isEmpty()) {
            Toast.makeText(this, "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        if (title.isEmpty()) {
            Toast.makeText(this, "Pertanyaan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        val uri = viewModel.currentImageUri
        Log.d("AddDiscussionActivity", "$uri")

        uri?.let { validUri ->
            lifecycleScope.launch {
                try {
                    val imageFile = withContext(Dispatchers.IO) {
                        uriToFile(validUri, this@AddDiscussionActivity).reduceFileImage()
                    }
                    processUpload(imageFile, desc, title, viewModel.selectedPlant ?: 0)
                } catch (e: Exception) {
                    Log.e("AddDiscussionActivity", "Error processing image", e)
                    Toast.makeText(this@AddDiscussionActivity, "Terjadi kesalahan saat memproses gambar", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: run {
            Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
        }
    }


    private fun processUpload(imageFile: File, desc: String, title: String, selectedPlant: Int) {
        Log.d("AddDiscussionActivity", "processuplaod")
        viewModel.addDiscussion(title, desc, imageFile, selectedPlant).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    loadingDialog.startLoadingDialog()
                }

                is Result.Success -> {
                    loadingDialog.dismissDialog()
                    successDialog.startSuccessDialog("Upload Berhasil!")
                    navigateBack()
                }

                is Result.Error -> {
                    loadingDialog.dismissDialog()
                    failedDialog.startFailedDialog("Upload Gagal!")
                }
            }
        }
    }

    private fun navigateBack() {
        lifecycleScope.launch {
            delay(2000)
            successDialog.dismissDialog()
                finish()
        }
    }
}
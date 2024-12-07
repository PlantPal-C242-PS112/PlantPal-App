package com.android.plantpal.ui.account.edit

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.plantpal.databinding.ActivityEditProfileBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.account.AccountViewModel
import com.android.plantpal.ui.customview.CustomEditFullname
import com.android.plantpal.ui.utils.Result
import com.android.plantpal.ui.utils.dialog.FailedDialog
import com.android.plantpal.ui.utils.dialog.LoadingDialog
import com.android.plantpal.ui.utils.dialog.SuccessDialog
import com.android.plantpal.ui.utils.reduceFileImage
import com.android.plantpal.ui.utils.uriToFile
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var viewModel: AccountViewModel
    private val successDialog = SuccessDialog(this)
    private val loadingDialog = LoadingDialog(this)
    private val failedDialog = FailedDialog(this)

    private var isPhotoChanged = false
    private var isNameChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        viewModel = ViewModelProvider(this, viewModelFactory)[AccountViewModel::class.java]

        setupData()
    }

    private fun setupData() {
        viewModel.getUserDetails().observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val userData = result.data.data
                    val fullname = viewModel.fullname ?: userData.fullname
                    val photoUri = viewModel.currentImageUri ?: Uri.parse(userData.profilePhoto.toString())

                    binding.edFullnameProf.setText(fullname)
                    Glide.with(binding.profilePic.context)
                        .load(photoUri)
                        .into(binding.profilePic)

                    setupButton(userData.fullname, fullname)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Tidak dapat load foto profil", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupButton(initialFullname: String, currentFullname: String) {
        binding.edFullnameProf.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                isNameChanged = s.toString().trim() != initialFullname
                viewModel.fullname = s.toString().trim()
                updateSaveButtonVisibility()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.editPhoto.setOnClickListener {
            startGallery()
        }

        binding.buttonSave.setOnClickListener {
            lifecycleScope.launch {
                val imageFile = if (isPhotoChanged) {
                    try {
                        viewModel.currentImageUri?.let { uri ->
                            withContext(Dispatchers.IO) {
                                uriToFile(uri, this@EditProfileActivity).reduceFileImage()
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@EditProfileActivity, "Terjadi kesalahan saat memproses gambar", Toast.LENGTH_SHORT).show()
                        null
                    }
                } else null

                updateProfile(imageFile, currentFullname)
            }
        }
    }

    private fun updateProfile(photoFile: File?, currentFullname: String) {
        Log.d("UpdateProfile", "$photoFile, $currentFullname")
        viewModel.updateProfile(photoFile, currentFullname).observe(this) { result ->
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
                    Log.e("Upload", "Error processing profile update: ${result.error}")
                }
            }
        }

    }

    private fun updateSaveButtonVisibility() {
        binding.buttonSave.visibility = if (isNameChanged || isPhotoChanged) View.VISIBLE else View.GONE
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            isPhotoChanged = true
            viewModel.currentImageUri = uri
            showImage()
            updateSaveButtonVisibility()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        viewModel.currentImageUri?.let {
            binding.profilePic.setImageURI(it)
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


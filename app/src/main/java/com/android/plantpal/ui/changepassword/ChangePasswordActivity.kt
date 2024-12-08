package com.android.plantpal.ui.changepassword

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.plantpal.R
import com.android.plantpal.data.remote.ChangeForgotPasswordRequest
import com.android.plantpal.databinding.ActivityChangePasswordBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.login.LoginActivity
import com.android.plantpal.ui.utils.Result
import com.android.plantpal.ui.utils.dialog.FailedDialog
import com.android.plantpal.ui.utils.dialog.LoadingDialog
import com.android.plantpal.ui.utils.dialog.SuccessDialog

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var changePasswordViewModel: ChangePasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.iconLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(applicationContext)
        changePasswordViewModel = ViewModelProvider(this, factory)[ChangePasswordViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnBackToLogin.setOnClickListener {
            finish()
        }

        binding.btnChangePassword.setOnClickListener {
            val newPassword = binding.edNewPassword.text.toString().trim()
            val confirmPassword = binding.edConfirmPassword.text.toString().trim()

            if (validateInputs(newPassword, confirmPassword)) {
                changePassword(newPassword)
            }
        }
    }

    private fun validateInputs(newPassword: String, confirmPassword: String): Boolean {
        return when {
            newPassword.isEmpty() -> {
                Toast.makeText(this, R.string.new_password_empty_error, Toast.LENGTH_SHORT).show()
                false
            }
            confirmPassword.isEmpty() -> {
                Toast.makeText(this, R.string.confirm_new_password, Toast.LENGTH_SHORT).show()
                false
            }
            newPassword != confirmPassword -> {
                Toast.makeText(this, R.string.passwords_same_error, Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun changePassword(newPassword: String) {
        val loadingDialog = LoadingDialog(this)
        val email = intent.getStringExtra("EMAIL") ?: ""

        if (email.isEmpty()) {
            val failedDialog = FailedDialog(this)
            failedDialog.startFailedDialog(getString(R.string.email_not_found))
            return
        }

        changePasswordViewModel.changeForgotPassword(email, newPassword).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    loadingDialog.startLoadingDialog()
                }

                is Result.Success -> {
                    loadingDialog.dismissDialog()
                    val successDialog = SuccessDialog(this)
                    successDialog.startSuccessDialog(getString(R.string.save_password_success))
                    navigateToLogin()
                }

                is Result.Error -> {
                    loadingDialog.dismissDialog()
                    val failedDialog = FailedDialog(this)
                    failedDialog.startFailedDialog(getString(R.string.save_password_failed))
                }
            }
        }
    }

    private fun navigateToLogin() {
        Intent(this@ChangePasswordActivity, LoginActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(it)
            finish()
        }
    }
}

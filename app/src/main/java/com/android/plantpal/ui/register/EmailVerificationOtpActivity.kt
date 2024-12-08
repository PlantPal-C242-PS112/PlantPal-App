package com.android.plantpal.ui.register

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
import androidx.lifecycle.lifecycleScope
import com.android.plantpal.R
import com.android.plantpal.databinding.ActivityEmailVerificationOtpBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.login.LoginActivity
import com.android.plantpal.ui.utils.Result
import com.android.plantpal.ui.utils.dialog.FailedDialog
import com.android.plantpal.ui.utils.dialog.LoadingDialog
import com.android.plantpal.ui.utils.dialog.SuccessDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EmailVerificationOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmailVerificationOtpBinding
    private lateinit var registerViewModel: RegisterViewModel
    private val successDialog = SuccessDialog(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailVerificationOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        registerViewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]

        setupView()
        setupAction()
        playAnimation()

        val email = intent.getStringExtra("EMAIL") ?: ""
        binding.emailForgotPassword.text = email
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

    private fun setupAction() {
        binding.btnVerifyOtp.setOnClickListener {
            verifyOtp()
        }
    }

    private fun verifyOtp() {
        val loadingDialog = LoadingDialog(this)
        val otp = binding.inputOtp.text.toString().trim()
        val email = intent.getStringExtra("EMAIL") ?: ""

        if (otp.isNotEmpty() && email.isNotEmpty()) {
            registerViewModel.verifyEmail(email, otp).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        loadingDialog.startLoadingDialog()
                    }

                    is Result.Success -> {
                        loadingDialog.dismissDialog()
                        successDialog.startSuccessDialog(getString(R.string.otp_verified))
                        navigateToLogin()
                    }

                    is Result.Error -> {
                        loadingDialog.dismissDialog()
                        val failedDialog = FailedDialog(this)
                        failedDialog.startFailedDialog(getString(R.string.otp_verification_failed))
                    }
                }
            }
        } else {
            Toast.makeText(this, R.string.otp_empty, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToLogin() {
        lifecycleScope.launch {
            delay(2000)
            successDialog.dismissDialog()
            Intent(this@EmailVerificationOtpActivity, LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.iconLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

}
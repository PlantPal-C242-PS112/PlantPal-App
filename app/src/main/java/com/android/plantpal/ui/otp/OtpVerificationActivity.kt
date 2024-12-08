package com.android.plantpal.ui.otp

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.plantpal.R
import com.android.plantpal.databinding.ActivityOtpVerificationBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.changepassword.ChangePasswordActivity
import com.android.plantpal.ui.forgotpw.ForgotPasswordActivity
import com.android.plantpal.ui.utils.Result
import com.android.plantpal.ui.utils.dialog.FailedDialog
import com.android.plantpal.ui.utils.dialog.LoadingDialog
import com.android.plantpal.ui.utils.dialog.SuccessDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OtpVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpVerificationBinding
    private lateinit var otpVerificationViewModel: OtpVerificationViewModel
    private val successDialog = SuccessDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        otpVerificationViewModel = ViewModelProvider(this, viewModelFactory)[OtpVerificationViewModel::class.java]

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

        binding.btnBackToForgotPassword.setOnClickListener {
            backToForgotPassword()
        }
    }


    private fun verifyOtp() {
        val loadingDialog = LoadingDialog(this)
        val otp = binding.inputOtp.text.toString().trim()
        val email = intent.getStringExtra("EMAIL") ?: ""

        if (otp.isNotEmpty() && email.isNotEmpty()) {
            otpVerificationViewModel.verifyForgotPasswordOtp(email, otp).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        loadingDialog.startLoadingDialog()
                    }

                    is Result.Success -> {
                        loadingDialog.dismissDialog()
                        successDialog.startSuccessDialog(getString(R.string.otp_verified))
                        navigateToChangePassword()
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

    private fun navigateToChangePassword() {
        lifecycleScope.launch {
            delay(2000)
            successDialog.dismissDialog()
            Intent(this@OtpVerificationActivity, ChangePasswordActivity::class.java).also {
                val email = intent.getStringExtra("EMAIL") ?: ""
                it.putExtra("EMAIL", email)
                startActivity(it)
            }
        }
    }

    private fun backToForgotPassword() {
        lifecycleScope.launch {
            delay(2000)
            successDialog.dismissDialog()
            Intent(this@OtpVerificationActivity, ForgotPasswordActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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

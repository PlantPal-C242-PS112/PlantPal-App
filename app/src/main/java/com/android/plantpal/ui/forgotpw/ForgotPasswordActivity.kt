package com.android.plantpal.ui.forgotpw

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.android.plantpal.R
import com.android.plantpal.ui.utils.Result
import com.android.plantpal.databinding.ActivityForgotPasswordBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.changepassword.ChangePasswordActivity
import com.android.plantpal.ui.login.LoginActivity
import com.android.plantpal.ui.otp.OtpVerificationActivity
import com.android.plantpal.ui.utils.dialog.FailedDialog
import com.android.plantpal.ui.utils.dialog.LoadingDialog
import com.android.plantpal.ui.utils.dialog.SuccessDialog

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()
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
        forgotPasswordViewModel = ViewModelProvider(this, factory)[ForgotPasswordViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnBackToLogin.setOnClickListener {
            Intent(this@ForgotPasswordActivity, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
                finish()
            }
        }

        binding.btnSendOtp.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                sendForgotPasswordOtp(email)
            } else {
                Toast.makeText(this, R.string.email_empty_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendForgotPasswordOtp(email: String) {
        val loadingDialog = LoadingDialog(this)
        forgotPasswordViewModel.forgotPassword(email).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    loadingDialog.startLoadingDialog()
                }
                is Result.Success -> {
                    loadingDialog.dismissDialog()
                    val successDialog = SuccessDialog(this)
                    successDialog.startSuccessDialog(getString(R.string.otp_sent_success))
                    Handler(Looper.getMainLooper()).postDelayed({
                        navigateToOtpVerification(email)
                    }, 2000)
                }
                is Result.Error -> {
                    val failedDialog = FailedDialog(this)
                    failedDialog.startFailedDialog(getString(R.string.otp_sent_failed))
                }
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

    private fun navigateToOtpVerification(email: String) {
        Intent(this@ForgotPasswordActivity, OtpVerificationActivity::class.java).also {
            it.putExtra("EMAIL", email)
            startActivity(it)
        }
    }
}

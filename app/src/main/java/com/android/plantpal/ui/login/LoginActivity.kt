package com.android.plantpal.ui.login

import android.animation.AnimatorSet
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
import com.android.plantpal.MainActivity
import com.android.plantpal.R
import com.android.plantpal.data.preference.UserPreference
import com.android.plantpal.data.preference.dataStore
import com.android.plantpal.databinding.ActivityLoginBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.forgotpw.ForgotPasswordActivity
import com.android.plantpal.ui.register.RegisterActivity
import com.android.plantpal.ui.utils.Result
import com.android.plantpal.ui.utils.dialog.FailedDialog
import com.android.plantpal.ui.utils.dialog.LoadingDialog
import com.android.plantpal.ui.utils.dialog.SuccessDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userPreference: UserPreference
    private val successDialog = SuccessDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        loginViewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        setupView()
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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.iconLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.iconBunga, View.TRANSLATION_Y, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.iconJeruk, View.TRANSLATION_Y, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.iconTomat, View.TRANSLATION_Y, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.iconTunas, View.TRANSLATION_Y, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(400)
        val welcomeText =
            ObjectAnimator.ofFloat(binding.welcomeText, View.ALPHA, 1f).setDuration(400)
        val welcomeDesc =
            ObjectAnimator.ofFloat(binding.welcomeDesc, View.ALPHA, 1f).setDuration(400)
        val emailText = ObjectAnimator.ofFloat(binding.emailText, View.ALPHA, 1f).setDuration(400)
        val emailIn =
            ObjectAnimator.ofFloat(binding.edLoginEmailLayout, View.ALPHA, 1f).setDuration(400)
        val passText = ObjectAnimator.ofFloat(binding.passwordText, View.ALPHA, 1f).setDuration(400)
        val passIn =
            ObjectAnimator.ofFloat(binding.edLoginPasswordLayout, View.ALPHA, 1f).setDuration(400)
        val forgot = ObjectAnimator.ofFloat(binding.lupaPasswordText, View.ALPHA, 1f).setDuration(400)
        val register = ObjectAnimator.ofFloat(binding.belumPunyaAkun, View.ALPHA, 1f).setDuration(400)

        AnimatorSet().apply {
            playSequentially(
                welcomeText,
                welcomeDesc,
                emailText,
                emailIn,
                passText,
                passIn,
                forgot,
                login,
                register
            )
            startDelay = 100
            start()
        }
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            login()
        }

        binding.belumPunyaAkun.setOnClickListener{
            navigateToRegister()
        }

        binding.lupaPasswordText.setOnClickListener{
            navigateToForgotPassword()
        }
    }


    private fun login() {
        val loadingDialog = LoadingDialog(this)
        val identifier = binding.edLoginEmail.text.toString().trim()
        val password = binding.edLoginPassword.text.toString().trim()

        if (identifier.isNotEmpty() && password.isNotEmpty()) {
            loginViewModel.login(identifier, password).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        loadingDialog.startLoadingDialog()
                    }

                    is Result.Success -> {
                        loadingDialog.dismissDialog()
                        successDialog.startSuccessDialog(getString(R.string.login_success))
                        navigateToMain()
                    }

                    is Result.Error -> {
                        loadingDialog.dismissDialog()
                        val failedDialog = FailedDialog(this)
                        failedDialog.startFailedDialog(getString(R.string.login_failed))
                    }
                }
            }
        } else {
            Toast.makeText(this, R.string.email_and_pass_empty, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToRegister() {
        Intent(this@LoginActivity, RegisterActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(it)
            finish()
        }
    }

    private fun navigateToForgotPassword() {
        Intent(this@LoginActivity, ForgotPasswordActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(it)
            finish()
        }
    }

    private fun navigateToMain() {
        lifecycleScope.launch {
            delay(2000)
            successDialog.dismissDialog()
            Intent(this@LoginActivity, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it)
                finish()
            }
        }
    }
}
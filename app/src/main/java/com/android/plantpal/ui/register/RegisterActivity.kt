package com.android.plantpal.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.plantpal.R
import com.android.plantpal.data.remote.RegisterRequest
import com.android.plantpal.databinding.ActivityRegisterBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.login.LoginActivity
import com.android.plantpal.ui.utils.dialog.FailedDialog
import com.android.plantpal.ui.utils.dialog.LoadingDialog
import com.android.plantpal.ui.utils.dialog.SuccessDialog
import com.android.plantpal.ui.utils.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private val successDialog = SuccessDialog(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        registerViewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]

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

    private fun setupAction() {
        binding.registerButton.setOnClickListener {
            val username = binding.edRegisterUsername.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val fullname = binding.edRegisterName.text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || fullname.isEmpty()) {
                Toast.makeText(this, R.string.all_req, Toast.LENGTH_SHORT).show()
            } else {
                registerUser(username, email, password, fullname)
            }
        }

        binding.sudahPunyaAkun.setOnClickListener{
            navigateToLogin()
        }
    }

    private fun registerUser(username: String, email: String, password: String, fullname: String) {
        val loadingDialog = LoadingDialog(this)

        registerViewModel.register(username, email, password, fullname).observe(this@RegisterActivity) { result ->
            when (result) {
                is Result.Loading -> {
                    loadingDialog.startLoadingDialog()
                }
                is Result.Success -> {
                    loadingDialog.dismissDialog()
                    successDialog.startSuccessDialog(getString(R.string.register_success))
                    navigateToLogin()
                }
                is Result.Error -> {
                    loadingDialog.dismissDialog()
                    val failedDialog = FailedDialog(this)
                    failedDialog.startFailedDialog(getString(R.string.register_failed))
                }
            }
        }

    }

    private fun navigateToLogin() {
        lifecycleScope.launch {
            delay(2000)
            successDialog.dismissDialog()
            Intent(this@RegisterActivity, LoginActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }
    }

    private fun playAnimation() {
//        ObjectAnimator.ofFloat(binding.iconRegister, View.TRANSLATION_X, -30f, 30f).apply {
//            duration = 6000
//            repeatCount = ObjectAnimator.INFINITE
//            repeatMode = ObjectAnimator.REVERSE
//        }.start()

        val registerButton = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(400)
        val welcomeText = ObjectAnimator.ofFloat(binding.welcomeText, View.ALPHA, 1f).setDuration(400)
        val welcomeDesc = ObjectAnimator.ofFloat(binding.welcomeDesc, View.ALPHA, 1f).setDuration(400)
        val nameText = ObjectAnimator.ofFloat(binding.nameText, View.ALPHA, 1f).setDuration(400)
        val nameIn = ObjectAnimator.ofFloat(binding.edRegisterNameLayout, View.ALPHA, 1f).setDuration(400)
        val usernameText = ObjectAnimator.ofFloat(binding.usernameText, View.ALPHA, 1f).setDuration(400)
        val usernameIn = ObjectAnimator.ofFloat(binding.edRegisterUsernameLayout, View.ALPHA, 1f).setDuration(400)
        val emailText = ObjectAnimator.ofFloat(binding.emailText, View.ALPHA, 1f).setDuration(400)
        val emailIn = ObjectAnimator.ofFloat(binding.edRegisterEmailLayout, View.ALPHA, 1f).setDuration(400)
        val passText = ObjectAnimator.ofFloat(binding.passwordText, View.ALPHA, 1f).setDuration(400)
        val passIn = ObjectAnimator.ofFloat(binding.edRegisterPasswordLayout, View.ALPHA, 1f).setDuration(400)
        val sudahPunyaAkun = ObjectAnimator.ofFloat(binding.sudahPunyaAkun, View.ALPHA, 1f).setDuration(400)

        AnimatorSet().apply {
            playSequentially(
                welcomeText,
                welcomeDesc,
                nameText,
                nameIn,
                usernameText,
                usernameIn,
                emailText,
                emailIn,
                passText,
                passIn,
                registerButton,
                sudahPunyaAkun)
            startDelay = 100
        }.start()
    }
}
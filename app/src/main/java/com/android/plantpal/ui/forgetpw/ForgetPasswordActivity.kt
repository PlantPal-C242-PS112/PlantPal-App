package com.android.plantpal.ui.forgetpw

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.plantpal.R

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var btnBackToLogin: ImageView
    private lateinit var etEmail: EditText
    private lateinit var btnSendOTP: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        btnBackToLogin = findViewById(R.id.btnBackToLogin)
        etEmail = findViewById(R.id.etEmail)
        btnSendOTP = findViewById(R.id.btnSendOTP)

        btnBackToLogin.setOnClickListener {
            finish()
        }

        btnSendOTP.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Alamat email tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(email)) {
                Toast.makeText(this, "Masukkan alamat email yang valid", Toast.LENGTH_SHORT).show()
            } else {
                sendOtpToEmail(email)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun sendOtpToEmail(email: String) {
        Toast.makeText(this, "OTP telah dikirim ke $email", Toast.LENGTH_SHORT).show()
    }
}

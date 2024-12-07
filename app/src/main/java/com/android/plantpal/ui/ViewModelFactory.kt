package com.android.plantpal.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.plantpal.MainViewModel
import com.android.plantpal.data.Repository
import com.android.plantpal.data.di.Injection
import com.android.plantpal.ui.changepassword.ChangePasswordViewModel
import com.android.plantpal.ui.forgotpw.ForgotPasswordViewModel
import com.android.plantpal.ui.login.LoginViewModel
import com.android.plantpal.ui.otp.OtpVerificationViewModel
import com.android.plantpal.ui.plant.MyPlantsViewModel
import com.android.plantpal.ui.register.RegisterViewModel

class ViewModelFactory private constructor(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java) -> {
                ForgotPasswordViewModel(repository) as T
            }
            modelClass.isAssignableFrom(OtpVerificationViewModel::class.java) -> {
                OtpVerificationViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ChangePasswordViewModel::class.java) -> {
                ChangePasswordViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MyPlantsViewModel::class.java) -> {
                MyPlantsViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        fun getInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))
    }

}

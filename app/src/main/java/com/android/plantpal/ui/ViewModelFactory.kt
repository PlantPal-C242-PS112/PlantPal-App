package com.android.plantpal.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.plantpal.MainViewModel
import com.android.plantpal.data.Repository
import com.android.plantpal.data.di.Injection
import com.android.plantpal.ui.account.AccountViewModel
import com.android.plantpal.ui.discussion.DiscussionViewModel
import com.android.plantpal.ui.discussion.add.AddDiscussionViewModel
import com.android.plantpal.ui.home.analyze.AnalyzeViewModel
import com.android.plantpal.ui.home.plants.PlantsViewModel
import com.android.plantpal.ui.changepassword.ChangePasswordViewModel
import com.android.plantpal.ui.forgotpw.ForgotPasswordViewModel
import com.android.plantpal.ui.home.disease.DiseaseViewModel
import com.android.plantpal.ui.login.LoginViewModel
import com.android.plantpal.ui.otp.OtpVerificationViewModel
import com.android.plantpal.ui.plant.MyPlantsViewModel
import com.android.plantpal.ui.plant.analysis.AnalysisHistoryViewModel
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
            modelClass.isAssignableFrom(AnalyzeViewModel::class.java) -> {
                AnalyzeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DiscussionViewModel::class.java) -> {
                DiscussionViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AccountViewModel::class.java) -> {
                AccountViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddDiscussionViewModel::class.java) -> {
                AddDiscussionViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PlantsViewModel::class.java) -> {
                PlantsViewModel(repository) as T
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
            modelClass.isAssignableFrom(AnalysisHistoryViewModel::class.java) -> {
                AnalysisHistoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DiseaseViewModel::class.java) -> {
                DiseaseViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        fun getInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))
    }

}

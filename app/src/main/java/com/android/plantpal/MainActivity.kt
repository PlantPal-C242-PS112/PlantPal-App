package com.android.plantpal

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.plantpal.databinding.ActivityMainBinding
import com.android.plantpal.ui.ViewModelFactory
import com.android.plantpal.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        checkLogin()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_plant, R.id.navigation_discussion, R.id.navigation_account
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        handleDeepLink(navController)
    }

    private fun handleDeepLink(navController: NavController) {
        val deepLinkIntent = intent
        if (deepLinkIntent != null && deepLinkIntent.action == Intent.ACTION_VIEW) {
            val deepLinkUri = deepLinkIntent.data
            if (deepLinkUri != null && deepLinkUri.host == "reminders") {
                navController.navigate(R.id.navigation_home)
            }
        }
    }

    private fun checkLogin() {
        mainViewModel.getSession().observe(this@MainActivity) { user ->
            if (user.isLogin) {

            } else {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}
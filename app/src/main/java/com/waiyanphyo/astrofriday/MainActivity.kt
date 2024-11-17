package com.waiyanphyo.astrofriday

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.waiyanphyo.astrofriday.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val nightMode = sharedPreferences.getInt("night_mode", AppCompatDelegate.MODE_NIGHT_NO)
        AppCompatDelegate.setDefaultNightMode(nightMode)

        val isLoggedIn = sharedPreferences.getBoolean("is_logged_in", false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        if (isLoggedIn) {
            navController.navigate(R.id.navigation_astronomy, null, NavOptions.Builder().setPopUpTo(R.id.navigation_astronomy, true).build())
        } else {
            navController.navigate(R.id.loginFragment)
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_astronomy, R.id.navigation_sport, R.id.navigation_search
            )
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.loginFragment) {
                supportActionBar?.hide()
                navView.visibility = View.GONE
            } else {
                supportActionBar?.show()
                navView.visibility = View.VISIBLE
            }
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.id == R.id.navigation_astronomy || navController.currentDestination?.id == R.id.loginFragment) {
                    finish()
                } else {
                    navController.popBackStack()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        updateThemeIcon(menu.findItem(R.id.action_theme))
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_theme -> {
                toggleTheme()
                true
            }
            R.id.action_logout -> {
                performLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performLogout() {
        sharedPreferences.edit {
            putBoolean("is_logged_in", false)
        }
        FirebaseAuth.getInstance().signOut()
        lifecycleScope.launch {
            CredentialManager.create(this@MainActivity)
                .clearCredentialState(ClearCredentialStateRequest())
        }
        findNavController(R.id.nav_host_fragment_activity_main).navigate(
            R.id.loginFragment,
            null,
            NavOptions.Builder().setPopUpTo(R.id.navigation_astronomy, true).build()
        )
    }

    private fun toggleTheme() {
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        val newMode = if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.MODE_NIGHT_NO
        } else {
            AppCompatDelegate.MODE_NIGHT_YES
        }

        AppCompatDelegate.setDefaultNightMode(newMode)

        // Save the new theme mode
        sharedPreferences.edit {
            putInt("night_mode", newMode)
        }

        recreate() // Optional: recreate activity to apply the theme immediately
    }

    private fun updateThemeIcon(menuItem: MenuItem) {
        val currentMode = AppCompatDelegate.getDefaultNightMode()
        menuItem.setIcon(
            if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
                R.drawable.baseline_light_mode_24
            } else {
                R.drawable.baseline_dark_mode_24
            }
        )
    }
}
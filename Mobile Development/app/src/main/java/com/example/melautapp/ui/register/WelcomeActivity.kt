package com.example.melautapp.ui.register

import SavePreferences
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.melaut.onboarding.Onboarding1Fragment
import com.example.melautapp.R
import com.example.melautapp.ui.MainActivity
import dataStore
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {

    private lateinit var savePreferences: SavePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1_main)
        savePreferences = SavePreferences.getInstance(dataStore)

        checkLoginStatus()
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.onBoardingContainer, Onboarding1Fragment())
                .commit()
        }


    }

    private fun checkLoginStatus() {
        lifecycleScope.launch {
            savePreferences.isLogin().collect { isLoggedIn ->
                if (isLoggedIn) {
                    navigateToMainActivity()
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        // Start MainActivity
        val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
        startActivity(intent)
        // Optionally finish LoginActivity so it won't be in the back stack
        finish()
    }
}

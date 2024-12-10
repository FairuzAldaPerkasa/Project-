package com.example.melautapp.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.melautapp.R

class LoginActivity : AppCompatActivity() {

    // Declare UI components
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var lupaPasswordTextView: TextView
    private lateinit var googleButton: Button
    private lateinit var facebookButton: Button
    private lateinit var daftarButton: Button
    private lateinit var masukTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize UI components
        usernameEditText = findViewById(R.id.username1)
        passwordEditText = findViewById(R.id.passwor1)
        lupaPasswordTextView = findViewById(R.id.lupaPassword)
        googleButton = findViewById(R.id.Button3)
        facebookButton = findViewById(R.id.Button4)
        daftarButton = findViewById(R.id.Button1)
        masukTextView = findViewById(R.id.masuk)

        // Set click listeners
        lupaPasswordTextView.setOnClickListener { onLupaPasswordClick() }
        googleButton.setOnClickListener { onGoogleLoginClick() }
        facebookButton.setOnClickListener { onFacebookLoginClick() }
        daftarButton.setOnClickListener { onDaftarClick() }
        masukTextView.setOnClickListener { onMasukClick() }
    }

    // Handle the "Lupa Password" click
    fun onLupaPasswordClick() {
        // Navigate to the password recovery activity (not implemented yet)
        Log.d("MainActivity", "Lupa Password clicked")
    }

    // Handle the Google login button click
    private fun onGoogleLoginClick() {
        // Handle Google login (you would integrate Firebase or OAuth here)
        Log.d("MainActivity", "Google login clicked")
    }

    // Handle the Facebook login button click
    private fun onFacebookLoginClick() {
        // Handle Facebook login (you would integrate Facebook SDK here)
        Log.d("MainActivity", "Facebook login clicked")
    }

    // Handle the "Daftar" button click
    private fun onDaftarClick() {
        // For example, you can start a new Activity when the "Daftar" button is clicked
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }



    // Handle the "Masuk" link click (for existing users)
    private fun onMasukClick() {
        // Navigate to login page
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}

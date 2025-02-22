package com.example.melautapp.ui

import SavePreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.melautapp.R
import com.example.melautapp.data.response.ProfileResponse
import com.example.melautapp.data.retrofit.ApiConfig
import com.example.melautapp.data.retrofit.AuthenService
import com.example.melautapp.databinding.ActivityMainBinding
import com.example.melautapp.ui.register.AuthService
import com.google.firebase.auth.FirebaseUser
import dataStore
import com.google.android.material.appbar.AppBarLayout
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var profileCircleImageView: CircleImageView
    private var profileImageUrl = "https://lh3.googleusercontent.com/-4qy2DfcXBoE/AAAAAAAAAAI/AAAAAAAABi4/rY-jrtntAi4/s640-il/photo.jpg"

    private lateinit var namaSide: TextView
    private lateinit var savePreferences: SavePreferences
    private lateinit var apiService: AuthenService
    var userIdSaved = -1
    private val authService = AuthService()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiService = ApiConfig.getAuthService()
        savePreferences = SavePreferences.getInstance(dataStore)

        // Find the NavigationView
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        // Get the header view from the navigation view
        val headerView: View = navigationView.getHeaderView(0)

        // Now find the TextView inside the header view
        namaSide = headerView.findViewById(R.id.namaSide)

        if (authService.isUserLoggedIn()) {
            val user: FirebaseUser? = authService.getLoggedInUser()
            user?.let {
                // User is logged in, you can access user details like email, display name, etc.
                val email = it.email
                val displayName = it.displayName
                namaSide.text = it.displayName
                // Proceed to main screen or show user-specific data
            }
        } else {
            lifecycleScope.launch {
                savePreferences.getUserId().collect{ userID ->
                    userIdSaved = userID!!
                    if (userID != null) {
                        userIdSaved = userID
                        fetchProfileData(userID)
                    }
                }
            }
        }

        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Hide the title

        // Make toolbar transparent and remove shadow
        binding.appBarMain.toolbar.setBackgroundColor(android.graphics.Color.TRANSPARENT) // Remove white background
        binding.appBarMain.toolbar.elevation = 0f // Remove shadow

        window.apply {
            // Atur status bar agar transparan
            statusBarColor = android.graphics.Color.TRANSPARENT
            decorView.systemUiVisibility =
                android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        profileCircleImageView = navView.getHeaderView(0).findViewById(R.id.imageView)
        Glide.with(this)
            .load(profileImageUrl)
            .into(profileCircleImageView)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_rumah, R.id.nav_editprofil
            ), drawerLayout
        )

        // Initialize ActionBar, even if we don't use it for visual display
        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Hide title

        // Set up ActionBarDrawerToggle for the hamburger icon
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up Navigation UI
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun fetchProfileData(id: Int) {
        // Make the API call
        val call = apiService.getProfile(id)

        call.enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if (response.isSuccessful) {
                    // Get the profile data from the response body
                    val profile = response.body()
                    if (profile != null) {
                        namaSide.text = profile.name
                        lifecycleScope.launch { savePreferences.saveusername(profile.name) }

                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                // Handle failure
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

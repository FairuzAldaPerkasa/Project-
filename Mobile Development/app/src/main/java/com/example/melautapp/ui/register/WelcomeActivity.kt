package com.example.melautapp.ui.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.melaut.onboarding.Onboarding1Fragment
import com.example.melautapp.R

class WelcomeActivity : AppCompatActivity() {

    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity1_main)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.onBoardingContainer, Onboarding1Fragment())
                .commit()
        }

    }
}

package com.example.melaut.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.melautapp.R
import com.example.melautapp.ui.register.LoginActivity
import com.google.android.material.textview.MaterialTextView

class Onboarding1Fragment : Fragment() {
    private lateinit var buttonLanjut: Button
    private lateinit var buttonSkip: MaterialTextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menghubungkan layout fragment dengan kelas ini
        return inflater.inflate(R.layout.fragment_onboarding1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonLanjut = view.findViewById(R.id.continueButton)
        buttonLanjut.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.onBoardingContainer, Onboarding2Fragment())
                .commit()
        }
        buttonSkip = view.findViewById(R.id.skipButton)
        buttonSkip.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }



    }
}

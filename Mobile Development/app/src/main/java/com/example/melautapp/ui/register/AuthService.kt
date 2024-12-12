package com.example.melautapp.ui.register

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthService {

    // FirebaseAuth instance
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Function to check if the user is logged in
    fun isUserLoggedIn(): Boolean {
        val currentUser: FirebaseUser? = auth.currentUser
        return currentUser != null  // Returns true if the user is logged in, otherwise false
    }

    // Function to get the current logged-in user details (if needed)
    fun getLoggedInUser(): FirebaseUser? {
        return auth.currentUser
    }
}

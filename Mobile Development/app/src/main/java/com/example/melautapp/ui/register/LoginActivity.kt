package com.example.melautapp.ui.register

import SavePreferences
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.melautapp.R
import com.example.melautapp.data.request.LoginRequest
import com.example.melautapp.data.response.LoginResponse
import com.example.melautapp.data.retrofit.ApiConfig
import com.example.melautapp.ui.MainActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dataStore
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    // Declare UI components
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var lupaPasswordTextView: TextView
    private lateinit var masukTextView: TextView
    private lateinit var buttonLogin: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var buttonGoogle: Button

    private lateinit var savePreferences: SavePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize SavePreferences using the DataStore from the context
        savePreferences = SavePreferences.getInstance(dataStore)

        auth = Firebase.auth

        // Initialize UI components
        usernameEditText = findViewById(R.id.username1)
        passwordEditText = findViewById(R.id.passwor1)
        lupaPasswordTextView = findViewById(R.id.lupaPassword)
        masukTextView = findViewById(R.id.masuk)
        buttonLogin = findViewById(R.id.Button1)
        buttonGoogle = findViewById(R.id.ButtonGoogle)

        // Set click listeners
        lupaPasswordTextView.setOnClickListener { onLupaPasswordClick() }
        masukTextView.setOnClickListener { onMasukClick() }

        buttonLogin.setOnClickListener {
            val username = usernameEditText.text.toString()
            val pass = passwordEditText.text.toString()
            login(username, pass)
        }

        buttonGoogle.setOnClickListener {
            signIn()
        }

    }

    fun login(username: String, pass: String){
        val request = LoginRequest(username, pass)
        val client = ApiConfig.getAuthService().login(request)
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val responseBody = response.body()
                if (responseBody != null){
                    lifecycleScope.launch {
                        savePreferences.saveToken(responseBody.accessToken)
                        savePreferences.saveUserId(responseBody.userId)
                        savePreferences.saveLoginStatus(true)
                    }
                    Toast.makeText(this@LoginActivity, "Login Berhasil ${responseBody.userId}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("Login Activity", "Login Gagal ${t}")
            }

        })
    }

    // Handle the "Lupa Password" click
    fun onLupaPasswordClick() {
        // Navigate to the password recovery activity (not implemented yet)
        Log.d("MainActivity", "Lupa Password clicked")
    }

    // Handle the "Masuk" link click (for existing users)
    private fun onMasukClick() {
        // Navigate to login page
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun signIn() {
        val credentialManager = CredentialManager.create(this) //import from androidx.CredentialManager
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("280836102349-flmes716sh1142upp4b5l245dhlqskrf.apps.googleusercontent.com")
            .build()
        val request = GetCredentialRequest.Builder() //import from androidx.CredentialManager
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential( //import from androidx.CredentialManager
                    request = request,
                    context = this@LoginActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) { //import from androidx.CredentialManager
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e("Login Activity", "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e("Login Activity", "Unexpected type of credential")
                }
            }
            else -> {
                // Catch any unrecognized credential type here.
                Log.e("Login Activity", "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("Login Activity", "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser
                    user?.let {
                        val intent =Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Log.w("Login Activity", "signInWithCredential:failure", task.exception)
                }
            }
    }
}

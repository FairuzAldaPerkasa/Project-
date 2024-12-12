package com.example.melautapp.ui.register
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
import com.example.melautapp.data.request.RegisRequest
import com.example.melautapp.data.response.LoginResponse
import com.example.melautapp.data.response.RegisResponse
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
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var daftarButton: Button
    private lateinit var loginTextView: TextView
    private lateinit var konfirPassword: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var ButtonGoggle: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        // Menghubungkan view dengan id di layout
        usernameEditText = findViewById(R.id.username1)
        phoneEditText = findViewById(R.id.phone1)
        passwordEditText = findViewById(R.id.passwor1) // Typo id pada XML, seharusnya password1
        daftarButton = findViewById(R.id.Button1)
        loginTextView = findViewById(R.id.masuk)
        konfirPassword = findViewById(R.id.konfirpasswor1)
        ButtonGoggle = findViewById(R.id.Button3)

        // Klik tombol daftar
        daftarButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val confpass = konfirPassword.text.toString().trim()

            if (username.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lengkapi semua field", Toast.LENGTH_SHORT).show()
            } else {
                register(username, phone, password, confpass)
            }
        }

        // Klik teks "Masuk" untuk login
        loginTextView.setOnClickListener {
            // Pindah ke activity login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        ButtonGoggle.setOnClickListener {
            signIn()
        }
    }
    fun register(username: String, phone:String, pass: String, confPass:String){
        val request = RegisRequest(username, phone, pass, confPass)
        val client = ApiConfig.getAuthService().register(request)
        client.enqueue(object : Callback<RegisResponse> {
            override fun onResponse(call: Call<RegisResponse>, response: Response<RegisResponse>) {
                val responseBody = response.body()
                if (responseBody != null){
                    Toast.makeText(this@RegisterActivity, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<RegisResponse>, t: Throwable) {
                Log.e("Register Activity", "Register Gagal ${t}")
            }

        })
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
                    context = this@RegisterActivity,
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
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    Log.w("Login Activity", "signInWithCredential:failure", task.exception)
                }
            }
    }
}

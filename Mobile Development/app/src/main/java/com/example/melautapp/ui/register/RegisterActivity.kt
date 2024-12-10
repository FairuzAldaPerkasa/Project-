package com.example.melautapp.ui.register
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.melautapp.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var daftarButton: Button
    private lateinit var loginTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Menghubungkan view dengan id di layout
        usernameEditText = findViewById(R.id.username1)
        phoneEditText = findViewById(R.id.phone1)
        passwordEditText = findViewById(R.id.passwor1) // Typo id pada XML, seharusnya password1
        daftarButton = findViewById(R.id.Button1)
        loginTextView = findViewById(R.id.masuk)

        // Klik tombol daftar
        daftarButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Lengkapi semua field", Toast.LENGTH_SHORT).show()
            } else {
                // Lakukan proses registrasi atau pindah ke aktivitas lain
                Toast.makeText(this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                // Anda bisa menambahkan logika registrasi di sini
            }
        }

        // Klik teks "Masuk" untuk login
        loginTextView.setOnClickListener {
            // Pindah ke activity login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}

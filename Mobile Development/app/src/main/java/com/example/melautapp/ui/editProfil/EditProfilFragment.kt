package com.example.melautapp.ui.editProfil

import SavePreferences
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.melautapp.data.request.EditRequest
import com.example.melautapp.data.response.EditProfileResponse
import com.example.melautapp.data.response.ProfileResponse
import com.example.melautapp.data.retrofit.ApiConfig
import com.example.melautapp.data.retrofit.AuthenService
import com.example.melautapp.databinding.FragmentEditprofilBinding
import com.example.melautapp.ui.register.LoginActivity
import kotlinx.coroutines.launch
import dataStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfilFragment : Fragment() {

    private var _binding: FragmentEditprofilBinding? = null

    private val binding get() = _binding!!

    private lateinit var savePreferences: SavePreferences
    private lateinit var apiService: AuthenService
    private var userIdSaved: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val editProfilViewModel =
            ViewModelProvider(this).get(EditProfilViewModel::class.java)
        _binding = FragmentEditprofilBinding.inflate(inflater, container, false)
        val root: View = binding.root

        apiService = ApiConfig.getAuthService()
        savePreferences = SavePreferences.getInstance(requireContext().dataStore)
        lifecycleScope.launch {
            savePreferences.getUserId().collect{ userID ->
                fetchProfileData(userID!!)
                userIdSaved = userID
            }
        }
        binding.buttonKeluar.setOnClickListener {
            logout()
        }
        var edit = true
        binding.buttonEdit.setOnClickListener {
            edit = false
            binding.apply {
                buttonEdit.visibility = View.GONE
                buttonSave.visibility = View.VISIBLE
                editNama.visibility = View.VISIBLE
                editPhone.visibility = View.VISIBLE
            }
        }

        if (edit){
            binding.buttonEdit.visibility = View.VISIBLE
            binding.buttonSave.visibility = View.GONE
        } else {
            binding.buttonEdit.visibility = View.GONE
            binding.buttonSave.visibility = View.VISIBLE
        }

        binding.buttonSave.setOnClickListener {
            val nama = binding.editNama.text.toString()
            val phone = binding.editPhone.text.toString()
            saveProfile(nama,phone, userIdSaved)
            edit = true
            binding.apply {
                buttonEdit.visibility = View.VISIBLE
                buttonSave.visibility = View.GONE
                editNama.visibility = View.GONE
                editPhone.visibility = View.GONE
            }
        }
        return root
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
                        binding.textName.text = profile.name
                        binding.editNama.setText(profile.name)
                        binding.editPhone.setText(profile.phone)
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                // Handle failure
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun saveProfile(nama: String, phone: String, id: Int){
        val request = EditRequest(nama,phone)
        val call = apiService.editProfile(id,request)

        call.enqueue(object : Callback<EditProfileResponse> {
            override fun onResponse(
                call: Call<EditProfileResponse>,
                response: Response<EditProfileResponse>,
            ) {
                if (response.isSuccessful) {
                    // Get the profile data from the response body
                    val profile = response.body()
                    if (profile != null) {
                        binding.textName.text = profile.name
                        binding.editNama.setText(profile.name)
                        binding.editPhone.setText(profile.phone)
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch profile", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun logout() {
        // Launch a coroutine to handle logout process
        lifecycleScope.launch {
            // Call the logout function to clear preferences
            savePreferences.logout()

            // Redirect to LoginActivity
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

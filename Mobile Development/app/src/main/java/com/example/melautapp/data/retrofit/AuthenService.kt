package com.example.melautapp.data.retrofit

import com.example.melautapp.data.request.EditRequest
import com.example.melautapp.data.request.LoginRequest
import com.example.melautapp.data.request.RegisRequest
import com.example.melautapp.data.response.EditProfileResponse
import com.example.melautapp.data.response.LoginResponse
import com.example.melautapp.data.response.ProfileResponse
import com.example.melautapp.data.response.RegisResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthenService {
    @POST("users")
    fun register(
        @Body request:RegisRequest
    ): Call<RegisResponse>

    @POST("login")
    fun login(
        @Body request:LoginRequest
    ): Call<LoginResponse>

    @PUT("profile/{id}")
    fun editProfile(
        @Path("id") id: Int,
        @Body request: EditRequest
    ): Call<EditProfileResponse>

    @GET("profile/{id}")
    fun getProfile(
        @Path("id") id: Int,
    ): Call<ProfileResponse>


}
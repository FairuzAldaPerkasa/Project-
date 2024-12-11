package com.example.melautapp.data.retrofit

import com.google.gson.annotations.SerializedName

data class ResultRequest(
    @field:SerializedName("lat")  // Pastikan nama parameter sesuai dengan yang diharapkan server
    val lat: Double,

    @field:SerializedName("lon")  // Pastikan nama parameter sesuai dengan yang diharapkan server
    val lon: Double
)



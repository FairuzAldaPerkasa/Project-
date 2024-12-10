package com.example.melautapp.data.retrofit

import com.google.gson.annotations.SerializedName

data class ResultRequest(
    @field:SerializedName("lat")
    val lat: Double,

    @field:SerializedName("lon")
    val lon: Double
)

package com.example.melautapp.data.retrofit

import com.example.melautapp.data.response.LocationResponse
import retrofit2.Call
import retrofit2.http.GET

interface WeatherService {
    //ambil sesuai wilayah
    @GET("wilayah.json")
    fun getLocations(): Call<List<LocationResponse>>
}

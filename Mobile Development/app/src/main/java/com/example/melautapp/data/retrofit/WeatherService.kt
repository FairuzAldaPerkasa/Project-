package com.example.melautapp.data.retrofit

import com.example.melautapp.data.response.LocationResponse
import com.example.melautapp.data.response.ResultResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    fun getLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<LocationResponse>

    @POST("result")
    fun postResult(
        @Body request: ResultRequest
    ): Call<ResultResponse>

    // Add a method for fetching the ResultResponse directly, if applicable
    @GET("result")
    fun getPredictionResult(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<ResultResponse> // Adjust this endpoint based on your API
}




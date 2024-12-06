package com.example.melautapp.data.response

import com.google.gson.annotations.SerializedName

data class LocationResponse(
	@field:SerializedName("propinsi")
	val propinsi: String,

	@field:SerializedName("kota")
	val kota: String,

	@field:SerializedName("kecamatan")
	val kecamatan: String,

	@field:SerializedName("lon")
	val lon: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("lat")
	val lat: String
)

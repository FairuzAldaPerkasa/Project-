package com.example.melautapp.data.response

import com.google.gson.annotations.SerializedName

data class ResultResponse(

	@field:SerializedName("input_data")
	val inputData: InputData,

	@field:SerializedName("predicted_rad")
	val predictedRad: Double, // Perbaiki tipe data menjadi Double jika ini adalah angka

	@field:SerializedName("predicted_condition")
	val predictedCondition: String
)

data class InputData(

	@field:SerializedName("Tx")
	val tx: Double, // Perbaiki tipe data sesuai kebutuhan

	@field:SerializedName("rad_m")
	val radM: Double, // Perbaiki tipe data sesuai kebutuhan

	@field:SerializedName("RH_avg")
	val rHAvg: Int,

	@field:SerializedName("Tavg")
	val tavg: Double, // Perbaiki tipe data sesuai kebutuhan

	@field:SerializedName("Tn")
	val tn: Double, // Perbaiki tipe data sesuai kebutuhan

	@field:SerializedName("ff_avg")
	val ffAvg: Double // Perbaiki tipe data sesuai kebutuhan
)

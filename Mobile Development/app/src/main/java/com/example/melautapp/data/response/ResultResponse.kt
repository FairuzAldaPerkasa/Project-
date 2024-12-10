package com.example.melautapp.data.response

import com.google.gson.annotations.SerializedName

data class ResultResponse(

	@field:SerializedName("input_data")
	val inputData: InputData,

	@field:SerializedName("predicted_rad")
	val predictedRad: Any,

	@field:SerializedName("predicted_condition")
	val predictedCondition: String
)

data class InputData(

	@field:SerializedName("Tx")
	val tx: Any,

	@field:SerializedName("rad_m")
	val radM: Any,

	@field:SerializedName("RH_avg")
	val rHAvg: Int,

	@field:SerializedName("Tavg")
	val tavg: Any,

	@field:SerializedName("Tn")
	val tn: Any,

	@field:SerializedName("ff_avg")
	val ffAvg: Any
)

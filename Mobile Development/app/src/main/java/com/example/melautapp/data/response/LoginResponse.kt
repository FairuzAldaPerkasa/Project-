package com.example.melautapp.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("msg")
	val msg: String,

	@field:SerializedName("accessToken")
	val accessToken: String,

	@field:SerializedName("userId")
	val userId: Int
)

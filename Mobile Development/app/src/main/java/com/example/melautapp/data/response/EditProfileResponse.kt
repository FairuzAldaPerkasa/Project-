package com.example.melautapp.data.response

import com.google.gson.annotations.SerializedName

data class EditProfileResponse(

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("message")
	val message: String
)

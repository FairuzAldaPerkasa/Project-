package com.example.melautapp.data.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("profile_photo")
	val profilePhoto: String,

	@field:SerializedName("phone")
	val phone: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)

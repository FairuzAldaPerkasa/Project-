package com.example.melautapp.data.response

import com.google.gson.annotations.SerializedName

data class WeatherResponse(

	@field:SerializedName("visibility")
	val visibility: Int,

	@field:SerializedName("timezone")
	val timezone: Int,

	@field:SerializedName("main")
	val main: Main,

	@field:SerializedName("clouds")
	val clouds: Clouds,

	@field:SerializedName("sys")
	val sys: Sys,

	@field:SerializedName("dt")
	val dt: Int,

	@field:SerializedName("coord")
	val coord: Coord,

	@field:SerializedName("weather")
	val weather: List<WeatherItem>,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("cod")
	val cod: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("base")
	val base: String,

	@field:SerializedName("wind")
	val wind: Wind
)

data class Clouds(

	@field:SerializedName("all")
	val all: Int
)

data class Wind(

	@field:SerializedName("deg")
	val deg: Any,

	@field:SerializedName("speed")
	val speed: Any
)

data class Coord(

	@field:SerializedName("lon")
	val lon: Double,

	@field:SerializedName("lat")
	val lat: Double,
)

data class Main(

	@field:SerializedName("temp")
	val temp: Any,

	@field:SerializedName("temp_min")
	val tempMin: Any,

	@field:SerializedName("humidity")
	val humidity: Int,

	@field:SerializedName("pressure")
	val pressure: Int,

	@field:SerializedName("feels_like")
	val feelsLike: Any,

	@field:SerializedName("temp_max")
	val tempMax: Any
)

data class Sys(

	@field:SerializedName("country")
	val country: String,

	@field:SerializedName("sunrise")
	val sunrise: Int,

	@field:SerializedName("sunset")
	val sunset: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("type")
	val type: Int,

	@field:SerializedName("message")
	val message: Any
)

data class WeatherItem(

	@field:SerializedName("icon")
	val icon: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("main")
	val main: String,

	@field:SerializedName("id")
	val id: Int
)
data class KabupatenResponse(
	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("local_names")
	val localNames: Map<String, String>?,

	@field:SerializedName("country")
	val country: String,

	@field:SerializedName("state")
	val state: String? // For example, this could be the "kabupaten" or region name
)


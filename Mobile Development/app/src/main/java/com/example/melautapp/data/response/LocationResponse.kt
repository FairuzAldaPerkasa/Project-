package com.example.melautapp.data.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class LocationResponse(
	@field:SerializedName("location")
	val location: String,

	@field:SerializedName("temperature")
	val temperature: Double,

	@field:SerializedName("temp_max")
	val tempMax: Double,

	@field:SerializedName("temp_min")
	val tempMin: Double,

	@field:SerializedName("humidity")
	val humidity: Int,

	@field:SerializedName("weather")
	val weather: String,

	@field:SerializedName("wind_speed")
	val windSpeed: Double,

	@field:SerializedName("pressure")
	val pressure: Int,

	@field:SerializedName("iconUrl")
	val iconUrl: String

) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString() ?: "",
		parcel.readDouble(),
		parcel.readDouble(),
		parcel.readDouble(),
		parcel.readInt(),
		parcel.readString() ?: "",
		parcel.readDouble(),
		parcel.readInt(),
		parcel.readString() ?: ""
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(location)
		parcel.writeDouble(temperature)
		parcel.writeDouble(tempMax)
		parcel.writeDouble(tempMin)
		parcel.writeInt(humidity)
		parcel.writeString(weather)
		parcel.writeDouble(windSpeed)
		parcel.writeInt(pressure)
		parcel.writeString(iconUrl)
	}

	override fun describeContents(): Int = 0

	companion object CREATOR : Parcelable.Creator<LocationResponse> {
		override fun createFromParcel(parcel: Parcel): LocationResponse {
			return LocationResponse(parcel)
		}

		override fun newArray(size: Int): Array<LocationResponse?> {
			return arrayOfNulls(size)
		}
	}
}

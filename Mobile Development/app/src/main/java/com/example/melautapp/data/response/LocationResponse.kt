package com.example.melautapp.data.response

import android.os.Parcel
import android.os.Parcelable
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
): Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString().toString(),
        parcel.readString().toString(),
		parcel.readString().toString(),
		parcel.readString().toString(),
		parcel.readString().toString(),
		parcel.readString().toString(),
	) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(id)
		parcel.writeString(propinsi)
		parcel.writeString(kota)
		parcel.writeString(kecamatan)
		parcel.writeString(lat)
		parcel.writeString(lon)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<LocationResponse> {
		override fun createFromParcel(parcel: Parcel): LocationResponse {
			return LocationResponse(parcel)
		}

		override fun newArray(size: Int): Array<LocationResponse?> {
			return arrayOfNulls(size)
		}
	}
}

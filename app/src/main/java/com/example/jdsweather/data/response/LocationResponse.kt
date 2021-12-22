package com.example.jdsweather.data.response

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("kode") val kode: String,
    @SerializedName("nama") val name: String
)

data class ListLocationResponse(
    @SerializedName("wilayah") val data: List<LocationResponse>
)

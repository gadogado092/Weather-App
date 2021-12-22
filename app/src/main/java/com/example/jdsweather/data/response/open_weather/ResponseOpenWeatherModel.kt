package com.example.jdsweather.data.response.open_weather

import com.google.gson.annotations.SerializedName

data class ResponseOpenWeatherModel(
    @SerializedName("status") var status: Boolean,
    @SerializedName("message") var message: String,
    @SerializedName("cod") var cod: String? = null,
    @SerializedName("cnt") var cnt: Int? = null,
    @SerializedName("list") var list: ArrayList<ListResponse> = arrayListOf()
)
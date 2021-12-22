package com.example.jdsweather.data.response.open_weather

import com.google.gson.annotations.SerializedName

data class ResponseWeatherModel(
    @SerializedName("status") var status: Boolean,
    @SerializedName("message") var message: String,
    @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
    @SerializedName("base") var base: String? = null,
    @SerializedName("main") var main: Main? = Main()
)
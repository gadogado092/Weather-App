package com.example.jdsweather.data.response.open_weather

import com.google.gson.annotations.SerializedName

data class ListResponse(
    @SerializedName("dt") var dt: Int? = null,
    @SerializedName("main") var main: Main? = Main(),
    @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
    @SerializedName("dt_txt") var dtTxt: String? = null
)


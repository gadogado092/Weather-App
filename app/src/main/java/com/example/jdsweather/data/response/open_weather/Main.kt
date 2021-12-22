package com.example.jdsweather.data.response.open_weather

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp") var temp: Double? = null
)

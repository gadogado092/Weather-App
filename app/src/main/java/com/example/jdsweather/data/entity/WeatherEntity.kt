package com.example.jdsweather.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherEntity(
    var time: String? = null,
    var icon: String? = null,
    var description: String? = null,
    var temp: String? = null
) : Parcelable
package com.example.jdsweather.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserEntity (
    var fullName: String = "",
    var provinceKode: String = "",
    var provinceName: String = "",
    var cityKode: String = "",
    var cityName: String = ""
):Parcelable
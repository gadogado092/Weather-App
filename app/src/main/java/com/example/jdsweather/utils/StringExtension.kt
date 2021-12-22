package com.example.jdsweather.utils

import android.annotation.SuppressLint
import android.util.Patterns
import java.math.BigInteger
import java.security.MessageDigest
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

fun isEmailValid(emailAddress: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()
}

fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}

@SuppressLint("SimpleDateFormat")
fun dateFormatToDisplay(dateString: String): String {
    val fmt = SimpleDateFormat("yyyy-MM-dd")
    val date = fmt.parse(dateString)

    val fmtOut = SimpleDateFormat("dd-MMM-yyyy")
    return fmtOut.format(date)
}

@SuppressLint("SimpleDateFormat")
fun dateFormatFromPicker(dateString: String): String {
    val fmt = SimpleDateFormat("yyyy-M-dd")
    val date = fmt.parse(dateString)

    val fmtOut = SimpleDateFormat("yyyy-MM-dd")
    return fmtOut.format(date)
}

@SuppressLint("SimpleDateFormat")
fun dateSplitFromEntity(dateString: String): Array<String> {
    val fmt = SimpleDateFormat("yyyy-MM-dd")
    val date = fmt.parse(dateString)

    val fmtOut = SimpleDateFormat("dd-M-yyyy")

    val date2 = fmtOut.format(date)


    return date2.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
}

fun splitToDisplay(time: String): Array<String> {
    return time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
}

fun timeAddZero(time: String): String {
    var result = time
    if (time.toInt() in 0..9) {
        result = "0$time"
    }
    return result
}

fun currencyFormatterString(num: String): String {
    if (num == "0" || num == "" || num == "null") {
        return ""
    }
    try {
        val m = num.toDouble()
        val formatter = DecimalFormat("###,###,###,###,###")
        return formatter.format(m).replace(',', '.')
    } catch (e: Exception) {

    }
    return "0";
}

fun currencyFormatterStringViewZero(num: String): String {
    if (num == "0" || num == "" || num == "null") {
        return "0"
    }
    try {
        val m = num.toDouble()
        val formatter = DecimalFormat("###,###,###,###,###")
        return formatter.format(m).replace(',', '.')
    } catch (e: Exception) {

    }
    return "0";
}

@SuppressLint("SimpleDateFormat")
fun generateDateTimeNow(value: Int = 0): String {
    val c = Calendar.getInstance()
    c.add(Calendar.DAY_OF_MONTH, value)
    val fmtOut = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return fmtOut.format(c.time)
}

@SuppressLint("SimpleDateFormat")
fun generateDate(value: Int = 0): String {
    val c = Calendar.getInstance()
    c.add(Calendar.DAY_OF_MONTH, value)
    val fmtOut = SimpleDateFormat("yyyy-MM-dd")
    return fmtOut.format(c.time)
}

@SuppressLint("SimpleDateFormat")
fun dateToGreeting(dateString: String): String {
    val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = fmt.parse(dateString)

    val fmtOut = SimpleDateFormat("HH")
    return when (fmtOut.format(date).toInt()) {
        in 5..10 -> "Selamat Pagi"
        in 11..14 -> "Selamat Siang"
        in 15..17 -> "Selamat Sore"
        else -> {
            "Selamat Malam"
        }
    }
}

@SuppressLint("SimpleDateFormat")
fun dateToDisplay(dateString: String): String {
    val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = fmt.parse(dateString)

    val fmtOut = SimpleDateFormat("EEEE, dd MMM yyyy")
    return fmtOut.format(date)
}

@SuppressLint("SimpleDateFormat")
fun parseTimeToDisplay(dateString: String): String {
    val fmt = SimpleDateFormat("HH:mm:ss")
    val date = fmt.parse(dateString)

    val fmtOut = SimpleDateFormat("HH:mm")
    return fmtOut.format(date)
}

@SuppressLint("SimpleDateFormat")
fun dateToDisplayShort(dateString: String): String {
    val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = fmt.parse(dateString)

    val fmtOut = SimpleDateFormat("EEE, dd MMM")
    return fmtOut.format(date)
}

fun capitalizeSentence(capString: String): String? {
    val capBuffer = StringBuffer()
    val capMatcher: Matcher =
        Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString)
    while (capMatcher.find()) {
        capMatcher.appendReplacement(
            capBuffer,
            capMatcher.group(1).uppercase() + capMatcher.group(2).lowercase()
        )
    }
    return capMatcher.appendTail(capBuffer).toString()
}
package com.example.windy.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatDate(timestamp: Int?,timeZoneOffset: Int): String {
    val timeZone = ZoneId.ofOffset("UTC",ZoneOffset.ofTotalSeconds(timeZoneOffset))

    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM")
        .withZone(timeZone)

    return formatter.format(Instant.ofEpochSecond(timestamp?.toLong() ?: 0))
}

fun convertUnixTimeToTime(unixTime: Long,timeZoneOffset: Int): String {
    val timeZone = ZoneId.ofOffset("UTC",ZoneOffset.ofTotalSeconds(timeZoneOffset))
    val date = Date(unixTime * 1000)
    val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone(timeZone)
    return format.format(date)
}

fun getCountryName(countryCode: String?): String {
    return countryCode?.let { Locale("", it).displayCountry } ?: "Unknown"
}



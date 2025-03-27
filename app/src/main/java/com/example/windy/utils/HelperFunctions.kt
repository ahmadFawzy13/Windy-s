package com.example.windy.utils

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun formatDate(timestamp: Int?): String {
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM")
        .withZone(ZoneId.systemDefault())

    return formatter.format(Instant.ofEpochSecond(timestamp?.toLong() ?: 0))
}

fun convertUnixTimeToTime(unixTime: Long): String {
    val date = Date(unixTime * 1000)
    val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return format.format(date)
}

fun getCountryName(countryCode: String?): String {
    return countryCode?.let { Locale("", it).displayCountry } ?: ""
}

fun getDayName(dateStringFormat: String): String {

    val dateTime = LocalDateTime.parse(dateStringFormat, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

    val dayName = dateTime.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH)

    return dateTime.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH)
}




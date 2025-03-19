package com.example.windy.data.remote

import com.example.windy.data.model.City
import com.example.windy.data.model.FiveDayWeatherDetails
import com.google.gson.annotations.SerializedName

data class FiveDayThreeHourResponse(
    val list: List<FiveDayWeatherDetails> = listOf(),
    val city: City? = null
)
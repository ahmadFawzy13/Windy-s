package com.example.windy.data.remote

import com.example.windy.data.model.City
import com.example.windy.data.model.FiveDayWeatherDetails

data class FiveDayThreeHourResponse(
    val list: List<FiveDayWeatherDetails> = listOf(),
    val city: City? = null
)
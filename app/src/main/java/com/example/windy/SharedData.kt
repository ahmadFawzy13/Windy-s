package com.example.windy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SharedCityName {
    var cityName by mutableStateOf("")
}
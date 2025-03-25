package com.example.windy.data.remote

import com.example.windy.data.model.Clouds
import com.example.windy.data.model.CountryDetails
import com.example.windy.data.model.Weather
import com.example.windy.data.model.WeatherDetails
import com.example.windy.data.model.Wind
import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    var weather: List<Weather>? = null,
    @SerializedName("main") var weatherDetails: WeatherDetails? = null,
    var wind: Wind? = null,
    var clouds: Clouds? = null,
    var dt: Int = 0,
    var sys: CountryDetails? = null,
    @SerializedName("name")
    var cityName: String? = null,
    @SerializedName("id")
    var cityId:Int,
    var timezone: Int = 0
)
package com.example.windy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Clouds(
    var all: Int = 0
)

data class Coordinates(
    var lon: Double = 0.0,
    var lat: Double = 0.0
)

data class WeatherDetails(
    var temp: Double = 0.0,
    @SerializedName("feels_like") var feelsLike: Double = 0.0,
    @SerializedName("temp_min") var tempMin: Double = 0.0,
    @SerializedName("temp_max") var tempMax: Double = 0.0,
    var pressure: Int = 0,
    var humidity: Int = 0,
    @SerializedName("sea_level") var seaLevel: Int = 0,
    @SerializedName("grnd_level") var grndLevel: Int = 0,
    @SerializedName("temp_kf") var tempKf: Double = 0.0
)

data class CountryDetails(
    var country: String? = null,
    var sunrise: Int = 0,
    var sunset: Int = 0
)

data class Weather(
    var id: Int = 0,
    var main: String? = null,
    var description: String? = null,
    var icon: String? = null
)

data class Wind(
    var speed: Double = 0.0,
    var deg: Int = 0,
    var gust: Double = 0.0
)

data class City(
    val id: Int,
    val name: String = "",
    val coord: Coordinates,
    val country: String = "",
    val population: Int = 0,
    val timezone: Int = 0,
    val sunrise: Int = 0,
    val sunset: Int = 0
)

@Entity(tableName = "favourites")
data class FavCity(
    @PrimaryKey val id:Int,
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val country: String = "",
    val population: Int = 0,
    val timezone: Int = 0,
    val sunrise: Int = 0,
    val sunset: Int = 0
)

data class Rain(
    @SerializedName("3h")
    val rainLastThreeHours: Double = 0.0
)

data class TimeOfDay(
    @SerializedName("pod")
    val periodOfDay: String = ""
)

data class FiveDayWeatherDetails(
    @SerializedName("dt")
    val dateAndTime: Int = 0,
    val main: WeatherDetails = WeatherDetails(),
    val weather: List<Weather> = emptyList(),
    val clouds: Clouds = Clouds(),
    val wind: Wind = Wind(),
    val visibility: Int = 0,
    val pop: Double = 0.0,
    val timeOfDay: TimeOfDay = TimeOfDay(),
    @SerializedName("dt_txt")
    val dateAndTimeAsString: String = "",
    val rain: Rain? = null
)







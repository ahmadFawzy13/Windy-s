package com.example.windy.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("appid") apiKey:String = "6d7d75f169e0855aeefd1ce1fabf5555",
        @Query("units") units:String = "metric"): CurrentWeatherResponse

    @GET("forecast")
    suspend fun getFiveDayThreeHourForecast(
        @Query("lat") lat:String,
        @Query("lon") lon:String,
        @Query("appid") apiKey:String = "6d7d75f169e0855aeefd1ce1fabf5555",
        @Query("units") units:String = "metric"): FiveDayThreeHourResponse

}
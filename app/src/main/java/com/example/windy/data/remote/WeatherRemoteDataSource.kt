package com.example.windy.data.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRemoteDataSource private constructor(private val service: WeatherApi){

    suspend fun getCurrentWeatherRemote(lat:String, lon:String, units:String): Flow<CurrentWeatherResponse> {
        return flowOf(service.getCurrentWeather(lat = lat,lon = lon, units = units))
    }

    suspend fun getFiveDayThreeHourWeatherRemote(lat: String, lon: String, units: String): Flow<FiveDayThreeHourResponse> {
        return flowOf(service.getFiveDayThreeHourForecast(lat = lat, lon = lon, units = units))
    }

    companion object {
        @Volatile
        private var INSTANCE: WeatherRemoteDataSource? = null

        fun getInstance(): WeatherRemoteDataSource{

            return INSTANCE?: synchronized(this){

                val instance = WeatherRemoteDataSource(RetroFitHelper.apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}
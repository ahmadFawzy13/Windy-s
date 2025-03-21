package com.example.windy.data.remote

interface IWeatherRemoteDataSource {
    suspend fun getCurrentWeatherRemote(lat: String, lon: String, units: String): CurrentWeatherResponse?
    suspend fun getFiveDayThreeHourWeatherRemote(lat: String, lon: String, units: String): FiveDayThreeHourResponse?
}
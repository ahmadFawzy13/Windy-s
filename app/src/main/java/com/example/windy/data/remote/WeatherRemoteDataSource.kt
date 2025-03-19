package com.example.windy.data.remote

class WeatherRemoteDataSource private constructor(private val service: WeatherApi){


    suspend fun getCurrentWeather(lat:String,lon:String,units:String): CurrentWeatherResponse? {
        return service.getCurrentWeather(lat = lat,lon = lon, units = units).body()
    }

    suspend fun getFiveDayThreeHourWeather(lat: String, lon: String, units: String): FiveDayThreeHourResponse? {
        return service.getFiveDayThreeHourForecast(lat = lat, lon = lon, units = units).body()
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
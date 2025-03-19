package com.example.windy.data.repo

import android.content.Context
import com.example.windy.data.local.WeatherLocalDataSource
import com.example.windy.data.model.City
import com.example.windy.data.model.FavCity
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.remote.WeatherRemoteDataSource

class Repository private constructor(private val localDataSource: WeatherLocalDataSource,
    private val remoteDataSource: WeatherRemoteDataSource){

    suspend fun getRemoteWeather(lat:String,lon:String,units:String): CurrentWeatherResponse? {
        return remoteDataSource.getCurrentWeather(lat,lon,units)
    }

    suspend fun getRemoteFiveDayThreeHourWeather(lat: String, lon: String, units: String): FiveDayThreeHourResponse? {
        return remoteDataSource.getFiveDayThreeHourWeather(lat,lon,units)
    }

    suspend fun getFavCities():List<FavCity>{
        return localDataSource.getFavCities()
    }

    suspend fun insertCity(favCity: FavCity):Long{
        return localDataSource.insertFavCity(favCity)
    }

    suspend fun deleteCity(id:Int):Int{
        return localDataSource.deleteFavCity(id)
    }


    companion object{
        @Volatile
        private var INSTANCE : Repository? = null

        fun getInstance(context:Context): Repository{

            return INSTANCE?:synchronized(this){
                val instance = Repository(WeatherLocalDataSource.getInstance(context),
                    WeatherRemoteDataSource.getInstance())
                INSTANCE = instance
                instance
            }
        }
    }
}
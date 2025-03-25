package com.example.windy.data.local

import android.content.Context
import com.example.windy.data.model.City
import com.example.windy.data.model.FavCity
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource (private val weatherDao: WeatherDao){

    fun getFavCitiesLocal():Flow<List<City>>{
        return weatherDao.getFavCities()
    }

    suspend fun insertFavCityLocal(city: City):Long{
        return weatherDao.insertFavCity(city)
    }

    suspend fun deleteFavCityLocal(id:Int):Int{
        return weatherDao.deleteFavCity(id)
    }
    companion object {
        @Volatile
        private var INSTANCE : WeatherLocalDataSource? = null

        fun getInstance(context:Context): WeatherLocalDataSource{

           return INSTANCE?: synchronized(this){

                val instance = WeatherLocalDataSource(WeatherDataBase.getInstance(context).getWeatherDao())
                INSTANCE = instance
                instance
            }
        }
    }
}
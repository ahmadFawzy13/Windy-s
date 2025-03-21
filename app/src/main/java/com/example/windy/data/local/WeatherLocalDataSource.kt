package com.example.windy.data.local

import android.content.Context
import com.example.windy.data.model.FavCity

class WeatherLocalDataSource private constructor(private val weatherDao: WeatherDao){

    suspend fun getFavCitiesLocal():List<FavCity>{
        return weatherDao.getFavCities()
    }

    suspend fun insertFavCityLocal(favCity: FavCity):Long{
        return weatherDao.insertFavCity(favCity)
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
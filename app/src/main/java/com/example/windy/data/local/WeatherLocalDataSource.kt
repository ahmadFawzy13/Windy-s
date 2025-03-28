package com.example.windy.data.local

import android.content.Context
import com.example.windy.data.model.Alarm
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

    suspend fun deleteFavCityLocal(city: City):Int{
        return weatherDao.deleteFavCity(city)
    }


    fun getAlarms():Flow<List<Alarm>>{
        return weatherDao.getAlarms()
    }

    suspend fun insertAlarm(alarm: Alarm):Long{
        return weatherDao.insertAlarm(alarm)
    }

    suspend fun deleteAlarm(alarm:Alarm):Int{
        return weatherDao.deleteAlarm(alarm)
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
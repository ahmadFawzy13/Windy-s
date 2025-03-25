package com.example.windy.data.repo

import android.content.Context
import com.example.windy.data.local.WeatherLocalDataSource
import com.example.windy.data.model.City
import com.example.windy.data.model.FavCity
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class Repository (private val localDataSource: WeatherLocalDataSource,
    private val remoteDataSource: WeatherRemoteDataSource){

    suspend fun getCurrentWeatherRemote(lat:String,lon:String,units:String): Flow<CurrentWeatherResponse> {
        return remoteDataSource.getCurrentWeatherRemote(lat,lon,units)
    }

    suspend fun getFiveDayThreeHourWeatherRemote(lat: String, lon: String, units: String): Flow<FiveDayThreeHourResponse> {
        return remoteDataSource.getFiveDayThreeHourWeatherRemote(lat,lon,units)
    }

    fun getFavCitiesLocal():Flow<List<City>>{
        return localDataSource.getFavCitiesLocal()
    }

    suspend fun insertFavCityLocal(city: City):Long{
        return localDataSource.insertFavCityLocal(city)
    }

    suspend fun deleteFavCityLocal(id:Int):Int{
        return localDataSource.deleteFavCityLocal(id)
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

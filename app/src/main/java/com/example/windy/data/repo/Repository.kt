package com.example.windy.data.repo

import android.content.Context
import com.example.windy.data.local.WeatherLocalDataSource
import com.example.windy.data.model.Alarm
import com.example.windy.data.model.City
import com.example.windy.data.model.HomeDetails
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.remote.WeatherRemoteDataSource
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.flow.Flow

class Repository (private val localDataSource: WeatherLocalDataSource,
    private val remoteDataSource: WeatherRemoteDataSource){

    suspend fun getCurrentWeatherRemote(lat:String,lon:String,units:String,lang:String): Flow<CurrentWeatherResponse> {
        return remoteDataSource.getCurrentWeatherRemote(lat,lon,units,lang)
    }

    suspend fun getFiveDayThreeHourWeatherRemote(lat: String, lon: String, units: String,lang:String): Flow<FiveDayThreeHourResponse> {
        return remoteDataSource.getFiveDayThreeHourWeatherRemote(lat,lon,units,lang)
    }

    suspend fun getPlaceOnMap(searchText:String,placesClient: PlacesClient) :Flow<LatLng>{
        return remoteDataSource.getPlaceOnMap(searchText,placesClient)
    }

    fun getFavCitiesLocal():Flow<List<City>>{
        return localDataSource.getFavCitiesLocal()
    }

    suspend fun insertFavCityLocal(city: City):Long{
        return localDataSource.insertFavCityLocal(city)
    }

    suspend fun deleteFavCityLocal(city: City):Int{
        return localDataSource.deleteFavCityLocal(city)
    }

    fun getAlarms():Flow<List<Alarm>>{
        return localDataSource.getAlarms()
    }

    suspend fun insertAlarm(alarm: Alarm):Long{
        return localDataSource.insertAlarm(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm):Int{
        return localDataSource.deleteAlarm(alarm)
    }

    suspend fun deleteAlarmById(alarmId:Int){
        return localDataSource.deleteAlarmById(alarmId)
    }

    suspend fun insertHome(homeDetails: HomeDetails):Long{
        return localDataSource.insertHome(homeDetails)
    }

    fun getLocalHomeDetails():Flow<HomeDetails>{
        return localDataSource.getLocalHomeDetails()
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

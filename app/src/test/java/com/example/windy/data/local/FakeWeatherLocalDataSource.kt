package com.example.windy.data.local

import com.example.windy.data.IWeatherLocalDataSource
import com.example.windy.data.model.FavCity

class FakeWeatherLocalDataSource(val favCityList : List<FavCity>, val insertResponse:Long, val deleteResponse:Int): IWeatherLocalDataSource {
    override suspend fun getFavCitiesLocal(): List<FavCity> = favCityList

    override suspend fun insertFavCityLocal(favCity: FavCity): Long = insertResponse

    override suspend fun deleteFavCityLocal(id: Int): Int = deleteResponse
}
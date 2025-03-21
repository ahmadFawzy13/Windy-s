package com.example.windy.data

import com.example.windy.data.model.FavCity

interface IWeatherLocalDataSource {
    suspend fun getFavCitiesLocal(): List<FavCity>
    suspend fun insertFavCityLocal(favCity: FavCity): Long
    suspend fun deleteFavCityLocal(id: Int): Int
}
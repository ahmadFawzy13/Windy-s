package com.example.windy.data.local
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.windy.data.model.City
import com.example.windy.data.model.FavCity

@Dao
interface WeatherDao {

    @Query("SELECT * FROM favourites")
    suspend fun getFavCities(): List <FavCity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavCity(favCity: FavCity) : Long

    @Query("DELETE FROM favourites WHERE id = :id")
    suspend fun deleteFavCity(id:Int) : Int
}

package com.example.windy.data.local
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.windy.data.model.Alarm
import com.example.windy.data.model.City
import com.example.windy.data.model.HomeDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM favourites")
    fun getFavCities(): Flow<List<City>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavCity(city: City) : Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHome(homeDetails: HomeDetails) : Long

    @Query("SELECT * FROM home")
    fun getHomeDetails(): Flow<HomeDetails>

    @Delete
    suspend fun deleteFavCity(city: City) : Int

    @Query("SELECT * FROM alarms")
    fun getAlarms(): Flow<List<Alarm>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlarm(alarm: Alarm): Long

    @Delete
    suspend fun deleteAlarm(alarm: Alarm) : Int

    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun deleteAlarmById(id:Int)
}

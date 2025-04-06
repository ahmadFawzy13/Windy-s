package com.example.windy.data.local
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.windy.utils.HomeDetailsTypeConverter
import com.example.windy.utils.CoordinatesTypeConverter
import com.example.windy.data.model.Alarm
import com.example.windy.data.model.City
import com.example.windy.data.model.HomeDetails


@Database(entities = [City::class, Alarm::class, HomeDetails::class], version = 1)
@TypeConverters(
    CoordinatesTypeConverter::class,
    HomeDetailsTypeConverter::class
)
abstract class WeatherDataBase: RoomDatabase() {

    abstract fun getWeatherDao():WeatherDao

    companion object{
        @Volatile
        private var INSTANCE: WeatherDataBase? = null

        fun  getInstance(context:Context): WeatherDataBase{

            return  INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, WeatherDataBase::class.java,"weather_database")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

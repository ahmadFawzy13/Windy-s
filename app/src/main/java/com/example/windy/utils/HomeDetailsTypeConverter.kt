package com.example.windy.utils

import androidx.room.TypeConverter
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.google.gson.Gson

class HomeDetailsTypeConverter {
        private val gson = Gson()

        @TypeConverter
        fun fromCurrentWeatherResponse(response: CurrentWeatherResponse?): String? {
            return response?.let { gson.toJson(it) }
        }

        @TypeConverter
        fun toCurrentWeatherResponse(json: String?): CurrentWeatherResponse? {
            return json?.let { gson.fromJson(it, CurrentWeatherResponse::class.java) }
        }

        @TypeConverter
        fun fromFiveDayThreeHourResponse(response: FiveDayThreeHourResponse?): String? {
            return response?.let { gson.toJson(it) }
        }

        @TypeConverter
        fun toFiveDayThreeHourResponse(json: String?): FiveDayThreeHourResponse? {
            return json?.let { gson.fromJson(it, FiveDayThreeHourResponse::class.java) }
        }
}
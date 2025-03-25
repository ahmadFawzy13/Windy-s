package com.example.windy

import androidx.room.TypeConverter
import com.example.windy.data.model.Coordinates

class CoordinatesTypeConverter {
    @TypeConverter
    fun fromString(value: String): Coordinates {
        val parts = value.split(",")
        return Coordinates(
            lon = parts[0].toDouble(),
            lat = parts[1].toDouble()
        )
    }

    @TypeConverter
    fun toString(coordinates: Coordinates): String {
        return "${coordinates.lon},${coordinates.lat}"
    }
}
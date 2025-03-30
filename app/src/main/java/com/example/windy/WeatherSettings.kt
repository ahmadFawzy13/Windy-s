package com.example.windy

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import androidx.core.content.edit
import java.util.Locale

class WeatherSettings private constructor (context: Context) {

    companion object{
        const val PREFS_NAME = "weather_settings"
        const val TEMP_KEY = "temp_unit"
        const val LANG_KEY = "lang"
        const val WIND_KEY = "wind_unit"
        const val LOCATION_KEY = "location"

        @Volatile
        private var INSTANCE: WeatherSettings? = null

        fun getInstance(context: Context): WeatherSettings{
            return INSTANCE?: synchronized(this){
                val instance = WeatherSettings(context)
                INSTANCE = instance
                instance
            }
        }
    }

    private val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setTemperatureUnit(tempUnit: String)= sharedPrefs.edit(){putString(TEMP_KEY, tempUnit)}

    fun getTemperatureUnit()= sharedPrefs.getString(TEMP_KEY,"c")

    fun setAppLanguage(context: Context, lang: String) {

        sharedPrefs.edit { putString(LANG_KEY, lang) }

        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        (context as? Activity)?.recreate()
    }

    fun getAppLanguage()= sharedPrefs.getString(LANG_KEY,"en")

    fun setWindSpeedUnit(windSpeedUnit:String)= sharedPrefs.edit(){putString(WIND_KEY, windSpeedUnit)}

    fun getWindSpeedUnit() = sharedPrefs.getString(WIND_KEY,"m/s")

    fun setLocationPref(locationPref: String) = sharedPrefs.edit {putString(LOCATION_KEY,locationPref)}

    fun getLocationPref() = sharedPrefs.getString(LOCATION_KEY,"gps")

}
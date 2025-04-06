package com.example.windy

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.example.windy.utils.WeatherSettings
import java.util.Locale

class MyWeatherApp : Application() {

    override fun attachBaseContext(base: Context) {
        val sharedPrefs = WeatherSettings.getInstance(base)
        super.attachBaseContext(retrieveLanguage(base,sharedPrefs.getAppLanguage() ?: "en")
        )
    }

    private fun retrieveLanguage(context: Context, language: String): Context {
        val locale = Locale(language)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}
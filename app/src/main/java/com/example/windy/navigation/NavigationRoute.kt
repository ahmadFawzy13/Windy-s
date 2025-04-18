package com.example.windy.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoute {
    @Serializable
    data class HomeFav (val favLat:String ="", val favLon:String = "") : NavigationRoute()
    @Serializable
    object  Home : NavigationRoute()
    @Serializable
    object Favourite : NavigationRoute()
    @Serializable
    object Map : NavigationRoute()
    @Serializable
    object Alarm : NavigationRoute()
    @Serializable
    object SetAlarm : NavigationRoute()
    @Serializable
    object Settings : NavigationRoute()
    @Serializable
    object MapSettings : NavigationRoute()
}
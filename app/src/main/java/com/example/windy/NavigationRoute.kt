package com.example.windy

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoute {
    @Serializable
    data class HomeWithParameters (val favLat:String ="",val favLon:String = "") : NavigationRoute()
    @Serializable
    object Home : NavigationRoute()
    @Serializable
    object Favourite : NavigationRoute()
    @Serializable
    object Map : NavigationRoute()
}
package com.example.windy

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoute {
    @Serializable
    object Home : NavigationRoute()
    @Serializable
    object Favourite : NavigationRoute()
    @Serializable
    object Map : NavigationRoute()
}
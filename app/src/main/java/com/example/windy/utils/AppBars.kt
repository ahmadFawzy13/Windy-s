package com.example.windy.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.windy.NavigationRoute

@Composable
fun MyBottomAppBar(navController: NavController?) {
        BottomAppBar(
            actions = {
                IconButton(onClick = { navController?.navigate(NavigationRoute.Home) }) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                }
                IconButton(onClick = { navController?.navigate(NavigationRoute.Favourite) }) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Localized description",
                    )
                }
                IconButton(onClick = { navController?.navigate(NavigationRoute.Home) }) {
                    Icon(
                        Icons.Filled.Notifications,
                        contentDescription = "Localized description",
                    )
                }
                IconButton(onClick = { navController?.navigate(NavigationRoute.Home) }) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Localized description",
                    )
                }
            }

        )
}

@Composable
fun MyBottomAppBarWithFab(navController: NavController?) {
    BottomAppBar(
        actions = {
            IconButton(onClick = { navController?.navigate(NavigationRoute.Home) }) {
                Icon(Icons.Filled.Home, contentDescription = "Localized description")
            }
            IconButton(onClick = { navController?.navigate(NavigationRoute.Favourite) }) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { navController?.navigate(NavigationRoute.Home) }) {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { navController?.navigate(NavigationRoute.Home) }) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Localized description",
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController?.navigate(NavigationRoute.Home) },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, "Localized description")
            }
        }
    )
}
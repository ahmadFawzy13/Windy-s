package com.example.windy.favourite.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.windy.favourite.viewmodel.FavViewModel
import com.example.windy.utils.MyBottomAppBarWithFab


@Composable
fun FavouriteScreen(navController: NavController,favViewModel: FavViewModel){
    val favCities = favViewModel.favCities.observeAsState()
    val favCityCurrentWeather = favViewModel.favCityCurrentWeather.observeAsState()
    val fiveDayFavCityWeather = favViewModel.fiveDayFavCityWeather.observeAsState()
    val responseMessage = favViewModel.responseMessage.observeAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(remember { SnackbarHostState() })},
        bottomBar = { MyBottomAppBarWithFab(navController) }
    )
    {contentPadding ->

        Text(modifier = Modifier.padding(contentPadding), text = "Fav")

    }

}
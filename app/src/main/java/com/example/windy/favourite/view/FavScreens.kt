package com.example.windy.favourite.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.windy.favourite.viewmodel.FavViewModel
import com.example.windy.utils.MyBottomAppBarWithFab


@Composable
fun FavouriteScreen(navController: NavController,favViewModel: FavViewModel){
    /*favViewModel.getRemoteFiveDayThreeHourWeather()
    favViewModel.favCityCurrentWeather()*/

    val favCities = favViewModel.favCities.collectAsStateWithLifecycle() //hghyro fl local
    val favCityCurrentWeather by favViewModel.favCityCurrentWeather.collectAsStateWithLifecycle()
    val fiveDayFavCityWeather by favViewModel.fiveDayFavCityWeather.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = { SnackbarHost(remember { SnackbarHostState() })},
        bottomBar = { MyBottomAppBarWithFab(navController) }
    )
    {contentPadding ->
        Text(modifier = Modifier.padding(contentPadding), text = "Fav")
    }

}
package com.example.windy.home.view

import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import com.example.windy.home.viewmodel.HomeViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.windy.Response
import com.example.windy.utils.MyBottomAppBar

@Preview(showSystemUi = true)
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel,currentLocation: MutableState<String?>, location:Location,unit:String){
    homeViewModel.getRemoteCurrentWeather(location.latitude.toString(),
        location.longitude.toString(),
        unit)

    homeViewModel.getRemoteFiveDayThreeHourWeather(location.latitude.toString(),
        location.longitude.toString(),
        unit)

    val currentWeather by homeViewModel.currentWeather.collectAsStateWithLifecycle()
    val fiveDayThreeHourWeather by homeViewModel.fiveDayThreeHourWeather.collectAsStateWithLifecycle()

    Scaffold(
        snackbarHost = {SnackbarHost(remember { SnackbarHostState() })},
        bottomBar = {MyBottomAppBar(navController)}

    )
    { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp), verticalArrangement = Arrangement.Center
        )
        {
            when(currentWeather){
                is Response.Success ->{}
                is Response.Failure -> {}
                is Response.Loading -> {
                    CircularProgressIndicator()
                }
            }
            Text(modifier = Modifier.padding(contentPadding), text = "Home")
        }
    }
}


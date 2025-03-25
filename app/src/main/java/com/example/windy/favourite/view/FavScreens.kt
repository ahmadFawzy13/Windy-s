package com.example.windy.favourite.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.GlideImage
import com.example.windy.Response
import com.example.windy.data.model.FavCity
import com.example.windy.favourite.viewmodel.FavViewModel
import com.example.windy.utils.LocationPickerMap
import com.example.windy.utils.MyBottomAppBarWithFab
import com.example.windy.utils.WeatherIconLink
import com.example.windy.utils.convertUnixTimeToTime
import com.example.windy.utils.getCountryName
import com.google.android.gms.maps.model.LatLng


@Composable
fun FavouriteScreen(navController: NavController,favViewModel: FavViewModel){
//    favViewModel.getRemoteFiveDayThreeHourWeather()
//    favViewModel.favCityCurrentWeather()
    favViewModel.getLocalFavCities()
    val favCities = favViewModel.favCities.collectAsStateWithLifecycle().value //hghyro fl local
    val favCityCurrentWeather = favViewModel.favCityCurrentWeather.collectAsStateWithLifecycle().value
    val fiveDayFavCityWeather = favViewModel.fiveDayFavCityWeather.collectAsStateWithLifecycle().value
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(remember { SnackbarHostState() })},
        bottomBar = { MyBottomAppBarWithFab(navController) },
        containerColor = Color(0xFF182354)
    )
    {contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        )
        {
           when(favCities){
               is Response.Success -> {
                   LazyColumn {
                       items(favCities.data.size){
                           FavCities(favCities.data[it])
                       }
                   }
               }
               is Response.Failure ->{
                   LaunchedEffect(favCities) {
                       snackBarHostState.showSnackbar(
                           message = favCities.error,
                           duration = SnackbarDuration.Short
                       )
                   }
               }
               is Response.Loading ->{
                   Row(modifier = Modifier.fillMaxSize(),
                       horizontalArrangement = Arrangement.Center,
                       verticalAlignment = Alignment.CenterVertically
                   ) {

                       CircularProgressIndicator(modifier = Modifier.size(100.dp))

                   }
               }
               is Response.SuccessDataBaseOp ->{
                   LaunchedEffect(favCities) {
                       snackBarHostState.showSnackbar(
                           message = favCities.msg,
                           duration = SnackbarDuration.Short
                       )
                   }
               }
           }
        }
    }
}

@Composable
fun FavCities(favCity: FavCity){

    val countryName = remember { getCountryName(favCity.country) }

    Card(modifier = Modifier
        .width(140.dp)
        .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    )
    {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(text = countryName,
                color = Color.White,
                fontSize = 20.sp
            )

            Text(
                text = "${countryName},${favCity.name}",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Icon(imageVector = Icons.Outlined.PlayArrow,
                contentDescription = null)

        }
    }

}

@Composable
fun MapScreen(
    navController: NavController?,
    onLocationSelected: (LatLng) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LocationPickerMap(onLocationSelected = onLocationSelected)


    }
}
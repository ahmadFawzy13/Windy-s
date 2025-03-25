package com.example.windy.favourite.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.windy.NavigationRoute
import com.example.windy.Response
import com.example.windy.data.model.City
import com.example.windy.favourite.viewmodel.FavViewModel
import com.example.windy.home.viewmodel.HomeViewModel
import com.example.windy.utils.MyBottomAppBarWithFab
import com.example.windy.utils.getCountryName
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun FavouriteScreen(navController: NavController,favViewModel: FavViewModel){
    favViewModel.getLocalFavCities()
    val favCities = favViewModel.favCities.collectAsStateWithLifecycle().value
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState)},
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
                           FavCities(favCities.data[it],favViewModel,navController)
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
fun FavCities(city: City,favViewModel: FavViewModel,navController: NavController){

    val countryName = getCountryName(city.country)
    val lat = city.coord.lat.toString()
    val lon = city.coord.lon.toString()

    Card(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .clickable{
            navController.navigate(NavigationRoute.HomeWithParameters(lat,lon))
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    )
    {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            Icon(imageVector = Icons.Filled.Delete,
                contentDescription = "Delete",
                tint = Color.White,
                modifier = Modifier.clickable{
                    favViewModel.deleteFavCity(city.id)
                })

            Spacer(modifier = Modifier.width(15.dp))

            Text(text = countryName,
                color = Color.White,
                fontSize = 25.sp
            )

            Spacer(modifier = Modifier.width(80.dp))

            Text(
                text = city.name,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )

        }
    }
}

@Composable
fun MapScreen(navController: NavController?, homeViewModel: HomeViewModel, favViewModel: FavViewModel) {

    val fiveDayThreeHourWeather = favViewModel.fiveDayFavCityWeather.collectAsStateWithLifecycle().value

    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }


    Box(modifier = Modifier.fillMaxSize()) {
        LocationPickerMap { latLng ->
            selectedLatLng = latLng
            // Fetch weather data using latLng.latitude/longitude
        }

        selectedLatLng?.let { latLng ->

            Card(
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 80.dp)
                    .align(Alignment.BottomCenter),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            ) {

                Box(modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center)
                {

                    Button(
                        onClick = {
                            favViewModel.getRemoteFiveDayThreeHourWeather(
                                latLng.latitude.toString(),
                                latLng.longitude.toString(),
                                "metric"
                            )
                            if (fiveDayThreeHourWeather is Response.Success) {

                                fiveDayThreeHourWeather.data.city?.let {
                                    favViewModel.insertFavCity(it)
                                }

                            }
                        }
                    ) {

                        Icon(Icons.Outlined.Favorite, contentDescription = "Add to fav")
                        Spacer(modifier = Modifier.width(5.dp))
                        Text("Save location", fontWeight = FontWeight.Bold)

                    }
                }
            }
        }
    }
}

@Composable
fun LocationPickerMap(
    onLocationSelected: (LatLng) -> Unit // Callback when user taps map
) {
    // Default position (e.g., Singapore)
    val defaultLocation = remember { LatLng(30.0381736, 30.9793528) }

    // Camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    // Selected marker
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            selectedLocation = latLng
            onLocationSelected(latLng) // Return coordinates
        }
    ) {
        // Add marker if a location is selected
        selectedLocation?.let { location ->
            Marker(
                state = MarkerState(position = location),
                title = "Weather Location"
            )
        }
    }
}


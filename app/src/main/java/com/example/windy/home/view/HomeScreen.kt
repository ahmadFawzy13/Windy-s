package com.example.windy.home.view

import android.location.Location
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.windy.home.viewmodel.HomeViewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.windy.Response
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.utils.MyBottomAppBar
import com.example.windy.utils.WeatherIconLink
import com.example.windy.utils.convertUnixTimeToTime
import com.example.windy.utils.formatDate
import com.example.windy.utils.getCountryName
import com.example.windy.utils.getDayName

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel, location:Location,unit:String="metric"){

    homeViewModel.getRemoteCurrentWeather(location.latitude.toString(),
        location.longitude.toString(),
        unit)

    homeViewModel.getRemoteFiveDayThreeHourWeather(location.latitude.toString(),
        location.longitude.toString(),
        unit)

    val currentWeather = homeViewModel.currentWeather.collectAsStateWithLifecycle().value
    val fiveDayThreeHourWeather = homeViewModel.fiveDayThreeHourWeather.collectAsStateWithLifecycle().value
    val snackBarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {SnackbarHost(remember { SnackbarHostState() })},
        bottomBar = {MyBottomAppBar(navController)},
        containerColor = Color(0xFF182354)

    )
    { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(16.dp),
                verticalArrangement = Arrangement.Center
        )
        {

            if(currentWeather is Response.Success && fiveDayThreeHourWeather is Response.Success){
                HomeWeather(
                    currentWeather.data,
                    fiveDayThreeHourWeather.data)

            }else if(currentWeather is Response.Failure || fiveDayThreeHourWeather is Response.Failure){

                LaunchedEffect(currentWeather) {
                    snackBarHostState.showSnackbar(
                        message = (currentWeather as Response.Failure).error,
                        duration = SnackbarDuration.Short
                    )
                }
            }else if(currentWeather is Response.Loading || fiveDayThreeHourWeather is Response.Loading){
                Row(modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                    ) {

                    CircularProgressIndicator(modifier = Modifier.size(100.dp))

                }

            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showSystemUi = true)
@Composable
fun HomeWeather(currentWeatherResponse: CurrentWeatherResponse? = null,fiveDayThreeHourResponse: FiveDayThreeHourResponse? = null){

    val date = remember { formatDate(currentWeatherResponse?.dt,
        currentWeatherResponse?.timezone ?: 0
    ) }

    val countryName = remember { getCountryName(currentWeatherResponse?.sys?.country)}
    val sunrise = remember { convertUnixTimeToTime(currentWeatherResponse?.sys?.sunrise?.toLong() ?: 0,
        currentWeatherResponse?.timezone ?: 0
    ) }
    val sunset = remember { convertUnixTimeToTime(currentWeatherResponse?.sys?.sunset?.toLong() ?: 0,
        currentWeatherResponse?.timezone ?: 0
    ) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            ,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    )
    {
        item{
           Column(verticalArrangement = Arrangement.spacedBy(5.dp))
           {
               Row(modifier = Modifier
                   .fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceEvenly
               )
               {
                   Text(text = currentWeatherResponse?.weather?.get(0)?.description.toString(),
                       fontSize = 24.sp,
                       fontWeight = FontWeight.Bold,
                       color = Color.White)

                   GlideImage(model = "${WeatherIconLink.ICONLINK.link}${currentWeatherResponse?.weather?.get(0)?.icon}@4x.png", contentDescription = null)

                   Text(text = "Today",
                       fontSize = 24.sp,
                       fontWeight = FontWeight.Bold,
                       color = Color.White)
               }

               Row(modifier = Modifier
                   .fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceBetween
               )
               {
                   Text(text = "Feels like ${currentWeatherResponse?.weatherDetails?.feelsLike.toString()}°C",
                       fontSize = 14.sp,
                       fontWeight = FontWeight.Bold,
                       color = Color.White)

                   Text(text = date ,
                       fontSize = 14.sp,
                       fontWeight = FontWeight.Bold,
                       color = Color.White)
               }

               Row(modifier = Modifier.fillMaxWidth()
                   , horizontalArrangement = Arrangement.Center)
               {
                   Text(text = "${currentWeatherResponse?.weatherDetails?.temp.toString()}°C",
                       fontSize = 40.sp,
                       fontWeight = FontWeight.Bold,
                       color = Color.White)
               }

               Row(modifier = Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.Center)
               {
                   Text (text = "${countryName}, ${currentWeatherResponse?.cityName}",
                       color = Color.White)
               }

               Row(modifier = Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.Absolute.SpaceEvenly)
               {
                   Text("Sunrise $sunrise",color = Color.White)
                   Text("Sunset $sunset", color = Color.White)
               }
           }
        }

        item {
            Column {
                Text(
                    text = "Hourly Details",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)

                ) {
                    items(fiveDayThreeHourResponse?.list ?: emptyList()) { hourlyWeather ->
                        Card(modifier = Modifier
                            .width(140.dp)
                            .padding(10.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                            )
                            {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = convertUnixTimeToTime(
                                            hourlyWeather.dateAndTime.toLong(),
                                            fiveDayThreeHourResponse?.city?.timezone ?: 0
                                        ),
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                    GlideImage(
                                        model = "${WeatherIconLink.ICONLINK.link}${hourlyWeather.weather[0].icon}@2x.png",
                                        contentDescription = null,
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Text(
                                        text = "${hourlyWeather.main.temp.toString()}°C",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp) // Space between rows
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(text = "Pressure", fontSize = 20.sp, color = Color.White)
                        Text(text = "Wind Speed", fontSize = 20.sp, color = Color.White)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "${currentWeatherResponse?.weatherDetails?.pressure} hpa",
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Text(
                            text = "${currentWeatherResponse?.wind?.speed} m/s",
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(text = "Humidity", fontSize = 20.sp, color = Color.White)
                        Text(text = "Clouds", fontSize = 20.sp, color = Color.White)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = "${currentWeatherResponse?.weatherDetails?.humidity}%",
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Text(
                            text = "${currentWeatherResponse?.clouds?.all}%",
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        item{
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            )
            {
                Text(text = "Next Five Days",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White)
            }
        }

        items(fiveDayThreeHourResponse?.list?.filter {it.dateAndTimeAsString.endsWith("12:00:00")} ?: emptyList())
        {dailyWeather ->

            Card(modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            )
            {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween)
                {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    )
                    {
                        Text(text = getDayName(dailyWeather.dateAndTimeAsString),
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,)
                        Text(text = "Temp",
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,)
                        Text(text = "Feels Like",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,)
                        GlideImage(model = "${WeatherIconLink.ICONLINK.link}${dailyWeather.weather[0].icon}@3x.png", contentDescription = null)

                    }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly)
                    {
                        Text(text = formatDate(dailyWeather.dateAndTime,
                            fiveDayThreeHourResponse?.city?.timezone ?: 0
                        ), color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold)

                        Text(text = "${dailyWeather.main.temp}°C",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold)

                        Text(text = "${dailyWeather.main.feelsLike}°C",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}


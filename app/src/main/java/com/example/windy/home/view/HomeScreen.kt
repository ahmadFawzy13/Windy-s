package com.example.windy.home.view

import android.content.SharedPreferences
import android.location.Location
import android.util.Log
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
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.example.windy.home.viewmodel.HomeViewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.windy.R
import com.example.windy.Response
import com.example.windy.WeatherSettings
import com.example.windy.utils.SharedCityName
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.utils.NavBar
import com.example.windy.utils.WeatherIconLink
import com.example.windy.utils.convertUnixTimeToTime
import com.example.windy.utils.formatDate
import com.example.windy.utils.getCountryName
import com.example.windy.utils.getDayName

@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel, location:Location,unit:String="metric",favLat: String = "", favLon:String=""){

    val defaultLat = location.latitude.toString()
    val defaultLon = location.longitude.toString()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val weatherPrefs = remember { WeatherSettings.getInstance(context) }

    val appTempUnit = when(weatherPrefs.getTemperatureUnit()){
            "c" -> "metric"
            "k" -> "standard"
            "f" -> "imperial"
             else -> "metric"
    }

    val appLanguage = when(weatherPrefs.getAppLanguage()){
        "en" -> "en"
        "ar" -> "ar"
        else -> "en"
    }

    val layoutDirection = if (weatherPrefs.getAppLanguage() == "ar") {
        LayoutDirection.Rtl
    } else {
        LayoutDirection.Ltr
    }

        if((favLat.isNotEmpty() && favLon.isNotEmpty())){
            homeViewModel.getRemoteCurrentWeather(favLat,
                favLon,
                appTempUnit,
                appLanguage)

            homeViewModel.getRemoteFiveDayThreeHourWeather(favLat,
                favLon,
                appTempUnit,
                appLanguage)
        }else{
            homeViewModel.getRemoteCurrentWeather(defaultLat,
                defaultLon,
                appTempUnit,
                appLanguage)

            homeViewModel.getRemoteFiveDayThreeHourWeather(defaultLat,
                defaultLon,
                appTempUnit,
                appLanguage)
        }

    val currentWeather = homeViewModel.currentWeather.collectAsStateWithLifecycle().value
    val fiveDayThreeHourWeather = homeViewModel.fiveDayThreeHourWeather.collectAsStateWithLifecycle().value

    LaunchedEffect(currentWeather) {
        when{
            currentWeather is Response.Message ->{
                snackBarHostState.showSnackbar(
                    message = currentWeather.msg,
                    duration = SnackbarDuration.Short
                )
            }

            fiveDayThreeHourWeather is Response.Message -> {
                snackBarHostState.showSnackbar(
                    message = fiveDayThreeHourWeather.msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackBarHostState) },
            bottomBar = { NavBar(navController) },
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
                when {
                    currentWeather is Response.Success &&
                            fiveDayThreeHourWeather is Response.Success -> {
                        HomeWeather(currentWeather.data, fiveDayThreeHourWeather.data)
                    }

                    currentWeather is Response.Loading ||
                            fiveDayThreeHourWeather is Response.Loading -> {

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            CircularProgressIndicator(modifier = Modifier.size(100.dp))

                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showSystemUi = true)
@Composable
fun HomeWeather(currentWeatherResponse: CurrentWeatherResponse? = null,fiveDayThreeHourResponse: FiveDayThreeHourResponse? = null){

    val date = formatDate(currentWeatherResponse?.dt)
    val context = LocalContext.current
    val weatherPrefs = remember { WeatherSettings.getInstance(context) }
    val countryName = getCountryName(currentWeatherResponse?.sys?.country)
    var windSpeed = currentWeatherResponse?.wind?.speed
    var windUnit = ""
    val sunrise = convertUnixTimeToTime(currentWeatherResponse?.sys?.sunrise?.toLong() ?: 0)
    val sunset = convertUnixTimeToTime(currentWeatherResponse?.sys?.sunset?.toLong() ?: 0)

    if((weatherPrefs.getTemperatureUnit() == "c" || weatherPrefs.getTemperatureUnit() == "k") && weatherPrefs.getWindSpeedUnit() == "m/h"){
        windUnit = "mph"
         windSpeed = currentWeatherResponse?.wind?.speed?.times(2.23694)

    }else if (weatherPrefs.getTemperatureUnit() == "f" && weatherPrefs.getWindSpeedUnit() == "m/s"){
        windUnit = "m/s"
        windSpeed = currentWeatherResponse?.wind?.speed?.times(0.44704)
    }else if(weatherPrefs.getTemperatureUnit() == "f"){
        windUnit = "mph"
    }
    else {
        windUnit = "m/s"
    }
   var tempUnit =  when(weatherPrefs.getTemperatureUnit()){
        "c" -> "°C"
        "k" -> "k"
        "f" ->"°F"
       else -> "°C"
   }
    val layoutDirection = if (weatherPrefs.getAppLanguage() == "ar") {
        LayoutDirection.Rtl
    } else {
        LayoutDirection.Ltr
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(5.dp))
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    )
                    {
                        Text(
                            text = currentWeatherResponse?.weather?.get(0)?.description.toString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        GlideImage(
                            model = "${WeatherIconLink.ICONLINK.link}${
                                currentWeatherResponse?.weather?.get(
                                    0
                                )?.icon
                            }@4x.png", contentDescription = null
                        )

                        Text(
                            text = stringResource(R.string.today),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Text(
                            text = "Feels like ${currentWeatherResponse?.weatherDetails?.feelsLike.toString()}°C",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = date,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    )
                    {
                        Text(
                            text = "${currentWeatherResponse?.weatherDetails?.temp}$tempUnit",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    )
                    {
                        Text(
                            text = "${countryName}, ${currentWeatherResponse?.cityName}",
                            color = Color.White
                        )
                        SharedCityName.cityName = currentWeatherResponse?.cityName.toString()
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Absolute.SpaceEvenly
                    )
                    {
                        Text("Sunrise $sunrise", color = Color.White)
                        Text("Sunset $sunset", color = Color.White)
                    }
                }
            }

            item {
                Column {
                    Text(
                        text = stringResource(R.string.hourly_details),
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
                            Card(
                                modifier = Modifier
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
                                            hourlyWeather.dateAndTime.toLong()
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
                                        text = "${hourlyWeather.main.temp}$tempUnit",
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
                            Text(
                                text = stringResource(R.string.pressure),
                                fontSize = 20.sp,
                                color = Color.White
                            )
                            Text(
                                text = stringResource(R.string.wind_speed),
                                fontSize = 20.sp,
                                color = Color.White
                            )
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
                                text = "$windSpeed $windUnit",
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = stringResource(R.string.humidity),
                                fontSize = 20.sp,
                                color = Color.White
                            )
                            Text(
                                text = stringResource(R.string.clouds),
                                fontSize = 20.sp,
                                color = Color.White
                            )
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

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                )
                {
                    Text(
                        text = stringResource(R.string.next_five_days),
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            items(fiveDayThreeHourResponse?.list?.filter { it.dateAndTimeAsString.endsWith("12:00:00") }
                ?: emptyList())
            { dailyWeather ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    )
                    {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        )
                        {
                            Text(
                                text = getDayName(dailyWeather.dateAndTimeAsString),
                                fontSize = 20.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = stringResource(R.string.temp),
                                fontSize = 20.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = stringResource(R.string.feels_like),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            )
                            GlideImage(
                                model = "${WeatherIconLink.ICONLINK.link}${dailyWeather.weather[0].icon}@3x.png",
                                contentDescription = null
                            )

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        )
                        {
                            Text(
                                text = formatDate(dailyWeather.dateAndTime), color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "${dailyWeather.main.temp}$tempUnit",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "${dailyWeather.main.feelsLike}°C",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}


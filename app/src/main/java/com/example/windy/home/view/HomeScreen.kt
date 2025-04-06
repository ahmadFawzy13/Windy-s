package com.example.windy.home.view

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.windy.R
import com.example.windy.data.model.HomeDetails
import com.example.windy.data.model.Response
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.home.viewmodel.HomeViewModel
import com.example.windy.utils.SharedCityName
import com.example.windy.utils.WeatherIconLink
import com.example.windy.utils.WeatherSettings
import com.example.windy.utils.convertNumberToAppLanguage
import com.example.windy.utils.convertUnixTimeToTime
import com.example.windy.utils.formatDate
import com.example.windy.utils.getCountryName
import com.example.windy.utils.getDayName

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    location: Location,
    favLat: String = "",
    favLon: String = "",
    isNetworkAvailable:Boolean?
) {
    homeViewModel.getLocalHomeDetails()
    Log.i("TAG", "HomeScreen: $isNetworkAvailable")
    val context = LocalContext.current
    val weatherPrefs = remember { WeatherSettings.getInstance(context) }
    var defaultLat by remember { mutableStateOf("") }
    var defaultLon by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {

        defaultLat = when (weatherPrefs.getLocationPref()) {
            "map" -> {
                weatherPrefs.getSettingLatitude()
            }

            else -> location.latitude.toString()
        }

        defaultLon = when (weatherPrefs.getLocationPref()) {
            "map" -> {
                weatherPrefs.getSettingsLongitude()
            }

            else -> location.longitude.toString()
        }
    }


    val appTempUnit = when (weatherPrefs.getTemperatureUnit()) {
        "c" -> "metric"
        "k" -> "standard"
        "f" -> "imperial"
        else -> "metric"
    }

    val appLanguage = when (weatherPrefs.getAppLanguage()) {
        "en" -> "en"
        "ar" -> "ar"
        else -> "en"
    }

    if ((favLat.isNotEmpty() && favLon.isNotEmpty())) {
        homeViewModel.getRemoteCurrentWeather(
            favLat,
            favLon,
            appTempUnit,
            appLanguage
        )

        homeViewModel.getRemoteFiveDayThreeHourWeather(
            favLat,
            favLon,
            appTempUnit,
            appLanguage
        )
    } else {
        homeViewModel.getRemoteCurrentWeather(
            defaultLat,
            defaultLon,
            appTempUnit,
            appLanguage
        )

        homeViewModel.getRemoteFiveDayThreeHourWeather(
            defaultLat,
            defaultLon,
            appTempUnit,
            appLanguage
        )
    }

    val currentWeather = homeViewModel.currentWeather.collectAsStateWithLifecycle().value
    val fiveDayThreeHourWeather =
        homeViewModel.fiveDayThreeHourWeather.collectAsStateWithLifecycle().value
    val localHomeDetails = homeViewModel.homeScreenLocal.collectAsStateWithLifecycle().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    )
    {
        if(isNetworkAvailable == true){
            when {
                currentWeather is Response.Success &&
                        fiveDayThreeHourWeather is Response.Success -> {
                    val homeDetails = HomeDetails(currentWeatherResponse = currentWeather.data,fiveDayThreeHourResponse= fiveDayThreeHourWeather.data)
                    homeViewModel.insertHomeDetails(homeDetails)
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
        }else if(isNetworkAvailable == false){
            when {
                localHomeDetails is Response.Success -> {
                    HomeWeather(localHomeDetails.data.currentWeatherResponse, localHomeDetails.data.fiveDayThreeHourResponse)
                }

                localHomeDetails is Response.Loading -> {
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

@OptIn(ExperimentalGlideComposeApi::class)
@Preview(showSystemUi = true)
@Composable
fun HomeWeather(
    currentWeatherResponse: CurrentWeatherResponse? = null,
    fiveDayThreeHourResponse: FiveDayThreeHourResponse? = null
) {

    val date = formatDate(currentWeatherResponse?.dt)
    val context = LocalContext.current
    val weatherPrefs = remember { WeatherSettings.getInstance(context) }
    val countryName = getCountryName(currentWeatherResponse?.sys?.country)
    var windSpeed = currentWeatherResponse?.wind?.speed
    var windUnit = ""
    var isArabic = weatherPrefs.getAppLanguage() == "ar"
    val sunrise = convertUnixTimeToTime(currentWeatherResponse?.sys?.sunrise?.toLong() ?: 0)
    val sunset = convertUnixTimeToTime(currentWeatherResponse?.sys?.sunset?.toLong() ?: 0)

    if ((weatherPrefs.getTemperatureUnit() == "c" || weatherPrefs.getTemperatureUnit() == "k") && weatherPrefs.getWindSpeedUnit() == "m/h") {
        windUnit = stringResource(R.string.mph)
        windSpeed = currentWeatherResponse?.wind?.speed?.times(2.23694)

    } else if (weatherPrefs.getTemperatureUnit() == "f" && weatherPrefs.getWindSpeedUnit() == "m/s") {
        windUnit = stringResource(R.string.m_s)
        windSpeed = currentWeatherResponse?.wind?.speed?.times(0.44704)
    } else if (weatherPrefs.getTemperatureUnit() == "f") {
        windUnit = stringResource(R.string.mph)
    } else {
        windUnit = stringResource(R.string.m_s)
    }
    var tempUnit = when (weatherPrefs.getTemperatureUnit()) {
        "c" -> stringResource(R.string.c)
        "k" -> stringResource(R.string.k)
        "f" -> stringResource(R.string.f)
        else -> stringResource(R.string.c)
    }



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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text(
                        text = currentWeatherResponse?.weather?.get(0)?.description.toString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Text(
                        text = stringResource(R.string.today),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                )
                {

                    GlideImage(
                        model = "${WeatherIconLink.ICONLINK.link}${
                            currentWeatherResponse?.weather?.get(
                                0
                            )?.icon
                        }@4x.png", contentDescription = null
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Text(
                        text = stringResource(
                            R.string.feels_like_c,
                            convertNumberToAppLanguage(
                                currentWeatherResponse?.weatherDetails?.feelsLike.toString(),
                                context
                            )
                        ),
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
                        text = convertNumberToAppLanguage(
                            "${currentWeatherResponse?.weatherDetails?.temp}",
                            context
                        ) + tempUnit,
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
                    Text(stringResource(R.string.sunrise, sunrise), color = Color.White)
                    Text(stringResource(R.string.sunset, sunset), color = Color.White)
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
                                    text = convertNumberToAppLanguage(
                                        "${hourlyWeather.main.temp}",
                                        context
                                    ) + tempUnit,
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
                    verticalArrangement = Arrangement.spacedBy(10.dp)
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
                            text = convertNumberToAppLanguage(
                                "${currentWeatherResponse?.weatherDetails?.pressure}",
                                context
                            ) + stringResource(
                                R.string.hpa
                            ),
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Text(
                            text = convertNumberToAppLanguage("$windSpeed", context) + windUnit,
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
                            text = convertNumberToAppLanguage(
                                "${currentWeatherResponse?.weatherDetails?.humidity}",
                                context
                            ) + " %",
                            fontSize = 15.sp,
                            color = Color.White
                        )
                        Text(
                            text = convertNumberToAppLanguage(
                                "${currentWeatherResponse?.clouds?.all}",
                                context
                            ) + " %",
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
                            text = getDayName(dailyWeather.dateAndTimeAsString, isArabic),
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
                            text = convertNumberToAppLanguage(
                                "${dailyWeather.main.temp}",
                                context
                            ) + tempUnit,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = convertNumberToAppLanguage(
                                "${dailyWeather.main.feelsLike}",
                                context
                            ) + tempUnit,
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


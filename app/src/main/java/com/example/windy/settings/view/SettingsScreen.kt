package com.example.windy.settings.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.windy.R
import com.example.windy.WeatherSettings
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.windy.NavigationRoute
import com.example.windy.Response
import com.example.windy.favourite.viewmodel.FavViewModel
import com.example.windy.utils.systemLanguage
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val weatherPrefs = remember { WeatherSettings.getInstance(context) }
    val activity = context as Activity

    val radioOptions = listOf(stringResource(R.string.arabic),
        stringResource(R.string.english), stringResource(R.string.def)
    )
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(
        when(weatherPrefs.getAppLanguage()){
            "ar" -> radioOptions[0]
            "en" -> radioOptions[1]
            "Default"-> radioOptions[2]
            else -> ""
        }
    )}

    val arText = stringResource(R.string.arabic)
    val enText = stringResource(R.string.english)
    val defText = stringResource(R.string.def)

    val radioOptions2 = listOf(stringResource(R.string.celsius_c),
        stringResource(R.string.fahrenheit_f), stringResource(R.string.kelvin_k)
    )
    val (selectedOption2, onOptionSelected2) = remember {mutableStateOf(
        when(weatherPrefs.getTemperatureUnit()){
            "c" -> radioOptions2[0]
            "f" -> radioOptions2[1]
            "k" -> radioOptions2[2]
            else -> ""
        }
    )}

    val celsiusText = stringResource(R.string.celsius_c)
    val fahrenheitText = stringResource(R.string.fahrenheit_f)
    val kelvinText = stringResource(R.string.kelvin_k)

    val radioOptions3 = listOf(stringResource(R.string.gps), stringResource(R.string.map))
    val (selectedOption3, onOptionSelected3) = remember {mutableStateOf(
        when(weatherPrefs.getLocationPref()){
            "gps" -> radioOptions3[0]
            "map" -> radioOptions3[1]
            else -> ""
        }
    )}

    val gpsText = stringResource(R.string.gps)
    val mapText = stringResource(R.string.map)


    val radioOptions4 = listOf(stringResource(R.string.m_s),stringResource(R.string.mph))
    val (selectedOption4, onOptionSelected4) = remember {mutableStateOf(
        when(weatherPrefs.getWindSpeedUnit()){
            "m/s" -> radioOptions4[0]
            "m/h" -> radioOptions4[1]
            else -> ""
        }
    )}

    val msText = stringResource(R.string.m_s)
    val mphText = stringResource(R.string.mph)



    val layoutDirection = if (weatherPrefs.getAppLanguage() == "ar") {
        LayoutDirection.Rtl
    } else {
        LayoutDirection.Ltr
    }



    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column() {
                    Card(
                        modifier = Modifier
                            .padding(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                    ) {
                        Spacer(modifier = Modifier.padding(5.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.padding(5.dp))

                            Icon(
                                painter = painterResource(id = R.drawable.language),
                                contentDescription = "language",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )

                            Spacer(modifier = Modifier.padding(8.dp))

                            Text(
                                text = stringResource(R.string.language),
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        }

                        Row(modifier = Modifier.selectableGroup()) {
                            radioOptions.forEach { text ->
                                Column(
                                    Modifier
                                        .padding(horizontal = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = text,
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                    RadioButton(
                                        selected = (text == selectedOption && selectedOption.isNotEmpty()),
                                        onClick = {
                                            onOptionSelected(text)

                                            when (text) {
                                                arText -> {
                                                    weatherPrefs.setAppLanguage(
                                                        "ar"
                                                    )
                                                    activity.recreate()
                                                }

                                                enText -> {
                                                    weatherPrefs.setAppLanguage(
                                                        "en"
                                                    )
                                                    activity.recreate()
                                                }

                                                defText -> {
                                                    Log.i("TAG", "SettingsScreen: System Language ${systemLanguage()}")
                                                    weatherPrefs.setAppLanguage(systemLanguage())
                                                    activity.recreate()
                                                }
                                            }

                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                    }

                    Card(
                        modifier = Modifier
                            .padding(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                    ) {
                        Spacer(modifier = Modifier.padding(5.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.padding(5.dp))

                            Icon(
                                painter = painterResource(id = R.drawable.temp),
                                contentDescription = "temp",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )

                            Spacer(modifier = Modifier.padding(8.dp))

                            Text(
                                text = stringResource(R.string.temperature),
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        }

                        Row(modifier = Modifier.selectableGroup()) {
                            radioOptions2.forEach { text ->
                                Column(
                                    Modifier
                                        .padding(horizontal = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = text,
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                    RadioButton(
                                        selected = (text == selectedOption2 && selectedOption2.isNotEmpty()),
                                        onClick = {
                                            onOptionSelected2(text)
                                            when (text) {
                                                celsiusText -> weatherPrefs.setTemperatureUnit("c")
                                                fahrenheitText -> weatherPrefs.setTemperatureUnit("f")
                                                kelvinText -> weatherPrefs.setTemperatureUnit("k")
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                    }


                    Card(
                        modifier = Modifier
                            .padding(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                    ) {
                        Spacer(modifier = Modifier.padding(5.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.padding(5.dp))

                            Icon(
                                painter = painterResource(id = R.drawable.location),
                                contentDescription = "temp",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )

                            Spacer(modifier = Modifier.padding(8.dp))

                            Text(
                                text = stringResource(R.string.location),
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        }

                        Row(modifier = Modifier.selectableGroup()) {
                            radioOptions3.forEach { text ->
                                Column(
                                    Modifier
                                        .padding(horizontal = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = text,
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                    RadioButton(
                                        selected = (text == selectedOption3 && selectedOption3.isNotEmpty()),
                                        onClick = {
                                            onOptionSelected3(text)
                                            when (text) {
                                                gpsText -> {
                                                    weatherPrefs.setLocationPref("gps")
                                                }
                                                mapText -> {
                                                    weatherPrefs.setLocationPref("map")
                                                    navController.navigate(NavigationRoute.MapSettings)
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                    }


                    Card(
                        modifier = Modifier
                            .padding(10.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                    ) {
                        Spacer(modifier = Modifier.padding(5.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.padding(5.dp))

                            Icon(
                                painter = painterResource(id = R.drawable.wind),
                                contentDescription = "wind",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )

                            Spacer(modifier = Modifier.padding(8.dp))

                            Text(
                                text = stringResource(
                                    R.string.wind_speed
                                ),
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 20.sp
                            )
                        }

                        Row(modifier = Modifier.selectableGroup()) {
                            radioOptions4.forEach { text ->
                                Column(
                                    Modifier
                                        .padding(horizontal = 16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = text,
                                        color = Color.White,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                    RadioButton(
                                        selected = (text == selectedOption4 && selectedOption4.isNotEmpty()),
                                        onClick = {
                                            onOptionSelected4(text)

                                            when (text) {
                                                msText -> weatherPrefs.setWindSpeedUnit("m/s")
                                                mphText -> weatherPrefs.setWindSpeedUnit("m/h")
                                            }

                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(5.dp))
                    }
                }
            }
        }
}

@Composable
fun MapScreenSettings(favViewModel: FavViewModel) {

    val searchPlaceCoordinates = favViewModel.searchPlaceCoordinates.
    collectAsStateWithLifecycle().value

    var searchText by remember { mutableStateOf("") }
    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }

    val context = LocalContext.current.applicationContext
    val sharedPrefs = WeatherSettings.getInstance(context)

    if (!Places.isInitialized()) {
        Places.initialize(context, "AIzaSyCvScI24VvzEEkv6EJQCQw7Ovv-_jha5GA")
    }

    val places = remember { Places.createClient(context) }

    favViewModel.getPlaceOnMap(searchText,places)

    Box(modifier = Modifier.fillMaxSize()) {
        LocationPickerMap(
            selectedLocation = selectedLatLng,
            onLocationSelected = { latLng ->
                selectedLatLng = latLng
            }
        )


        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(onGo = {

                if(searchPlaceCoordinates is Response.Success){
                    selectedLatLng   = searchPlaceCoordinates.data
                }

            }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
                .padding(16.dp),
            label = { Text("Search for a place...") },
            singleLine = true
        )

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
                            sharedPrefs.setSettingLatitude(selectedLatLng?.latitude.toString())
                            sharedPrefs.setSettingsLongitude(selectedLatLng?.longitude.toString())
                        },
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    ) {
                        Icon(painterResource(R.drawable.locarrow), contentDescription = "Select Location",
                            tint = Color.White)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(stringResource(R.string.select_location), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun LocationPickerMap(
    selectedLocation :LatLng?,
    onLocationSelected: (LatLng) -> Unit
) {

    val defaultLocation = remember {LatLng(30.0381736, 30.9793528) }

    var currentLocation = selectedLocation ?: defaultLocation

    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 10f)
    }

    LaunchedEffect(currentLocation) {
        selectedLocation?.let {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(currentLocation,10f)
            )
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            onLocationSelected(latLng)
        }
    ) {

        selectedLocation?.let { location ->
            Marker(
                state = MarkerState(position = location),
                title = "Weather Location"
            )
        }
    }
}
package com.example.windy.settings.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.windy.R
import com.example.windy.Response
import com.example.windy.WeatherSettings
import com.example.windy.alarm.view.SetAlarm
import com.example.windy.alarm.viewmodel.AlarmViewModel
import com.example.windy.data.model.Alarm
import com.example.windy.utils.AlarmScheduler
import com.example.windy.utils.NavBar
import com.example.windy.utils.RadioButtonSingleSelection
import com.example.windy.utils.SharedCityName
import android.R.attr.onClick
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.windy.NavigationRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val weatherPrefs = remember { WeatherSettings.getInstance(context) }

    val radioOptions = listOf("Arabic", "English", "Default")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(
        when(weatherPrefs.getAppLanguage()){
            "ar" -> radioOptions[0]
            "en" -> radioOptions[1]
            "Default"-> radioOptions[2]
            else -> ""
        }
    )}

    val radioOptions2 = listOf("Celsius °C","Fahrenheit °F","Kelvin K")
    val (selectedOption2, onOptionSelected2) = remember {mutableStateOf(
        when(weatherPrefs.getTemperatureUnit()){
            "c" -> radioOptions2[0]
            "f" -> radioOptions2[1]
            "k" -> radioOptions2[2]
            else -> ""
        }
    )}

    val radioOptions3 = listOf("Gps","Map")
    val (selectedOption3, onOptionSelected3) = remember {mutableStateOf(
        when(weatherPrefs.getLocationPref()){
            "gps" -> radioOptions3[0]
            "map" -> radioOptions3[1]
            else -> ""
        }
    )}

    val radioOptions4 = listOf("m/s","m/h")
    val (selectedOption4, onOptionSelected4) = remember {mutableStateOf(
        when(weatherPrefs.getWindSpeedUnit()){
            "m/s" -> radioOptions4[0]
            "m/h" -> radioOptions4[1]
            else -> ""
        }
    )}


    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = { NavBar(navController) },
        containerColor = Color(0xFF182354),
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
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
                            text = "Language",
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
                                            "Arabic" -> weatherPrefs.setAppLanguage(context,"ar")
                                            "English" -> weatherPrefs.setAppLanguage(context,"en")
                                            "Default" -> println("Calls selected")
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
                            text = "Temperature",
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
                                            "Celsius °C"-> weatherPrefs.setTemperatureUnit("c")
                                            "Fahrenheit °F"-> weatherPrefs.setTemperatureUnit("f")
                                            "Kelvin K"-> weatherPrefs.setTemperatureUnit("k")
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
                            text = "Location",
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
                                            "Gps"-> weatherPrefs.setLocationPref("gps")
                                            "Map"-> weatherPrefs.setLocationPref("map")
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
                            text = "Wind Speed",
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
                                            "m/s"-> weatherPrefs.setWindSpeedUnit("m/s")
                                            "m/h"-> weatherPrefs.setWindSpeedUnit("m/h")
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
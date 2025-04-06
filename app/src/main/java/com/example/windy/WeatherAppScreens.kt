package com.example.windy

import android.location.Location
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.windy.alarm.view.AlarmScreen
import com.example.windy.alarm.view.SetAlarm
import com.example.windy.alarm.viewmodel.MyAlarmFactory
import com.example.windy.data.repo.Repository
import com.example.windy.favourite.view.FavouriteScreen
import com.example.windy.favourite.view.MapScreen
import com.example.windy.favourite.viewmodel.MyFavFactory
import com.example.windy.home.view.HomeScreen
import com.example.windy.home.viewmodel.MyHomeFactory
import com.example.windy.navigation.NavigationRoute
import com.example.windy.settings.view.MapScreenSettings
import com.example.windy.settings.view.SettingsScreen
import com.example.windy.utils.NavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppScreens(locationState: MutableState<Location>, isNetworkAvailable:Boolean?){

    val context = LocalContext.current
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    var floatingActionButtonAction : MutableState<(()->Unit)?> = remember { mutableStateOf(null) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = { NavBar(navController) },
        containerColor = Color(0xFF182354),
        floatingActionButton = {
            if(floatingActionButtonAction.value!=null){
                ExtendedFloatingActionButton(
                    onClick = {
                        floatingActionButtonAction.value?.invoke()
                    },
                    icon = { Icon(Icons.Filled.AddCircle, "Add") },
                    text = {
                        Text(text = stringResource(R.string.add), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    },
                    containerColor = Color.White
                )
            }
        }
    ){paddingContent->

        LaunchedEffect(isNetworkAvailable) {
            when(isNetworkAvailable){
                true -> {}
                false -> {snackBarHostState.showSnackbar(
                    message = context.getString(R.string.windy_is_offline),
                    duration = SnackbarDuration.Long
                )}
                else -> {}
            }
        }

        NavHost(navController = navController,
            startDestination = NavigationRoute.Home,
            modifier = Modifier.padding(paddingContent)) {

            composable<NavigationRoute.HomeFav>{ params->
                floatingActionButtonAction.value = null
                val data =params.toRoute<NavigationRoute.HomeFav>()
                val favLat = data.favLat
                val favLon = data.favLon

                HomeScreen(viewModel(factory = MyHomeFactory(Repository.getInstance(context))),
                    locationState.value,
                    favLat,favLon,isNetworkAvailable = isNetworkAvailable)
            }

            composable <NavigationRoute.Home>{ params ->
                floatingActionButtonAction.value = null
                HomeScreen(viewModel(factory = MyHomeFactory(Repository.getInstance(context))),
                    locationState.value, isNetworkAvailable = isNetworkAvailable)

            }

            composable <NavigationRoute.Favourite>{
                FavouriteScreen(navController,viewModel(factory = MyFavFactory(Repository.getInstance(context))),floatingActionButtonAction,snackBarHostState)
            }

            composable<NavigationRoute.Map> {
                floatingActionButtonAction.value = null
                MapScreen(viewModel(factory = MyFavFactory(Repository.getInstance(context))))
            }

            composable <NavigationRoute.Alarm> {
                AlarmScreen(viewModel(factory = MyAlarmFactory(Repository.getInstance(context))),floatingActionButtonAction)
            }

            composable <NavigationRoute.SetAlarm> {
                floatingActionButtonAction.value = null
                SetAlarm()
            }
            composable <NavigationRoute.Settings> {
                floatingActionButtonAction.value = null

                SettingsScreen(navController)
            }
            composable <NavigationRoute.MapSettings> {
                floatingActionButtonAction.value = null
                MapScreenSettings(viewModel(factory = MyFavFactory(Repository.getInstance(context))))
            }
        }
    }
}
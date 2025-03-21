package com.example.windy.home.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.windy.home.viewmodel.HomeViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import com.example.windy.utils.MyBottomAppBar

@Preview(showSystemUi = true)
@Composable
fun HomeScreen(navController: NavController? = null, homeViewModel: HomeViewModel? = null){

    val currentWeather = homeViewModel?.currentWeather?.observeAsState()
    val fiveDayThreeHourWeather = homeViewModel?.fiveDayThreeHourWeather?.observeAsState()
    val responseMessage = homeViewModel?.responseMessage?.observeAsState()

    Scaffold(
        snackbarHost = {SnackbarHost(remember { SnackbarHostState() })},
        bottomBar = {MyBottomAppBar(navController)}

    )
    {contentPadding ->

        Text(modifier = Modifier.padding(contentPadding), text = "Home")

    }
}


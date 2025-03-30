package com.example.windy.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.windy.NavigationRoute

/*@Composable
fun MyBottomAppBar(navController: NavController?) {
        BottomAppBar(
            containerColor = Color(0xFF182354),
            actions = {
                IconButton(onClick = { navController?.navigate(NavigationRoute.Home) }) {
                    Icon(imageVector = Icons.Filled.Home, contentDescription = "Home",
                        tint = Color.White)
                }
                IconButton(onClick = { navController?.navigate(NavigationRoute.Favourite) }) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Fav",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { navController?.navigate(NavigationRoute.Home) }) {
                    Icon(
                        Icons.Filled.Notifications,
                        contentDescription = "Alert",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { navController?.navigate(NavigationRoute.Home) }) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }
        )
}

@Composable
fun MyBottomAppBarWithFab(navController: NavController?) {
    BottomAppBar(
        containerColor = Color(0xFF182354),
        actions = {
            IconButton(onClick = { navController?.navigate(NavigationRoute.Home) }) {
                Icon(Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = Color.White)
            }
            IconButton(onClick = { navController?.navigate(NavigationRoute.Favourite) }) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Fav",
                    tint = Color.White
                )
            }
            IconButton(onClick = { navController?.navigate(NavigationRoute.Home) }) {
                Icon(
                    Icons.Filled.Notifications,
                    contentDescription = "Alert",
                    tint = Color.White
                )
            }
            IconButton(onClick = { navController?.navigate(NavigationRoute.Home) }) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = Color.White
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController?.navigate(NavigationRoute.Map) },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Outlined.LocationOn, "Maps")
            }
        }
    )
}*/

/*@Composable
fun FloatingActionMap(navController: NavController){

        ExtendedFloatingActionButton(
            onClick = {
                navController.navigate(NavigationRoute.Map)
            },
            icon = { Icon(Icons.Filled.LocationOn, "Maps") },
            text = {
                Text(text = "Maps", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            },
            containerColor = Color.White
        )

}*/

/*
@Composable
fun FloatingActionAlarm(navController: NavController){

    ExtendedFloatingActionButton(
        onClick = {
            navController.navigate(NavigationRoute.SetAlarm)
        },
        icon = { Icon(Icons.Filled.LocationOn, "Alarm") },
        text = {
            Text(text = "Set Alarm", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        },
        containerColor = Color.White
    )

}*/

@Composable
fun NavBar(navController: NavController){

    var selectedItem by remember { mutableIntStateOf(0) }

    val items = listOf("Home",
        "Favourite",
        "Alert",
        "Settings")

    val selectedIcons = listOf(Icons.Filled.Home,
        Icons.Filled.Favorite,
        Icons.Filled.Notifications,
        Icons.Filled.Settings)

    val unselectedIcons = listOf(Icons.Outlined.Home,
        Icons.Outlined.FavoriteBorder,
        Icons.Outlined.Notifications,
        Icons.Outlined.Settings)

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item
                    )
                },
                selected = selectedItem == index,
                onClick = {

                    selectedItem = index

                    when(selectedItem){
                         0 -> navController.navigate(NavigationRoute.Home)
                         1 -> navController.navigate(NavigationRoute.Favourite)
                         2 -> navController.navigate(NavigationRoute.Alarm)
                         3-> navController.navigate(NavigationRoute.Settings)
                    }
                }
            )
        }
    }
}

@Composable
fun RadioButtonSingleSelection(modifier: Modifier = Modifier,option1:String,option2: String,option3: String) {
    val radioOptions = listOf(option1,option2,option3)
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Row(modifier.selectableGroup()) {
        radioOptions.forEach { text ->
            Column(
                Modifier
                    .fillMaxHeight()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null
                )
            }
        }
    }
}
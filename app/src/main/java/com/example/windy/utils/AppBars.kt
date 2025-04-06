package com.example.windy.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.windy.navigation.NavigationRoute
import com.example.windy.R

@Composable
fun NavBar(navController: NavController){

    var selectedItem by remember { mutableIntStateOf(0) }

    val items = listOf(
        stringResource(R.string.home),
        stringResource(R.string.favourite),
        stringResource(R.string.alert),
        stringResource(R.string.settings)
    )

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
                label = {Text(item)},
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
fun HandleNegativeAction(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Warning")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

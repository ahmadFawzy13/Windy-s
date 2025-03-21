package com.example.windy

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LocationUi(currentLocation: MutableState<String?>, location:Location){

    Column(modifier = Modifier.padding(top = 100.dp, start = 20.dp)){
        Text(text = "Longitude is: ${location.longitude}")
        Text(text = "Latitude is: ${location.latitude}")
        Text(text = currentLocation.value?:"")
    }
}
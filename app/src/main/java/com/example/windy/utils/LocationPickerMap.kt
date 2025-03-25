package com.example.windy.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun LocationPickerMap(
    onLocationSelected: (LatLng) -> Unit // Callback when user taps map
) {
    // Default position (e.g., Singapore)
    val defaultLocation = remember { LatLng(30.0381736, 30.9793528) }

    // Camera position state
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 10f)
    }

    // Selected marker
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            selectedLocation = latLng
            onLocationSelected(latLng) // Return coordinates
        }
    ) {
        // Add marker if a location is selected
        selectedLocation?.let { location ->
            Marker(
                state = MarkerState(position = location),
                title = "Weather Location"
            )
        }
    }
}

@Composable
fun WeatherScreen() {
    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }

    LocationPickerMap { latLng ->
        selectedLatLng = latLng
        // Fetch weather data using latLng.latitude/longitude
    }

    // Display selected coordinates
    selectedLatLng?.let { latLng ->
        Text(
            text = "Selected: ${latLng.latitude}, ${latLng.longitude}",
            modifier = Modifier.padding(16.dp)
        )
    }
}


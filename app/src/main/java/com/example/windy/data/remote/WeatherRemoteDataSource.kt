package com.example.windy.data.remote

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class WeatherRemoteDataSource private constructor(private val service: WeatherApi){


    suspend fun getCurrentWeatherRemote(lat:String, lon:String, units:String): Flow<CurrentWeatherResponse> {
        return flowOf(service.getCurrentWeather(lat = lat,lon = lon, units = units))
    }

    suspend fun getFiveDayThreeHourWeatherRemote(lat: String, lon: String, units: String): Flow<FiveDayThreeHourResponse> {
        return flowOf(service.getFiveDayThreeHourForecast(lat = lat, lon = lon, units = units))
    }

    suspend fun getPlaceOnMap(searchText: String, placesClient: PlacesClient) : Flow<LatLng>{

        var placeCoordinates : LatLng = LatLng(20.0,20.0)
        // 1. Find predictions
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(searchText)
            .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                // Take the first prediction
                response.autocompletePredictions.firstOrNull()?.let { prediction ->
                    // 2. Get place details
                    val receivedInfo = listOf(Place.Field.LAT_LNG)
                    val placeRequest = FetchPlaceRequest.builder(
                        prediction.placeId,
                        receivedInfo
                    ).build()

                    placesClient.fetchPlace(placeRequest)
                        .addOnSuccessListener { placeResponse ->
                            placeResponse.place.latLng?.let { latLng ->
                                Log.i("TAG", "getPlaceOnMap: $latLng")
                                placeCoordinates = latLng
                            }
                        }
                }
            }
        delay(1000)
        return flowOf(placeCoordinates)
    }

    companion object {
        @Volatile
        private var INSTANCE: WeatherRemoteDataSource? = null

        fun getInstance(): WeatherRemoteDataSource{

            return INSTANCE?: synchronized(this){

                val instance = WeatherRemoteDataSource(RetroFitHelper.apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}
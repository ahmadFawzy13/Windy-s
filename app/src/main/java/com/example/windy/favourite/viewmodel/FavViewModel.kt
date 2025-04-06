package com.example.windy.favourite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.windy.data.model.Response
import com.example.windy.data.model.City
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.repo.Repository
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavViewModel(val repo: Repository): ViewModel() {

    private val _favCities = MutableStateFlow<Response<List<City>>>(Response.Loading)
    val favCities = _favCities.asStateFlow()

    private val _searchPlaceCoordinates : MutableSharedFlow<Response<LatLng>> = MutableSharedFlow<Response<LatLng>>()
    val searchPlaceCoordinates = _searchPlaceCoordinates.asSharedFlow()

    private val _favCityCurrentWeather = MutableStateFlow<Response<CurrentWeatherResponse>>(Response.Loading)
    val favCityCurrentWeather =_favCityCurrentWeather.asStateFlow()

    private val _fiveDayFavCityWeather = MutableStateFlow<Response<FiveDayThreeHourResponse>>(Response.Loading)
    val fiveDayFavCityWeather = _fiveDayFavCityWeather.asStateFlow()


    fun getRemoteFavCityCurrentWeather(lat:String,lon:String,units:String,lang:String){
        viewModelScope.launch(Dispatchers.IO){
               try{
                   repo.getCurrentWeatherRemote(lat,lon,units,lang)
                       .catch {_favCityCurrentWeather.value = Response.Message(it.printStackTrace().toString()) }
                       .collect { _favCityCurrentWeather.value = Response.Success(it) }
               }catch(e: Exception){
                   _favCityCurrentWeather.value = Response.Message(e.printStackTrace().toString())
               }
        }
    }

    fun getRemoteFiveDayThreeHourWeather(lat:String,lon:String,units:String,lang:String){
        viewModelScope.launch (Dispatchers.IO){
            try {
                repo.getFiveDayThreeHourWeatherRemote(lat,lon,units,lang)
                    .catch {_fiveDayFavCityWeather.value = Response.Message(it.printStackTrace().toString()) }
                    .collect { _fiveDayFavCityWeather.value = Response.Success(it) }
            }catch (e: Exception){
                _fiveDayFavCityWeather.value = Response.Message(e.printStackTrace().toString())
            }
        }
    }

    fun getPlaceOnMap(searchText:String,placesClient: PlacesClient){
            viewModelScope.launch {
                try {
                    repo.getPlaceOnMap(searchText, placesClient)
                        .catch {
                            _searchPlaceCoordinates.emit(
                                Response.Message(
                                    it.printStackTrace().toString()
                                )
                            )
                        }
                        .collect {
                            _searchPlaceCoordinates.emit(Response.Success(it))
                        }


                }catch (e: Exception){
                    _searchPlaceCoordinates.emit(Response.Message("Error Connecting To Places Api"))
                }
            }
    }

    fun getLocalFavCities(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                repo.getFavCitiesLocal()
                    .catch {_favCities.value = Response.Message(it.printStackTrace().toString()) }
                    .collect {_favCities.value = Response.Success(it)}
            }catch(e: Exception){
                _favCities.value = Response.Message(e.printStackTrace().toString())
            }
        }
    }

    fun insertFavCity(city: City){
        viewModelScope.launch (Dispatchers.IO){
            try {
                  repo.insertFavCityLocal(city)
            }catch (e: Exception){
                _favCities.value = Response.Message(e.printStackTrace().toString())
            }
        }
    }

    fun deleteFavCity(city: City){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                 repo.deleteFavCityLocal(city)
            }catch (e: Exception){
                _favCities.value = Response.Message(e.printStackTrace().toString())
            }
        }
    }
}
class MyFavFactory(val repo: Repository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavViewModel(repo) as T
    }
}


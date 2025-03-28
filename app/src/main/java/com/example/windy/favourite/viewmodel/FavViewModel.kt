package com.example.windy.favourite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.windy.Response
import com.example.windy.data.model.City
import com.example.windy.data.model.FavCity
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.repo.Repository
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavViewModel(val repo: Repository): ViewModel() {

    private val _favCities = MutableStateFlow<Response<List<City>>>(Response.Loading)
    val favCities = _favCities.asStateFlow()

    private val _searchPlaceCoordinates = MutableStateFlow<Response<LatLng>>(Response.Loading)
    val searchPlaceCoordinates = _searchPlaceCoordinates.asStateFlow()

    private val _favCityCurrentWeather = MutableStateFlow<Response<CurrentWeatherResponse>>(Response.Loading)
    val favCityCurrentWeather =_favCityCurrentWeather.asStateFlow()

    private val _fiveDayFavCityWeather = MutableStateFlow<Response<FiveDayThreeHourResponse>>(Response.Loading)
    val fiveDayFavCityWeather = _fiveDayFavCityWeather.asStateFlow()


    fun getRemoteFavCityCurrentWeather(lat:String,lon:String,units:String){
        viewModelScope.launch(Dispatchers.IO){
               try{
                   repo.getCurrentWeatherRemote(lat=lat,lon=lon,units=units)
                       .catch {_favCityCurrentWeather.value = Response.Message(it.printStackTrace().toString()) }
                       .collect { _favCityCurrentWeather.value = Response.Success(it) }
               }catch(e: Exception){
                   _favCityCurrentWeather.value = Response.Message(e.printStackTrace().toString())
               }
        }
    }

    fun getRemoteFiveDayThreeHourWeather(lat:String,lon:String,units:String){
        viewModelScope.launch (Dispatchers.IO){
            try {
                repo.getFiveDayThreeHourWeatherRemote(lat = lat, lon = lon, units = units)
                    .catch {_fiveDayFavCityWeather.value = Response.Message(it.printStackTrace().toString()) }
                    .collect { _fiveDayFavCityWeather.value = Response.Success(it) }
            }catch (e: Exception){
                _fiveDayFavCityWeather.value = Response.Message(e.printStackTrace().toString())
            }
        }
    }

    fun getPlaceOnMap(searchText:String,placesClient: PlacesClient){
        try{
            viewModelScope.launch {
                repo.getPlaceOnMap(searchText,placesClient)
                    .catch { _searchPlaceCoordinates.value = Response.Message(it.printStackTrace().toString()) }
                    .collect { _searchPlaceCoordinates.value = Response.Success(it) }
            }
        }catch(e: Exception){
            _searchPlaceCoordinates.value = Response.Message("Error Connecting To Places Api")
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
                val result = repo.insertFavCityLocal(city)
                if(result > 0){
                    _favCities.value = Response.Message("Saved to favourites")
                }else{
                    _favCities.value = Response.Message("Problem saving to database")
                }
            }catch (e: Exception){
                _favCities.value = Response.Message(e.printStackTrace().toString())
            }
        }
    }

    fun deleteFavCity(city: City){

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val result = repo.deleteFavCityLocal(city)

                if(result > 0){
                    _favCities.value = Response.Message("Deleted")
                }else{
                    _favCities.value = Response.Message("Couldn't remove from favourites")
                }
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


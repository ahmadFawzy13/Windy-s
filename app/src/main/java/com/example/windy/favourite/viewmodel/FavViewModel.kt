package com.example.windy.favourite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.windy.Response
import com.example.windy.data.model.FavCity
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavViewModel(val repo: Repository): ViewModel() {

    private val _favCities = MutableStateFlow<Response<List<FavCity>>>(Response.Loading)
    val favCities = _favCities.asStateFlow()

    private val _favCityCurrentWeather = MutableStateFlow<Response<CurrentWeatherResponse>>(Response.Loading)
    val favCityCurrentWeather =_favCityCurrentWeather.asStateFlow()

    private val _fiveDayFavCityWeather = MutableStateFlow<Response<FiveDayThreeHourResponse>>(Response.Loading)
    val fiveDayFavCityWeather = _fiveDayFavCityWeather.asStateFlow()


    fun getRemoteFavCityCurrentWeather(lat:String,lon:String,units:String){
        viewModelScope.launch(Dispatchers.IO){
               try{
                   repo.getCurrentWeatherRemote(lat=lat,lon=lon,units=units)
                       .catch {_favCityCurrentWeather.value = Response.Failure(it.printStackTrace().toString()) }
                       .collect { _favCityCurrentWeather.value = Response.Success(it) }
               }catch(e: Exception){
                   _favCityCurrentWeather.value = Response.Failure(e.printStackTrace().toString())
               }
        }
    }

    fun getRemoteFiveDayThreeHourWeather(lat:String,lon:String,units:String){
        viewModelScope.launch (Dispatchers.IO){
            try {
                repo.getFiveDayThreeHourWeatherRemote(lat = lat, lon = lon, units = units)
                    .catch {_fiveDayFavCityWeather.value = Response.Failure(it.printStackTrace().toString()) }
                    .collect { _fiveDayFavCityWeather.value = Response.Success(it) }
            }catch (e: Exception){
                _fiveDayFavCityWeather.value = Response.Failure(e.printStackTrace().toString())
            }
        }
    }

    fun getLocalFavCities(){
        viewModelScope.launch(Dispatchers.IO) {
            try{
                repo.getFavCitiesLocal()
                    .catch {_favCities.value = Response.Failure(it.printStackTrace().toString()) }
                    .collect {_favCities.value = Response.Success(it)}
            }catch(e: Exception){
                _favCities.value = Response.Failure(e.printStackTrace().toString())
            }
        }
    }

    fun insertFavCity(favCity: FavCity){
        viewModelScope.launch (Dispatchers.IO){
            try {
                val result = repo.insertFavCityLocal(favCity)
                if(result > 0){
                    _favCities.value = Response.SuccessDataBaseOp("Saved to favourites")
                }else{
                    _favCities.value = Response.Failure("Problem saving to database")
                }
            }catch (e: Exception){
                _favCities.value = Response.Failure(e.printStackTrace().toString())
            }
        }
    }

    fun deleteFavCity(id: Int, favCity: FavCity){

        viewModelScope.launch(Dispatchers.IO) {

            try {
              val result = repo.deleteFavCityLocal(id)

                if(result > 0){
                    _favCities.value = Response.SuccessDataBaseOp("Deleted")
                }else{
                    _favCities.value = Response.Failure("Couldn't remove from favourites")
                }
            }catch (e: Exception){
                _favCities.value = Response.Failure(e.printStackTrace().toString())
            }
        }
    }
}
class MyFavFactory(val repo: Repository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavViewModel(repo) as T
    }
}


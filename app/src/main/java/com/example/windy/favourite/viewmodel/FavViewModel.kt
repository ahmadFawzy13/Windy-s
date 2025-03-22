package com.example.windy.favourite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.windy.data.model.FavCity
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavViewModel(val repo: Repository): ViewModel() {
    private val _favCities : MutableLiveData<List<FavCity>> = MutableLiveData()
    val favCities : LiveData <List<FavCity>> = _favCities

    private val _favCityCurrentWeather : MutableLiveData<CurrentWeatherResponse> = MutableLiveData()
    val favCityCurrentWeather : LiveData<CurrentWeatherResponse> = _favCityCurrentWeather

    private val _fiveDayFavCityWeather: MutableLiveData<FiveDayThreeHourResponse> = MutableLiveData()
    val fiveDayFavCityWeather: LiveData<FiveDayThreeHourResponse> = _fiveDayFavCityWeather

    private val _responseMessage : MutableLiveData <String> = MutableLiveData()
    val responseMessage : LiveData<String> = _responseMessage


    fun getRemoteFavCityCurrentWeather(lat:String,lon:String,units:String){

        viewModelScope.launch(Dispatchers.IO){
                 repo.getCurrentWeatherRemote(lat=lat,lon=lon,units=units)
                     .catch {_responseMessage.postValue(it.printStackTrace().toString()) }
                     .collect { _favCityCurrentWeather.postValue(it) }
        }
    }

    fun getRemoteFiveDayThreeHourWeather(lat:String,lon:String,units:String){

        viewModelScope.launch (Dispatchers.IO){
                repo.getFiveDayThreeHourWeatherRemote(lat = lat, lon = lon, units = units)
                    .catch {_responseMessage.postValue(it.printStackTrace().toString()) }
                    .collect { _fiveDayFavCityWeather.postValue(it) }
        }
    }

    fun getLocalFavCities(){
        viewModelScope.launch(Dispatchers.IO) {
             repo.getFavCitiesLocal()
                 .catch {_responseMessage.postValue(it.printStackTrace().toString()) }
                 .collect {_favCities.postValue(it)}
        }
    }

    fun insertFavCity(favCity: FavCity){

        viewModelScope.launch (Dispatchers.IO){
            try {
                val result = repo.insertFavCityLocal(favCity)
                if(result > 0){
                    _responseMessage.postValue("Saved to favourites")
                }else{
                    _responseMessage.postValue("Problem saving to database")
                }
            }catch (e: Exception){
                _responseMessage.postValue(e::class.simpleName)
            }
        }
    }

    fun deleteFavCity(id: Int, favCity: FavCity){

        viewModelScope.launch(Dispatchers.IO) {

            try {
              val result = repo.deleteFavCityLocal(id)

                if(result > 0){
                    val currentList = _favCities.value?.toMutableList() ?: mutableListOf()
                    currentList.remove(favCity)
                    _favCities.postValue(currentList)
                    _responseMessage.postValue("Deleted")
                }else{
                    _responseMessage.postValue("Couldn't remove from favourites")
                }
            }catch (e: Exception){
                _responseMessage.postValue(e::class.simpleName)
            }

        }
    }
}
class MyFavFactory(val repo: Repository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavViewModel(repo) as T
    }
}


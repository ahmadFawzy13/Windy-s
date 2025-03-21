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
            try{
                val result = repo.getCurrentWeatherRemote(lat=lat,lon=lon,units=units)
                if(result != null){
                    _favCityCurrentWeather.postValue(result)
                }else{
                    _responseMessage.postValue("result is null (getRemoteFavCityCurrentWeather)")
                }
            }catch (e: Exception){
                _responseMessage.postValue(e::class.simpleName)
            }
        }
    }

    fun getRemoteFiveDayThreeHourWeather(lat:String,lon:String,units:String){

        viewModelScope.launch (Dispatchers.IO){

            try {

                val result = repo.getFiveDayThreeHourWeatherRemote(lat = lat, lon = lon, units = units)
                if(result != null){
                    _fiveDayFavCityWeather.postValue(result)
                }else{
                    _responseMessage.postValue("result is null(getRemoteFiveDay)")
                }
            }catch (e: Exception){
                _responseMessage.postValue(e::class.simpleName)
            }
        }
    }

    fun getLocalFavCities(){
        viewModelScope.launch(Dispatchers.IO) {

            try {

             val result = repo.getFavCitiesLocal()

                if (!result.isEmpty()){
                    _favCities.postValue(result)
                }else{
                    _responseMessage.postValue("Empty Local (getLocalFavCities)")
                }

            }catch (e: Exception){
                _responseMessage.postValue(e::class.simpleName)
            }
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


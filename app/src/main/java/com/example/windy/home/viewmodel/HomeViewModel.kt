package com.example.windy.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(val repo: Repository) : ViewModel() {

    private val _currentWeather : MutableLiveData<CurrentWeatherResponse> = MutableLiveData()
    val currentWeather : LiveData<CurrentWeatherResponse> = _currentWeather

    private val _fiveDayThreeHourWeather : MutableLiveData<FiveDayThreeHourResponse> = MutableLiveData()
    val fiveDayThreeHourWeather : LiveData<FiveDayThreeHourResponse> = _fiveDayThreeHourWeather

    private val _responseMessage: MutableLiveData<String> = MutableLiveData()
    val responseMessage : LiveData<String> = _responseMessage

    fun getRemoteCurrentWeather(lat:String,lon:String,units:String){

        viewModelScope.launch(Dispatchers.IO) {
            try {
               repo.getCurrentWeatherRemote(lat = lat,lon = lon,units = units)
                   .collect { _currentWeather.postValue(it) }

            }catch (ex: Exception){
                _responseMessage.postValue(ex.printStackTrace().toString())
            }
        }

    }

    fun getRemoteFiveDayThreeHourWeather(lat:String,lon:String,units:String){

        viewModelScope.launch (Dispatchers.IO){

            try {

                repo.getFiveDayThreeHourWeatherRemote(lat = lat, lon = lon, units = units)
                    .collect { _fiveDayThreeHourWeather.postValue(it) }
            }catch (e: Exception){
                _responseMessage.postValue(e.printStackTrace().toString())
            }
        }
    }
}

class MyHomeFactory (val repo: Repository): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo) as T
    }
}
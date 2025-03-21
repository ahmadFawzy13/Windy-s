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
                val result = repo.getCurrentWeatherRemote(lat = lat,lon = lon,units = units)
                if(result != null){
                   _currentWeather.postValue(result)
                }else{
                    _responseMessage.postValue("result is null (getRemoteCurrentWeather)")
                }
            }catch (ex: Exception){
                _responseMessage.postValue(ex::class.simpleName)
            }
        }
    }

    fun getRemoteFiveDayThreeHourWeather(lat:String,lon:String,units:String){

        viewModelScope.launch (Dispatchers.IO){

            try {

                val result = repo.getFiveDayThreeHourWeatherRemote(lat = lat, lon = lon, units = units)
                if(result != null){
                    _fiveDayThreeHourWeather.postValue(result)
                }else{
                    _responseMessage.postValue("result is null(getRemoteFiveDay)")
                }
            }catch (e: Exception){
                _responseMessage.postValue(e::class.simpleName)
            }
        }
    }
}

class MyHomeFactory (val repo: Repository): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo) as T
    }
}
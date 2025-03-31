package com.example.windy.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.windy.Response
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(val repo: Repository) : ViewModel() {

    private val _currentWeather =
        MutableStateFlow<Response<CurrentWeatherResponse>>(Response.Loading)

    val currentWeather =
        _currentWeather.asStateFlow()


    private val _fiveDayThreeHourWeather =
        MutableStateFlow<Response<FiveDayThreeHourResponse>>(Response.Loading)

    val fiveDayThreeHourWeather =
        _fiveDayThreeHourWeather.asStateFlow()

    fun getRemoteCurrentWeather(lat:String,lon:String,units:String,lang:String){
        viewModelScope.launch(Dispatchers.IO) {
             try {
                 repo.getCurrentWeatherRemote(lat,lon,units,lang)
                     .catch {_currentWeather.value = (Response.Message(it.printStackTrace().toString())) }
                     .collect { _currentWeather.value = Response.Success(it) }
             }catch(e: Exception){
                 _currentWeather.value = Response.Message(e.printStackTrace().toString())
             }

        }
    }

    fun getRemoteFiveDayThreeHourWeather(lat:String,lon:String,units:String,lang:String){

        viewModelScope.launch (Dispatchers.IO){
               try {
                   repo.getFiveDayThreeHourWeatherRemote(lat,lon,units,lang)
                       .catch {_fiveDayThreeHourWeather.value = Response.Message(it.printStackTrace().toString()) }
                       .collect { _fiveDayThreeHourWeather.value = Response.Success(it) }
               }catch(e: Exception
               ){
                   _fiveDayThreeHourWeather.value = Response.Message(e.printStackTrace().toString())
               }
        }
    }
}

class MyHomeFactory (val repo: Repository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repo) as T
    }
}
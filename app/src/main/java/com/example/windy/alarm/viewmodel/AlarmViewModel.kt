package com.example.windy.alarm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.windy.data.model.Response
import com.example.windy.data.model.Alarm
import com.example.windy.data.repo.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlarmViewModel (val repo: Repository) : ViewModel(){

    private val _alarms = MutableStateFlow<Response<List<Alarm>>>(Response.Loading)
    val alarms = _alarms.asStateFlow()

    fun getAlarms(){
        viewModelScope.launch(Dispatchers.IO){
          try{
              repo.getAlarms()
                  .catch {_alarms.value = Response.Message(it.printStackTrace().toString())}
                  .collect {_alarms.value = Response.Success(it) }

          }catch(e: Exception){
              _alarms.value = Response.Message(e.printStackTrace().toString())
          }
        }
    }


    fun insertAlarm(alarm: Alarm){
        viewModelScope.launch {
            try {
                 repo.insertAlarm(alarm)
            }catch(e: Exception){
                _alarms.value = Response.Message(e.printStackTrace().toString())
            }
        }
    }

    fun deleteAlarm(alarm: Alarm){

        viewModelScope.launch {

            try {
                  repo.deleteAlarm(alarm)
            }catch(e: Exception){
                _alarms.value = Response.Message(e.printStackTrace().toString())
            }
        }
    }
}

class MyAlarmFactory(val repo: Repository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmViewModel(repo) as T
    }
}
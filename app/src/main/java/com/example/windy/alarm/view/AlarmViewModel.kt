package com.example.windy.alarm.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.windy.Response
import com.example.windy.alarm.view.AlarmViewModel
import com.example.windy.data.repo.Repository
import kotlinx.coroutines.flow.MutableStateFlow

class AlarmViewModel (val repo: Repository) : ViewModel(){

    private val _alarms = MutableStateFlow<Response<List<Alarm>>>


}

class MyAlarmFactory(val repo: Repository) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmViewModel(repo) as T
    }
}
package com.example.windy

import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.repo.Repository
import com.example.windy.home.viewmodel.HomeViewModel
import getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    val currentWeatherResponse : CurrentWeatherResponse = CurrentWeatherResponse()
    val fiveDayThreeHourResponse : FiveDayThreeHourResponse = FiveDayThreeHourResponse()
    private var stubRepository: Repository = mockk()

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup(){
        homeViewModel = HomeViewModel(stubRepository)
        coEvery { stubRepository.getCurrentWeatherRemote("52.5200","13.4050","metric") } returns currentWeatherResponse
        coEvery { stubRepository.getFiveDayThreeHourWeatherRemote("52.5200","13.4050","metric") } returns fiveDayThreeHourResponse
    }


    @Test
    fun getRemoteCurrentWeather_LiveDataIsNotNull(){

        //when
        homeViewModel.getRemoteCurrentWeather("52.5200","13.4050","metric")
        val result = homeViewModel.currentWeather.getOrAwaitValue{}

        //then
        assertThat(result,not(nullValue()))
        coVerify { stubRepository.getCurrentWeatherRemote("52.5200","13.4050","metric") }

    }
}
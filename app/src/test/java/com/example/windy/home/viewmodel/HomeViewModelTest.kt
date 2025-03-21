package com.example.windy.home.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.repo.Repository
import com.example.windy.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


class HomeViewModelTest {

    val currentWeatherResponse : CurrentWeatherResponse = CurrentWeatherResponse()
    val fiveDayThreeHourResponse : FiveDayThreeHourResponse = FiveDayThreeHourResponse()
    private var stubRepository: Repository = mockk()

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup(){
        homeViewModel = HomeViewModel(stubRepository)
        coEvery { stubRepository.getCurrentWeatherRemote("52.5200", "13.4050", "metric") } returns currentWeatherResponse
        coEvery {
            stubRepository.getFiveDayThreeHourWeatherRemote(
                "52.5200",
                "13.4050",
                "metric"
            )
        } returns fiveDayThreeHourResponse
    }


    @Test
    fun getRemoteCurrentWeather_LiveDataIsNotNull(){

        //when
        homeViewModel.getRemoteCurrentWeather("52.5200","13.4050","metric")
        val result = homeViewModel.currentWeather.getOrAwaitValue {}

        //then
        MatcherAssert.assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
        coVerify { stubRepository.getCurrentWeatherRemote("52.5200", "13.4050", "metric") }

    }


    @Test
    fun getRemoteFiveDayThreeHourWeather_LiveDataIsNotNull(){

        //when
        homeViewModel.getRemoteFiveDayThreeHourWeather("52.5200","13.4050","metric")
        val result = homeViewModel.fiveDayThreeHourWeather.getOrAwaitValue{}

        //then
        MatcherAssert.assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
        coVerify { stubRepository.getFiveDayThreeHourWeatherRemote("52.5200", "13.4050", "metric") }

    }

}
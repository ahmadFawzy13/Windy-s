package com.example.windy.favourite.viewmodel

import com.example.windy.data.model.FavCity
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.repo.Repository
import com.example.windy.getOrAwaitValue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class FavouriteViewModelTest {

    val currentWeatherResponse : CurrentWeatherResponse = CurrentWeatherResponse()
    val fiveDayThreeHourResponse : FiveDayThreeHourResponse = FiveDayThreeHourResponse()
    val cityList : List<FavCity> = listOf(
        FavCity(),
        FavCity(),
        FavCity()
    )

    private var stubRepository: Repository = mockk()
    private lateinit var favViewModel: FavViewModel

    @Before
    fun setup(){


        favViewModel = FavViewModel(stubRepository)

        coEvery { stubRepository.getCurrentWeatherRemote(
            "52.5200",
            "13.4050",
            "metric")
        } returns currentWeatherResponse

        coEvery { stubRepository.getFiveDayThreeHourWeatherRemote("52.5200",
            "13.4050",
            "metric")
        } returns fiveDayThreeHourResponse

        coEvery {stubRepository.getFavCitiesLocal()} returns cityList

    }


    @Test
    fun getRemoteFavCityCurrentWeather_LiveDataIsNotNull(){

        //when
        favViewModel.getRemoteFavCityCurrentWeather("52.5200","13.4050","metric")
        val result = favViewModel.favCityCurrentWeather.getOrAwaitValue {}

        //then
        assertThat(result, not(nullValue()))
        coVerify { stubRepository.getCurrentWeatherRemote("52.5200", "13.4050", "metric") }

    }

    @Test
    fun getRemoteFiveDayThreeHourWeather_LiveDataIsNotNull(){

        //when
        favViewModel.getRemoteFiveDayThreeHourWeather("52.5200","13.4050","metric")
        val result = favViewModel.fiveDayFavCityWeather.getOrAwaitValue {}

        //then
        assertThat(result, not(nullValue()))
        coVerify { stubRepository.getFiveDayThreeHourWeatherRemote("52.5200", "13.4050", "metric") }

    }

    @Test
    fun getLocalFavCities_LiveDataIsNotNull(){

        //when
        favViewModel.getLocalFavCities()
        val result = favViewModel.favCities.getOrAwaitValue{}

        //then
        assertThat(result, not(nullValue()))
        coVerify { stubRepository.getFavCitiesLocal() }

    }
}
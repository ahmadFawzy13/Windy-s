package com.example.windy.home.viewmodel

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.repo.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.example.windy.data.local.WeatherLocalDataSource
import com.example.windy.data.model.FavCity
import com.example.windy.data.remote.WeatherRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers
import org.hamcrest.core.IsEqual
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.windy.Response
import com.example.windy.data.model.Clouds
import com.example.windy.data.model.CountryDetails
import com.example.windy.data.model.Weather
import com.example.windy.data.model.WeatherDetails
import com.example.windy.data.model.Wind
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert


class HomeViewModelTest {

    val currentWeatherResponse = CurrentWeatherResponse(
        weather = listOf(Weather(id = 800, main = "Clear", description = "clear sky", icon = "01d")),
        weatherDetails = WeatherDetails(temp = 25.0, pressure = 1012, humidity = 60),
        wind = Wind(speed = 5.0, deg = 180),
        clouds = Clouds(all = 10),
        dt = 1672531200,
        sys = CountryDetails(country = "US", sunrise = 1672500000, sunset = 1672550000),
        cityName = "New York",
        cityId = 5128581,
        timezone = -18000
    )

    val fiveDayThreeHourResponse : FiveDayThreeHourResponse = FiveDayThreeHourResponse()

    private lateinit var stubRepository : Repository
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setup(){
        stubRepository = mockk()
        homeViewModel = HomeViewModel(stubRepository)

        coEvery { stubRepository.getCurrentWeatherRemote("52.5200",
            "13.4050",
            "metric") } returns flowOf(currentWeatherResponse)

        coEvery {
            stubRepository.getFiveDayThreeHourWeatherRemote(
                "52.5200",
                "13.4050",
                "metric"
            )
        } returns flowOf(fiveDayThreeHourResponse)
      //  Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() = Dispatchers.resetMain()


    @Test
    fun getRemoteCurrentWeather_stateFlowNotNull() = runTest{

         homeViewModel.getRemoteCurrentWeather("52.5200","13.4050","metric")

         val result = homeViewModel.currentWeather.value

        if(result is Response.Success){
            assertNotNull(result.data)
            assertThat(result.data, `is`(currentWeatherResponse))
            coVerify { stubRepository.getCurrentWeatherRemote("52.5200", "13.4050", "metric") }
        }
    }


    @Test
    fun getRemoteFiveDayThreeHourWeather_LiveDataIsNotNull(){

        //when
        homeViewModel.getRemoteFiveDayThreeHourWeather("52.5200","13.4050","metric")
        val result = homeViewModel.fiveDayThreeHourWeather.value

        if(result is Response.Success){
            assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
            coVerify { stubRepository.getFiveDayThreeHourWeatherRemote("52.5200", "13.4050", "metric") }
        }
    }
}
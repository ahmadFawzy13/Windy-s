package com.example.windy.favourite.viewmodel

import com.example.windy.Response
import com.example.windy.data.model.City
import com.example.windy.data.model.Clouds
import com.example.windy.data.model.Coordinates
import com.example.windy.data.model.CountryDetails
import com.example.windy.data.model.Weather
import com.example.windy.data.model.WeatherDetails
import com.example.windy.data.model.Wind
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.repo.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class FavouriteViewModelTest {

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

    val cityList : List<City> = listOf(
        City(
            id = 1,
            name = "fakeName",
            coord = Coordinates(2.0,30.25),
            country = "fakeCountry",
            population = 20,
            timezone = 1,
            sunrise = 8,
            sunset = 6,
        ),
        City(
            id = 2,
            name = "fakeCountry",
            coord = Coordinates(21.0,26.0),
            country = "fakeCountry",
            population = 4,
            timezone = 3,
            sunrise = 2,
            sunset = 1
        ),
    )

    private var stubRepository: Repository = mockk()
    private lateinit var favViewModel: FavViewModel

    @Before
    fun setup(){

        favViewModel = FavViewModel(stubRepository)

        coEvery { stubRepository.getCurrentWeatherRemote(
            "52.5200",
            "13.4050",
            "metric",
            "en")
        } returns flowOf(currentWeatherResponse)

        coEvery {stubRepository.getFavCitiesLocal()} returns flowOf(cityList)

    }


    @Test
    fun getRemoteFavCityCurrentWeather_StateFlowNotNull(){

        //when
        favViewModel.getRemoteFavCityCurrentWeather("52.5200","13.4050","metric","en")
        val result = favViewModel.favCityCurrentWeather.value

        //then
        if(result is Response.Success){
            assertThat(result.data, not(nullValue()))
        }

        coVerify { stubRepository.getCurrentWeatherRemote("52.5200", "13.4050", "metric","en") }

    }

    @Test
    fun getLocalFavCities_stateFlowNotNull(){

        //when
        favViewModel.getLocalFavCities()
        val result = favViewModel.favCities.value

        if(result is Response.Success){
            assertThat(result.data, not(nullValue()))
        }

        coVerify { stubRepository.getFavCitiesLocal() }

    }
}
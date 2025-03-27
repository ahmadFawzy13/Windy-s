package com.example.windy.data.repo

import com.example.windy.data.local.WeatherLocalDataSource
import com.example.windy.data.model.City
import com.example.windy.data.model.Clouds
import com.example.windy.data.model.Coordinates
import com.example.windy.data.model.CountryDetails
import com.example.windy.data.model.FavCity
import com.example.windy.data.model.Weather
import com.example.windy.data.model.WeatherDetails
import com.example.windy.data.model.Wind
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.remote.WeatherRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.flow.first
import org.hamcrest.MatcherAssert.assertThat

class RepositoryTest {

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

    private val mockWeatherRemoteDataSource: WeatherRemoteDataSource = mockk()
    private val mockLocalDataSource: WeatherLocalDataSource = mockk()
    private lateinit var repo : Repository

    @Before
    fun setup(){
        repo = Repository(mockLocalDataSource,mockWeatherRemoteDataSource)

        coEvery {
            mockWeatherRemoteDataSource.getCurrentWeatherRemote(
                "52.5200",
                "13.4050",
                "metric"
            )
        } returns flowOf(currentWeatherResponse)
        coEvery { mockLocalDataSource.getFavCitiesLocal() } returns flowOf(cityList)
        coEvery { mockLocalDataSource.deleteFavCityLocal(654) } returns 1
    }

    @Test
    fun getCurrentWeatherRemote_returnsRemoteDataFromRemote() = runTest {

        val result=repo.getCurrentWeatherRemote("52.5200", "13.4050", "metric").first()

        assertThat(result, IsEqual(currentWeatherResponse))

        coVerify {
            mockWeatherRemoteDataSource.getCurrentWeatherRemote(
                "52.5200",
                "13.4050",
                "metric"
            )
        }
    }

    @Test
    fun getFavCitiesLocal_returnsLocalDataFromLocal() = runTest {

         val result = repo.getFavCitiesLocal().first()

        assertThat(result, IsEqual(cityList))
        coVerify { mockLocalDataSource.getFavCitiesLocal() }
    }

    @Test
    fun deleteFavCityLocal_returnsSuccessCodeFromLocal() = runTest {

        var result = repo.deleteFavCityLocal(654)

        assertThat(result, Matchers.greaterThan(0))
        coVerify { mockLocalDataSource.deleteFavCityLocal(654) }
    }

}
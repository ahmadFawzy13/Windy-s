package com.example.windy

import com.example.windy.data.local.WeatherLocalDataSource
import com.example.windy.data.model.FavCity
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.test.runTest
import com.example.windy.data.repo.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.core.IsEqual

import org.junit.Before
import org.junit.Test

class RepositoryTest {

    val currentWeatherResponse : CurrentWeatherResponse = CurrentWeatherResponse()
    val fiveDayThreeHourResponse : FiveDayThreeHourResponse = FiveDayThreeHourResponse()

    val cityList : List<FavCity> = listOf(
            FavCity(),
            FavCity(),
            FavCity()
    )


    private val mockWeatherRemoteDataSource: WeatherRemoteDataSource = mockk()
    private val mockLocalDataSource: WeatherLocalDataSource = mockk()
    private lateinit var repo : Repository


    @Before
    fun setup(){
        repo = Repository(mockLocalDataSource,mockWeatherRemoteDataSource)
    }


    @Test
    fun getCurrentWeatherRemote_returnsRemoteDataFromRemote() = runTest(){

        coEvery { mockWeatherRemoteDataSource.getCurrentWeatherRemote("52.5200","13.4050","metric") } returns currentWeatherResponse

        var result = repo.getCurrentWeatherRemote("52.5200","13.4050","metric")
        assertThat(result, IsEqual(currentWeatherResponse))
        coVerify { mockWeatherRemoteDataSource.getCurrentWeatherRemote("52.5200","13.4050","metric") }

    }

    @Test
    fun getFiveDayWeatherRemote_returnsRemoteDataFromRemote() = runTest{

        coEvery { mockWeatherRemoteDataSource.getFiveDayThreeHourWeatherRemote("52.5200","13.4050","metric") } returns fiveDayThreeHourResponse

        var result = repo.getFiveDayThreeHourWeatherRemote("52.5200","13.4050","metric")
        assertThat(result, IsEqual(fiveDayThreeHourResponse))

        coVerify { mockWeatherRemoteDataSource.getFiveDayThreeHourWeatherRemote("52.5200","13.4050","metric") }

    }

    @Test
    fun getFav_returnsLocalDataFromLocal() = runTest{

        coEvery { mockLocalDataSource.getFavCitiesLocal() } returns cityList

        var result = repo.getFavCitiesLocal()
        assertThat(result, IsEqual(cityList))

        coVerify {  mockLocalDataSource.getFavCitiesLocal() }

    }

    @Test
    fun deleteFav_returnsSuccessCodeFromLocal() = runTest {

        coEvery { mockLocalDataSource.deleteFavCityLocal(654) } returns 1

        var result = repo.deleteFavCityLocal(654)
        assertThat(result, greaterThan(0))

        coVerify { mockLocalDataSource.deleteFavCityLocal(654) }
    }

    @Test
    fun insertFav_returnsSuccessCodeFromLocal() = runTest {

        coEvery { mockLocalDataSource.insertFavCityLocal(FavCity()) } returns 1

        var result = repo.insertFavCityLocal(FavCity())
        assertThat(result, greaterThan(0))
    }
}
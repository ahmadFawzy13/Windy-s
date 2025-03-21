package com.example.windy.data.repo

import com.example.windy.data.local.WeatherLocalDataSource
import com.example.windy.data.model.FavCity
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.remote.WeatherRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
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
        coEvery {
            mockWeatherRemoteDataSource.getCurrentWeatherRemote(
                "52.5200",
                "13.4050",
                "metric"
            )
        } returns currentWeatherResponse
        coEvery {
            mockWeatherRemoteDataSource.getFiveDayThreeHourWeatherRemote(
                "52.5200",
                "13.4050",
                "metric"
            )
        } returns fiveDayThreeHourResponse
        coEvery { mockLocalDataSource.getFavCitiesLocal() } returns cityList
        coEvery { mockLocalDataSource.deleteFavCityLocal(654) } returns 1
        coEvery { mockLocalDataSource.insertFavCityLocal(FavCity()) } returns 1
    }


    @Test
    fun getCurrentWeatherRemote_returnsRemoteDataFromRemote() = runTest {
        var result = repo.getCurrentWeatherRemote("52.5200", "13.4050", "metric")
        MatcherAssert.assertThat(result, IsEqual(currentWeatherResponse))
        coVerify {
            mockWeatherRemoteDataSource.getCurrentWeatherRemote(
                "52.5200",
                "13.4050",
                "metric"
            )
        }
    }

    @Test
    fun getFiveDayWeatherRemote_returnsRemoteDataFromRemote() = runTest {
        var result = repo.getFiveDayThreeHourWeatherRemote("52.5200", "13.4050", "metric")
        MatcherAssert.assertThat(result, IsEqual(fiveDayThreeHourResponse))
        coVerify {
            mockWeatherRemoteDataSource.getFiveDayThreeHourWeatherRemote(
                "52.5200",
                "13.4050",
                "metric"
            )
        }
    }

    @Test
    fun getFav_returnsLocalDataFromLocal() = runTest {
        var result = repo.getFavCitiesLocal()
        MatcherAssert.assertThat(result, IsEqual(cityList))
        coVerify { mockLocalDataSource.getFavCitiesLocal() }
    }

    @Test
    fun deleteFav_returnsSuccessCodeFromLocal() = runTest {
        var result = repo.deleteFavCityLocal(654)
        MatcherAssert.assertThat(result, Matchers.greaterThan(0))
        coVerify { mockLocalDataSource.deleteFavCityLocal(654) }
    }

    @Test
    fun insertFav_returnsSuccessCodeFromLocal() = runTest {
        var result = repo.insertFavCityLocal(FavCity())
        MatcherAssert.assertThat(result, Matchers.greaterThan(0))
        coVerify { mockLocalDataSource.insertFavCityLocal(FavCity()) }
    }
}
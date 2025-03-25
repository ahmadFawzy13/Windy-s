package com.example.windy.data.repo

import com.example.windy.data.local.WeatherLocalDataSource
import com.example.windy.data.model.FavCity
import com.example.windy.data.remote.CurrentWeatherResponse
import com.example.windy.data.remote.FiveDayThreeHourResponse
import com.example.windy.data.remote.WeatherRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.runner.RunWith

class RepositoryTest {

    private val currentWeatherResponse : CurrentWeatherResponse = CurrentWeatherResponse()
    private val fiveDayThreeHourResponse : FiveDayThreeHourResponse = FiveDayThreeHourResponse()
    private val cityList : List<FavCity> = listOf(
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
        } returns flowOf(currentWeatherResponse)

        coEvery {
            mockWeatherRemoteDataSource.getFiveDayThreeHourWeatherRemote(
                "52.5200",
                "13.4050",
                "metric"
            )
        } returns flowOf(fiveDayThreeHourResponse)

        coEvery { mockLocalDataSource.getFavCitiesLocal() } returns flowOf(cityList)
        coEvery { mockLocalDataSource.deleteFavCityLocal(654) } returns 1
        coEvery { mockLocalDataSource.insertFavCityLocal(FavCity()) } returns 1
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
    fun getFiveDayWeatherRemote_returnsRemoteDataFromRemote() = runTest {

       val result =  repo.getFiveDayThreeHourWeatherRemote("52.5200", "13.4050", "metric").first()

         assertThat(result, IsEqual(fiveDayThreeHourResponse))

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
         val result = repo.getFavCitiesLocal().first()

        assertThat(result, IsEqual(cityList))
        coVerify { mockLocalDataSource.getFavCitiesLocal() }
    }

    @Test
    fun deleteFav_returnsSuccessCodeFromLocal() = runTest {
        var result = repo.deleteFavCityLocal(654)
        assertThat(result, Matchers.greaterThan(0))
        coVerify { mockLocalDataSource.deleteFavCityLocal(654) }
    }

    @Test
    fun insertFav_returnsSuccessCodeFromLocal() = runTest {
        var result = repo.insertFavCityLocal(FavCity())
        assertThat(result, Matchers.greaterThan(0))
        coVerify { mockLocalDataSource.insertFavCityLocal(FavCity()) }
    }
}
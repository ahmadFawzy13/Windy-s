package com.example.windy.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.windy.data.model.FavCity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class WeatherLocalDataSourceTest {

    private lateinit var db : WeatherDataBase
    private lateinit var weatherLocalDataSource: WeatherLocalDataSource

    @Before
    fun setup(){
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext()
            , WeatherDataBase::class.java)
            .allowMainThreadQueries()
            .build()

        weatherLocalDataSource = WeatherLocalDataSource(db.getWeatherDao())
    }

    @After
    fun teardown() = db.close()

    @Test
    fun getFavCityLocal_insertedCity_sameCity()= runTest{
        val favCity = FavCity(1,"Cairo",123.0,
            321.0,"Egypt",1,2,3)
        weatherLocalDataSource.insertFavCityLocal(favCity)

        val result = weatherLocalDataSource.getFavCitiesLocal().first()

        assertNotNull(result)
        assertThat(result[0],`is`(favCity))

    }


    @Test
    fun deleteFavCity_insertedCity_greaterThanZero()=runTest{
        val favCity = FavCity(1,"Cairo",123.0,
            321.0,"Egypt",1,2,3)
        weatherLocalDataSource.insertFavCityLocal(favCity)

        val result = weatherLocalDataSource.deleteFavCityLocal(favCity.id)
        assertNotNull(result)
        assertThat(result,greaterThan(0))

    }
}
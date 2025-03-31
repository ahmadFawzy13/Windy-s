package com.example.windy.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.windy.data.model.City
import com.example.windy.data.model.Coordinates
import com.example.windy.data.model.FavCity
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {

    private lateinit var db : WeatherDataBase
    private lateinit var dao : WeatherDao

    val city = City(
        id = 1,
        name = "fakeName",
        coord = Coordinates(2.0,30.25),
        country = "fakeCountry",
        population = 20,
        timezone = 1,
        sunrise = 8,
        sunset = 6,
    )

    @Before
    fun setup(){
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.getWeatherDao()
    }

    @After
    fun teardown() = db.close()

    @Test
    fun getFavCity_insertedCity_sameCity()= runTest{

        dao.insertFavCity(city)

        val result = dao.getFavCities().first()

        assertNotNull(result)
        assertThat(result[0], `is`(city))

    }

    @Test
    fun deleteFavCity_insertedCity_greaterThanZero()=runTest{

        dao.insertFavCity(city)

        val result = dao.deleteFavCity(city)

        assertThat(result,greaterThan(0))
    }
}
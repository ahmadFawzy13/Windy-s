package com.example.windy.data.local

import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.windy.data.model.FavCity
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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
@SmallTest
class WeatherDaoTest {

    private lateinit var db : WeatherDataBase
    private lateinit var dao : WeatherDao

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

        val favCity = FavCity(1,"Cairo",123.0,321.0,
            "Egypt",1,2,3)
        dao.insertFavCity(favCity)

        val result = dao.getFavCities().first()

        assertNotNull(result)
        assertThat(result[0], `is`(favCity))

    }

    @Test
    fun deleteFavCity_insertedCity_greaterThanZero()=runTest{

        val favCity = FavCity(1,"Cairo",123.0,321.0,
            "Egypt",1,2,3)
        dao.insertFavCity(favCity)

        val result = dao.deleteFavCity(favCity.id)

        assertThat(result,greaterThan(0))
    }
}
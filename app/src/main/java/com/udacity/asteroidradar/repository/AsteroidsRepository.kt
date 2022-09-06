package com.udacity.asteroidradar.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.PrefsHelper
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.NetworkAsteroidContainer
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.utils.Constants.ASTEROID_ENDPOINT
import com.udacity.asteroidradar.utils.Constants.TODAY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject


class AsteroidsRepository(private val database: AsteroidDatabase) {
     val todayAsteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getTodayAsteroid(
        TODAY)) {
        it.asDomainModel()
    }
     val allAsteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroid()) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try{

                val asteroids = Network.asteroidService.getAsteroid(ASTEROID_ENDPOINT)
                val asteroidContainer = NetworkAsteroidContainer(parseAsteroidsJsonResult(JSONObject(asteroids.toString())))


                database.asteroidDao.clear()
                database.asteroidDao.insertAll(*asteroidContainer.asDatabaseModel())
            }catch (e: Throwable){

            }
        }
    }


    suspend fun getPicOfTheDay() {
        withContext(Dispatchers.IO) {
            try{

                val image:PictureOfDay = Network.picOfTheDayService.getImageOfTheDay()


                PrefsHelper.write("image", image.url)


            }catch (e: Throwable){
                Log.e("deedee","no no no $e")

            }
        }
    }




}

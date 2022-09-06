package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.PrefsHelper
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : ViewModel() {
    private val database = AsteroidDatabase.getInstance(application)
    private val asteroidRepository = AsteroidsRepository(database)


    private val _isToday = MutableLiveData<Boolean>()
    val isToday: LiveData<Boolean> get() = _isToday

    init {
        _isToday.value = true
        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
            asteroidRepository.getPicOfTheDay()
            _picOfTheDay.value = PrefsHelper.read("image", "")
        }
    }

    val dailyList = asteroidRepository.todayAsteroids
    val weeklyList = asteroidRepository.allAsteroids




    private val _picOfTheDay = MutableLiveData<String?>()
     val picOfTheDay: LiveData<String?> get() = _picOfTheDay


    val navigateToSelectedAsteroid: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroid
    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid?>()


    fun displayDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }


    fun updateFilter(isToday:Boolean){
        _isToday.value =isToday
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
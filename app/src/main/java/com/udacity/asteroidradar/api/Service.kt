package com.udacity.asteroidradar.api

import com.google.gson.JsonObject
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.utils.Constants.ASTEROID_ENDPOINT
import com.udacity.asteroidradar.utils.Constants.BASE_URL
import com.udacity.asteroidradar.utils.Constants.IMAGE_OF_THE_DAY_ENDPOINT
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url


interface AsteroidService {
    @GET
    suspend fun getAsteroid(@Url url:String): JsonObject



    @GET(IMAGE_OF_THE_DAY_ENDPOINT)
    suspend fun getImageOfTheDay(): PictureOfDay
}


private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


object Network {
    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()



    private val retrofit2 = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()


    val asteroidService: AsteroidService = retrofit.create(AsteroidService::class.java)
    val picOfTheDayService: AsteroidService = retrofit2.create(AsteroidService::class.java)

}

package com.udacity.asteroidradar.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object Constants {
    private const val API_KEY = "Kv5losaJPb9zvs3RAoPxJbfrVojPquugi7grbG7K"
    const val API_QUERY_DATE_FORMAT = "YYYY-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov/"
    const val IMAGE_OF_THE_DAY_ENDPOINT = "planetary/apod?api_key=$API_KEY"

    val ASTEROID_ENDPOINT =
        "neo/rest/v1/feed?start_date=${getNextSevenDaysFormattedDates().first()}&end_date=${getNextSevenDaysFormattedDates().last()}&api_key=$API_KEY"

    val TODAY = getNextSevenDaysFormattedDates().first()


     fun getNextSevenDaysFormattedDates(): ArrayList<String> {
        val formattedDateList = ArrayList<String>()

        val calendar = Calendar.getInstance()
        for (i in 0..DEFAULT_END_DATE_DAYS) {
            val currentTime = calendar.time
            val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
            formattedDateList.add(dateFormat.format(currentTime))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        return formattedDateList
    }

}


package uk.ac.tees.mad.airtrack.ui.mainapp

import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.tees.mad.airtrack.ui.mainapp.model.AirQualityInfo

interface APIinterface {

    @GET("current.json?")
    suspend fun getAirQuality(
        @Query("key") key: String,
        @Query("q") q: String,
        @Query("aqi") aqi: String
    ): AirQualityInfo
}
package uk.ac.tees.mad.airtrack.ui.mainapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val BASE_URL ="https://api.weatherapi.com/v1/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api:APIinterface by lazy {
        retrofit.create(APIinterface::class.java)
    }
}
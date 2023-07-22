package com.example.weatherappkotlin.Utilities

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query

object ApiUtilities {

    private lateinit var retrofit: Retrofit
    var BASE_URL = "https://api.openweathermap.org/data/2.5/"
    var FORECAST_URL = "api.openweathermap.org/data/2.5/forecast/"

    fun getApiInterface():ApıInterface{
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApıInterface::class.java)
    }

    fun getApiForecast(){

    }


}
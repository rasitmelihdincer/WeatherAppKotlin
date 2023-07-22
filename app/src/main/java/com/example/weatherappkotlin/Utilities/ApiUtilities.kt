package com.example.weatherappkotlin.Utilities

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiUtilities {

    private lateinit var retrofit: Retrofit
    var BASE_URL = "https://api.openweathermap.org/data/2.5/"

    fun getApiInterface():ApıInterface{
        if (retrofit == null){
            retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        }
        return retrofit.create(ApıInterface::class.java)
    }


}
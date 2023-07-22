package com.example.weatherappkotlin.Utilities

import com.example.weatherappkotlin.Models.ModelClass
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApıInterface {

    @GET("weather")
    fun getCurrentWeatherData(
        @Query("lat") latitude : String,
        @Query("lot") longitude : String,
        @Query("ApıKey") apiKey : String,
    ):Call<ModelClass>

    @GET("weather")
    fun getCityWeatherData(
        @Query("q") cityName : String,
        @Query("ApıKey") apiKey : String,
    ):Call<ModelClass>


}
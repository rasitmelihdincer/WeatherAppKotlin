package com.example.weatherappkotlin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.location.LocationManager
import android.media.audiofx.Equalizer.Settings
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.weatherappkotlin.Models.ModelClass
import com.example.weatherappkotlin.Utilities.ApiUtilities
import com.example.weatherappkotlin.Utilities.ApıInterface
import com.example.weatherappkotlin.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Query
import java.util.Date

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationProvideClient : FusedLocationProviderClient
    private lateinit var binding : ActivityMainBinding
    private lateinit var weather : ModelClass
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProvideClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()
        binding.searchText.setOnEditorActionListener { v, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getCityWeather(binding.searchText.text.toString())
                val view = this.currentFocus
                if (view != null) {
                    val imm : InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken,0)
                    binding.searchText.clearFocus()
                }
                true
            } else false

        }
    }

    private fun getCityWeather(cityName: String) {
        ApiUtilities.getApiInterface().getCityWeatherData(cityName, API_KEY).enqueue(object : Callback<ModelClass>{
            override fun onResponse(call: Call<ModelClass>, response: Response<ModelClass>) {
                setDataOnView(response.body())
            }

            override fun onFailure(call: Call<ModelClass>, t: Throwable) {
                println("satır 65")
            }

        })
    }

    private fun getCurrentLocation(){


        if (checkPermission()){
            if (isLocationEnabled()){
            // latitute and longitute
                fusedLocationProvideClient.lastLocation.addOnCompleteListener(this){ task->
                    val location : Location? = task.result
                    if (location == null){
                        Toast.makeText(this,"Null Received",Toast.LENGTH_LONG).show()
                        println("location null")
                    } else{
                        fetchCurrentLocationWeather(location.latitude.toString(),location.longitude.toString())
                        println(location.latitude)
                        println(location.longitude)
                    }

                }

            }else{  //settings
                Toast.makeText(this,"Turn on Location",Toast.LENGTH_LONG).show()
                val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else{  // request Permisson
            requestPermisson()
        }

    }

    private fun fetchCurrentLocationWeather(latitude: String, longitude: String) {
        ApiUtilities.getApiInterface().getCurrentWeatherData(latitude,longitude, API_KEY).enqueue(object : Callback<ModelClass>{
            override fun onResponse(call: Call<ModelClass>, response: Response<ModelClass>) {
                if (response.isSuccessful){
                   setDataOnView(response.body())
                }
            }

            override fun onFailure(call: Call<ModelClass>, t: Throwable) {
                Toast.makeText(this@MainActivity,"ERROR",Toast.LENGTH_LONG).show()
                println("hata")
            }

        })




    }

    private fun setDataOnView(body: ModelClass?) {
        val sdf = SimpleDateFormat("dd/mm/yyyy hh:mm")
        val currentDate = sdf.format(Date())
        binding.dateTime.text = currentDate

        binding.temp.text = body?.main?.temp.toString()

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermission() : Boolean{
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED
            &&
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            return true
        }
        return false
    }
    private fun requestPermisson(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_ACCESS_LOCATION)
    }
    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
        private const val API_KEY = "c04ff650f1079fffa038165004ae1653"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Granted",Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this,"Denied",Toast.LENGTH_LONG).show()
            }
        }
    }

}
package com.example.tareaapiweatherdavid.fragments

import com.example.tareaapiweatherdavid.R
import com.example.tareaapiweatherdavid.pojo.AttributesCities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class Compare : Fragment() {
    private var oneList = MutableLiveData<ArrayList<AttributesCities>>()
    private var secondList = MutableLiveData<ArrayList<AttributesCities>>()
    private val meAPI = "c8986d01d046f289b7280a5efbf50b60"
    private val lang = "es"
    private val units = "metric"
    override fun onCreateView(
        inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?
    ):View? {
        val v = inflater.inflate(R.layout.compare_fragment, container, false)
        val navigation:CompareArgs by navArgs()
        val firstCityCompare = navigation.FirstCity
        val secondCityCompare = navigation.SecondCity
        getFirstCity(v, firstCityCompare, 1)
        getSecondCity(v, secondCityCompare, 2)
        return v
    }

    private fun getFirstCity(v:View, city:String, orderCity:Int) {
        val iconFirst = v.findViewById<ImageView>(R.id.imageViewFirst)
        val descriptionFirst = v.findViewById<TextView>(R.id.textViewDescriptionFirst)
        val nameFirst = v.findViewById<TextView>(R.id.textViewnameFirst)
        val temperatureFirst = v.findViewById<TextView>(R.id.textViewtemperatureFirst)
        val feelsLikeFirst = v.findViewById<TextView>(R.id.textViewfeelslikeFirst)
        val tempMaxFirst = v.findViewById<TextView>(R.id.textViewtempMaxFirst)
        val tempMinFirst = v.findViewById<TextView>(R.id.textViewtempMinFirst)
        val humidityFirst = v.findViewById<TextView>(R.id.textViewhumidityFirst)
        val pressureFirst = v.findViewById<TextView>(R.id.textViewpressureFirst)
        val speedFirst = v.findViewById<TextView>(R.id.textViewvelocityFirst)
        val allFirst = v.findViewById<TextView>(R.id.textViewallFirst)
        val countryFirst = v.findViewById<TextView>(R.id.textViewcountryFirst)
        getApiOpenWeather(city, orderCity)
        oneList.observe(viewLifecycleOwner, Observer {
            val oneStr:String = oneList.value!![0].description.substring(0, 1).toUpperCase(Locale.ROOT)
            val secondStr:String = oneList.value!![0].description.substring(1)
            val threeStr = oneStr + secondStr
            descriptionFirst.text = threeStr
            Picasso.get().load("https://openweathermap.org/img/w/" + oneList.value!![0].icon + ".png").into(iconFirst)
            nameFirst.text = oneList.value!![0].name
            temperatureFirst.text = oneList.value!![0].temperature.toString() + "°C"
            feelsLikeFirst.text = "Sensación térmica: " + oneList.value!![0].feelsLike.toString() + "°C"
            tempMaxFirst.text = "MÁXIMAS: " + oneList.value!![0].tempMax.toString() + "°C"
            tempMinFirst.text = "MÍNIMAS: " + oneList.value!![0].tempMin.toString() + "°C"
            pressureFirst.text = "PRESIÓN: " + oneList.value!![0].pressure.toString() + "hPA"
            humidityFirst.text = "HUMEDAD: " + oneList.value!![0].humidity.toString() + "%"
            speedFirst.text = "VELOCIDAD: " + oneList.value!![0].speed + "%"
            allFirst.text = "NUBES: " + oneList.value!![0].all + "%"
            countryFirst.text = "PAÍS: " + oneList.value!![0].country
        })
    }

    private fun getSecondCity(v:View, city:String, orderCity:Int) {
        val iconSecond = v.findViewById<ImageView>(R.id.imageViewSecond)
        val descriptionSecond = v.findViewById<TextView>(R.id.textViewDescriptionSecond)
        val nameSecond = v.findViewById<TextView>(R.id.textViewnameSecond)
        val temperatureSecond = v.findViewById<TextView>(R.id.textViewtemperatureSecond)
        val feelsLikeSecond = v.findViewById<TextView>(R.id.textViewfeelslikeSecond)
        val tempMaxSecond = v.findViewById<TextView>(R.id.textViewtempMaxSecond)
        val tempMinSecond = v.findViewById<TextView>(R.id.textViewtempMinSecond)
        val pressureSecond = v.findViewById<TextView>(R.id.textViewpressureSecond)
        val humiditySecond = v.findViewById<TextView>(R.id.textViewhumiditySecond)
        val speedSecond = v.findViewById<TextView>(R.id.textViewvelocitySecond)
        val allSecond = v.findViewById<TextView>(R.id.textViewallSecond)
        val countrySecond = v.findViewById<TextView>(R.id.textViewcountrySecond)
        getApiOpenWeather(city, orderCity)
        secondList.observe(viewLifecycleOwner, Observer {
            val oneList:String = secondList.value!![0].description.substring(0, 1).toUpperCase(Locale.ROOT)
            val secondStr:String = secondList.value!![0].description.substring(1)
            val threeStr = oneList + secondStr
            Picasso.get().load("https://openweathermap.org/img/w/" + secondList.value!![0].icon + ".png").into(iconSecond)
            descriptionSecond.text = threeStr
            nameSecond.text = secondList.value!![0].name
            temperatureSecond.text = secondList.value!![0].temperature.toString() + "°C"
            feelsLikeSecond.text = "Sensación térmica: " + secondList.value!![0].feelsLike.toString() + "°C"
            tempMaxSecond.text = "MÁXIMAS: " + secondList.value!![0].tempMax.toString() + "°C"
            tempMinSecond.text = "MÍNIMAS: " + secondList.value!![0].tempMin.toString() + "°C"
            pressureSecond.text = "PRESIÓN: " + secondList.value!![0].pressure.toString() + "hPA"
            humiditySecond.text = "HUMEDAD: " + secondList.value!![0].humidity.toString() + "%"
            speedSecond.text = "VELOCIDAD: " + secondList.value!![0].speed + "%"
            allSecond.text = "NUBES: " + secondList.value!![0].all + "%"
            countrySecond.text = "PAÍS: " + secondList.value!![0].country
        })
    }

    private fun getApiOpenWeather(nameCity:String, orderCity:Int) {
        val url = "https://api.openweathermap.org/data/2.5/weather?q=$nameCity&lang=$lang&units=$units&appid=$meAPI"
        val requestQueue = Volley.newRequestQueue(requireContext())
        var icon:String = ""
        var description:String = ""
        var name:String = ""
        var temperature:Int = 1
        var feelsLike:Int = 2
        var tempMax:Int = 3
        var tempMin:Int = 4
        var humidity:Int = 5
        var pressure:Int = 6
        var speed:String = ""
        var all:String = ""
        var country:String = ""
        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val main = response.optJSONObject("main")
                val wind = response.optJSONObject("wind")
                val clouds = response.optJSONObject("clouds")
                val sys = response.optJSONObject("sys")
                val list = ArrayList<AttributesCities>()
                list.clear()
                val weather = response.optJSONArray("weather")
                name = response.getString("name")
                for (i in 0 until weather.length()) {
                    val weather = weather.getJSONObject(i)
                    icon = weather.getString("icon")
                    description = weather.getString("description")
                }
                temperature = main.getInt("temp")
                feelsLike = main.getInt("feels_like")
                tempMax = main.getInt("temp_max")
                tempMin = main.getInt("temp_min")
                pressure = main.getInt("pressure")
                humidity = main.getInt("humidity")
                speed = wind.getString("speed")
                all = clouds.getString("all")
                country = sys.getString("country")
                list.add(AttributesCities(icon, name, description, temperature, feelsLike, tempMax, tempMin, pressure, humidity, speed, all, country))
                if (orderCity == 1) {
                    oneList.value = list
                }
                if (orderCity == 2) {
                    secondList.value = list
                }
            } catch (e:JSONException) {
                e.printStackTrace()
            }
        },
            { error ->
                error.printStackTrace()
            })
        requestQueue.add(request)
    }
}
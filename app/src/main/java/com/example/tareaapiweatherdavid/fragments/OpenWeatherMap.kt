package com.example.tareaapiweatherdavid.fragments

import com.example.tareaapiweatherdavid.R
import com.example.tareaapiweatherdavid.database.Cities
import com.example.tareaapiweatherdavid.database.DataRepository
import com.example.tareaapiweatherdavid.model.MyViewModel
import com.example.tareaapiweatherdavid.pojo.AttributesCities
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class OpenWeatherMap : Fragment() {
    private var list = MutableLiveData<ArrayList<AttributesCities>>()
    lateinit var nameCities:String
    lateinit var icon:ImageView
    lateinit var description:TextView
    lateinit var name:TextView
    lateinit var temperature:TextView
    lateinit var feelsLike:TextView
    lateinit var tempMax:TextView
    lateinit var tempMin:TextView
    lateinit var humidity:TextView
    lateinit var pressure:TextView
    lateinit var speed:TextView
    lateinit var all:TextView
    lateinit var country:TextView
    lateinit var like:MenuItem
    lateinit var dislike:MenuItem
    private val meAPI = "c8986d01d046f289b7280a5efbf50b60"
    private val lang = "es"
    private val units = "metric"
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?
    ):View? {
        val v = inflater.inflate(R.layout.main_fragment, container, false)
        val viewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
        val cities = viewModel.getMeCity()
        icon = v.findViewById(R.id.imageView)
        description = v.findViewById(R.id.textViewDescription)
        name = v.findViewById(R.id.textViewname)
        temperature = v.findViewById(R.id.textViewtemperature)
        feelsLike = v.findViewById(R.id.textViewfeelslike)
        tempMax = v.findViewById(R.id.textViewtempMax)
        tempMin = v.findViewById(R.id.textViewtempMin)
        humidity = v.findViewById(R.id.textViewhumidity)
        pressure = v.findViewById(R.id.textViewpressure)
        speed = v.findViewById(R.id.textViewvelocity)
        all = v.findViewById(R.id.textViewall)
        country = v.findViewById(R.id.textViewcountry)
        nameCities = if (cities.name != "") {
            cities.name
        } else {
            "Madrid"
        }
        getApiOpenWeather(nameCities)
        getInformationCities()
        return v
    }

    override fun onOptionsItemSelected(item:MenuItem):Boolean {
        val dataRepository = DataRepository(requireContext())
        val preferences = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val user = preferences?.getString("user", "")
        val likes = dataRepository.getFavoritesCities(user.toString(), nameCities)
        return when (item.itemId) {
            R.id.like -> {
                if (user != null) {
                    dataRepository.getCRUDInsertCities(Cities(nameCities), user)
                    if (!likes) {
                        dislike.isVisible = true
                        like.isVisible = false
                    }
                }
                Toast.makeText(context, "Añadida a favoritos", Toast.LENGTH_LONG).show()
                true
            }
            R.id.dislike -> {
                if (user != null) {
                    dataRepository.getCRUDeleteCities(user, nameCities)
                    if (likes) {
                        like.isVisible = true
                        dislike.isVisible = false
                    }
                }
                Toast.makeText(context, "Eliminada de favoritos", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu:Menu, inflater:MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
        val preferences = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val user = preferences?.getString("user", "")
        val dataRepository = DataRepository(requireContext())
        val likes = dataRepository.getFavoritesCities(user.toString(), nameCities)
        like = menu.findItem(R.id.like)
        dislike = menu.findItem(R.id.dislike)
        if (likes) {
            dislike.isVisible = true
            like.isVisible = false
        } else if (!likes) {
            like.isVisible = true
            dislike.isVisible = false
        }

        val menuItemSearch = menu.findItem(R.id.app_bar_search)
        menuItemSearch.isVisible = true
        val searchView = menuItemSearch.actionView as SearchView
        searchView.queryHint = "Buscar"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query:String?):Boolean {
                val oneStr:String = list.value!![0].description.substring(0, 1).toUpperCase(Locale.ROOT)
                val secondStr:String = list.value!![0].description.substring(1)
                val threeStr = oneStr + secondStr
                getApiOpenWeather(query.toString())
                list.observe(viewLifecycleOwner, Observer {
                    Picasso.get().load("https://openweathermap.org/img/w/" + list.value!![0].icon + ".png").into(icon)
                    nameCities = list.value!![0].name
                    description.text = threeStr
                    name.text = list.value!![0].name
                    temperature.text = list.value!![0].temperature.toString() + "°C"
                    feelsLike.text = "Sensación térmica: " + list.value!![0].feelsLike.toString() + "°C"
                    tempMax.text = "MÁXIMAS: " + list.value!![0].tempMax.toString() + "°C"
                    tempMin.text = "MÍNIMAS: " + list.value!![0].tempMin.toString() + "°C"
                    pressure.text = "PRESIÓN: " + list.value!![0].pressure.toString() + "hPa"
                    humidity.text = "HUMEDAD: " + list.value!![0].humidity.toString() + "%"
                    speed.text = "VELOCIDAD: " + list.value!![0].speed + "%"
                    all.text = "NUBES: " + list.value!![0].all+ "%"
                    country.text = "PAÍS: " + list.value!![0].country
                    Toast.makeText(context, "Has buscado: $nameCities", Toast.LENGTH_SHORT).show()
                    val likes = dataRepository.getFavoritesCities(user.toString(), nameCities)
                    if (likes) {
                        dislike.isVisible = true
                        like.isVisible = false
                    } else if (!likes) {
                        like.isVisible = true
                        dislike.isVisible = false
                    }
                    val viewModel =
                        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
                    viewModel.setMeCity(Cities(nameCities))

                })
                return true
            }

            override fun onQueryTextChange(newText:String?):Boolean {
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getApiOpenWeather(nameCity:String) {
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
                    val weather = response.optJSONArray("weather")
                    list.clear()
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
                    this.list.value = list
                } catch (e:JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            })
        requestQueue.add(request)
    }

    private fun getInformationCities() {
        list.observe(viewLifecycleOwner, Observer {
            nameCities = list.value!![0].name
            val viewModel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
            viewModel.setMeCity(Cities(nameCities))
            val oneStr:String = list.value!![0].description.substring(0, 1).toUpperCase(Locale.ROOT)
            val secondStr:String = list.value!![0].description.substring(1)
            val threeStr = oneStr + secondStr
            Picasso.get().load("https://openweathermap.org/img/w/" + list.value!![0].icon + ".png").into(icon)
            description.text = threeStr
            name.text = list.value!![0].name
            temperature.text = list.value!![0].temperature.toString() + "°C"
            feelsLike.text = "Sensación térmica: " + list.value!![0].feelsLike.toString() + "°C"
            tempMax.text = "MÁXIMAS: " + list.value!![0].tempMax.toString() + "°C"
            tempMin.text = "MÍNIMAS: " + list.value!![0].tempMin.toString() + "°C"
            pressure.text = "PRESIÓN: " + list.value!![0].pressure.toString() + "hPA"
            humidity.text = "HUMEDAD: " + list.value!![0].humidity.toString() + "%"
            speed.text = "VELOCIDAD: " + list.value!![0].speed + "%"
            all.text = "NUBES: " + list.value!![0].all + "%"
            country.text = "PAÍS: " + list.value!![0].country
        })
    }
}
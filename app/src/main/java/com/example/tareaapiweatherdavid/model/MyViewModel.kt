package com.example.tareaapiweatherdavid.model

import com.example.tareaapiweatherdavid.database.Cities
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    private var meCity:Cities
    fun getMeCity():Cities {
        return meCity
    }

    fun setMeCity(cities:Cities) {
        meCity = cities
    }

    init {
        meCity = Cities("")
    }
}
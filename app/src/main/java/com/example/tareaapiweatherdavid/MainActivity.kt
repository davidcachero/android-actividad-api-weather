package com.example.tareaapiweatherdavid

import android.content.Context
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(item:MenuItem):Boolean {
        val preferences = this.getSharedPreferences("casilla", Context.MODE_PRIVATE)
        val change:SharedPreferences.Editor = preferences.edit()
        val nav = findNavController(R.id.nav_host_fragment)
        return when (item.itemId) {
            R.id.home -> {
                change.putString("Sha512", "false")
                change.apply()
                item.onNavDestinationSelected(nav)
            }
            R.id.OpenWeatherMap -> {
                return item.onNavDestinationSelected(nav)
            }
            R.id.Favorites -> {
                return item.onNavDestinationSelected(nav)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
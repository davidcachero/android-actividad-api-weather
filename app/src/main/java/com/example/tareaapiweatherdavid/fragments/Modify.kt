package com.example.tareaapiweatherdavid.fragments

import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tareaapiweatherdavid.R
import com.example.tareaapiweatherdavid.database.DataRepository
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import org.json.JSONException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Modify : Fragment() {
    lateinit var user:String
    private lateinit var editTextCity:EditText
    private lateinit var oneCity:String
    private val meAPI = "c8986d01d046f289b7280a5efbf50b60"
    private val lang = "es"
    private val units = "metric"
    override fun onCreateView(
        inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?
    ):View? {
        val v = inflater.inflate(R.layout.modify_fragment, container, false)
        val btnUpdate = v.findViewById<Button>(R.id.btnModify)
        val args:ModifyArgs by navArgs()
        val preferences = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        user = preferences?.getString("user", "").toString()
        oneCity = args.city
        editTextCity = v.findViewById(R.id.editTextnameModifyCity)
        editTextCity.setText(oneCity)
        btnUpdate.setOnClickListener {
            onUpdateCities()
            Toast.makeText(context, "Ciudad modificada correctamente", Toast.LENGTH_SHORT).show()
        }
        return v
    }

    private fun onUpdateCities() {
        val dataRepository = DataRepository(requireContext())
        var secondCity = editTextCity.text.toString()
        var str:String
        val url =
            "https://api.openweathermap.org/data/2.5/weather?q=$secondCity&lang=$lang&units=$units&appid=$meAPI"
        val requestQueue = Volley.newRequestQueue(requireContext())
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    str = response.getString("cod")
                    secondCity = response.getString("name")
                    if (str == "200") {
                        dataRepository.getCRUDUpdateCities(user, oneCity, secondCity)
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
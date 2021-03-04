package com.example.tareaapiweatherdavid.fragments

import com.example.tareaapiweatherdavid.R
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlin.system.exitProcess

class Home : Fragment() {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Salir")
                builder.setMessage("¿Quieres salir de la aplicacion?")
                builder.setPositiveButton("SI") { dialog, _ ->
                    dialog.dismiss()
                    exitProcess(-1)
                }
                builder.setNegativeButton("NO") { dialog, _ -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()
            }
        })
    }

    override fun onCreateView(
        inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?
    ):View? {
        val v = inflater.inflate(R.layout.home_fragment, container, false)
        val buttonLogin = v.findViewById<Button>(R.id.btnLogin)
        val buttonRegister = v.findViewById<Button>(R.id.buttonRegister)
        val preferences = activity?.getSharedPreferences("casilla", Context.MODE_PRIVATE)
        val checkBox = preferences?.getString("Sha512", "")
        if (checkBox.equals("true")) {
            findNavController().navigate(R.id.action_homeFragment_to_mainWeather)
        } else if (checkBox.equals("false")) {
        }
        buttonLogin.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
        buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_registerFragment)
        }
        return v
    }
}
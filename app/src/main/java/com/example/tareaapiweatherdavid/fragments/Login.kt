package com.example.tareaapiweatherdavid.fragments

import com.example.tareaapiweatherdavid.R
import com.example.tareaapiweatherdavid.database.DataRepository
import com.example.tareaapiweatherdavid.utils.ShaSavePassword
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class Login : Fragment() {
    private lateinit var editTextUser:EditText
    private lateinit var editTextPassword:EditText
    lateinit var passwordSha:String
    lateinit var saveShaPassword:CheckBox
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?
    ):View? {
        val v = inflater.inflate(R.layout.login_fragment, container, false)
        val btnRegister = v.findViewById<Button>(R.id.btnLogin)
        val preferences = activity?.getSharedPreferences("casilla", Context.MODE_PRIVATE)
        val checkBox = preferences?.getString("Sha512", "")
        editTextUser = v.findViewById(R.id.editTextUser)
        editTextPassword = v.findViewById(R.id.editTextPassword)
        if (checkBox.equals("true")) {
            findNavController().navigate(R.id.action_loginFragment_to_mainWeather)
        } else if (checkBox.equals("false")) {
            Toast.makeText(context, "Por favor, inice sesión", Toast.LENGTH_SHORT).show()
        }
        btnRegister.setOnClickListener {
            logIn()
        }
        return v
    }

    private fun logIn() {
        val savePassword = ShaSavePassword
        val dataRepository = DataRepository(requireContext())
        var preferences = activity?.getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = preferences!!.edit()
        saveShaPassword = requireView().findViewById(R.id.recorder)
        passwordSha = savePassword.sha512(editTextPassword.text.toString())
        if (dataRepository.getUser() == 0) {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        } else {
            if (dataRepository.getLogIn(editTextUser.text.toString(), passwordSha)) {
                editor.putString("user", editTextUser.text.toString())
                editor.apply()
                if (saveShaPassword.isChecked) {
                    preferences = activity?.getSharedPreferences("casilla", Context.MODE_PRIVATE)
                    val editor:SharedPreferences.Editor = preferences!!.edit()
                    editor.putString("Sha512", "true")
                    editor.apply()
                } else if (!saveShaPassword.isChecked) {
                    preferences = activity?.getSharedPreferences("casilla", Context.MODE_PRIVATE)
                    val editor:SharedPreferences.Editor = preferences!!.edit()
                    editor.putString("Sha512", "false")
                    editor.apply()
                }
                findNavController().navigate(R.id.action_loginFragment_to_mainWeather)
                Toast.makeText(requireContext(), "Bienvenid@ a OpenWeather", Toast.LENGTH_LONG)
                    .show()
            } else
                Toast.makeText(requireContext(), "Usuario/Contraseña incorrecta", Toast.LENGTH_LONG)
                    .show()
        }
    }
}
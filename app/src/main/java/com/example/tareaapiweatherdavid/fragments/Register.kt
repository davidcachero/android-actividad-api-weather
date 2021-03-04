package com.example.tareaapiweatherdavid.fragments

import com.example.tareaapiweatherdavid.R
import com.example.tareaapiweatherdavid.database.DataRepository
import com.example.tareaapiweatherdavid.database.Users
import com.example.tareaapiweatherdavid.utils.ShaSavePassword
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class Register : Fragment() {
    private lateinit var passwordSha:String
    private lateinit var editTextUser:EditText
    private lateinit var editTextPassword:EditText
    private lateinit var editTextName:EditText
    private lateinit var btnRegister:Button
    override fun onCreateView(
        inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?
    ):View? {
        val v = inflater.inflate(R.layout.register_fragment, container, false)
        btnRegister = v.findViewById(R.id.buttonRegister)
        editTextUser = v.findViewById(R.id.editTextRegisterUser)
        editTextPassword = v.findViewById(R.id.editTextRegisterPassword)
        editTextName = v.findViewById(R.id.editTextRegisterName)
        btnRegister.setOnClickListener {
            register()
            Toast.makeText(context, "Te has registrado correctamente", Toast.LENGTH_SHORT).show()
        }
        return v
    }

    private fun register() {
        val dataRepository = DataRepository(requireContext())
        val passwordSecure = ShaSavePassword
        passwordSha = passwordSecure.sha512(editTextPassword.text.toString())
        if (dataRepository.getCRUDInsert(
                Users(
                    editTextUser.text.toString(),
                    passwordSha,
                    editTextName.text.toString()
                )
            ) == -1
        ) {
        }
    }
}
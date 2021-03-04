package com.example.tareaapiweatherdavid.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Users (
    @PrimaryKey(autoGenerate = false)
    val user: String,
    val password: String,
    val name: String
)
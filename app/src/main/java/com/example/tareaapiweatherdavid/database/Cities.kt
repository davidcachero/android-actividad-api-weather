package com.example.tareaapiweatherdavid.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cities(
    @PrimaryKey(autoGenerate = false)
    val name: String
)
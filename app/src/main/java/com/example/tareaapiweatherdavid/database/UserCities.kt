package com.example.tareaapiweatherdavid.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

data class UserCities(
    @Embedded val users: Users,
    @Relation(
        parentColumn = "user",
        entityColumn = "name",
        associateBy = Junction(UserCitiesCrossRef::class)
    )
    val cities: List<Cities>
)

@Entity(primaryKeys = ["user", "name"])
data class UserCitiesCrossRef(
    val user: String,
    val name: String
)
package com.example.tareaapiweatherdavid.database

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun getCRUDInsert(users:Users)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCity(cities:Cities)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCitiesCrossRef(crossRef:UserCitiesCrossRef)

    @Query("SELECT COUNT(*) FROM users WHERE user = :user AND password = :password")
    suspend fun getLogIn(user:String, password:String):Int?

    @Query("SELECT COUNT(*) FROM Cities")
    suspend fun numCities():Int?

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUser():Int?

    @Query("DELETE FROM UserCitiesCrossRef WHERE user = :user AND name = :cities")
    suspend fun getCRUDeleteCities(user:String, cities:String)

    @Transaction
    @Query("SELECT * FROM users WHERE user = :user")
    suspend fun getCities(user:String):List<UserCities>

    @Transaction
    @Query("SELECT COUNT(*) FROM UserCitiesCrossRef WHERE user = :user AND name = :cities")
    suspend fun getFavoritesCities(user:String, cities:String):Int?

    @Transaction
    @Query("UPDATE UserCitiesCrossRef SET name = :secondCity WHERE user = :user AND name= :oneCity")
    suspend fun getCRUDUpdateCities(user:String, oneCity:String, secondCity:String):Int?
}
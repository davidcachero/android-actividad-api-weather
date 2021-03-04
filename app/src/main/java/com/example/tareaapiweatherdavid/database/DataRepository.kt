package com.example.tareaapiweatherdavid.database

import android.content.Context
import kotlinx.coroutines.*

class DataRepository(context:Context) {
    fun getUser():Int = runBlocking {
        userDao!!.getUser()!!
    }

    fun getLogIn(user:String, password:String):Boolean {
        val job:Job
        job = CoroutineScope(Dispatchers.IO).async {
            userDao!!.getLogIn(user, password)!!
        }
        return runBlocking {
            job.await() == 1
        }
    }

    private val userDao:UserDao? = AppDatabase.getInstance(context)?.userDao()
    fun getCities(user:String):List<UserCities> = runBlocking {
        userDao!!.getCities(user)
    }

    fun getFavoritesCities(user:String, city:String):Boolean {
        val job:Job
        job = CoroutineScope(Dispatchers.IO).async {
            userDao!!.getFavoritesCities(user, city)!!
        }
        return runBlocking {
            job.await() == 1
        }
    }

    fun getCRUDInsert(users:Users):Int {
        if (userDao != null) {
            CoroutineScope(Dispatchers.IO).launch {
                userDao.getCRUDInsert(users)
            }
            return 0
        }
        return -1
    }

    fun getCRUDInsertCities(cities:Cities, user:String):Int {
        if (userDao != null) {
            CoroutineScope(Dispatchers.IO).launch {
                userDao.insertCity(cities)
                userDao.insertUserCitiesCrossRef(
                    UserCitiesCrossRef(user, cities.name)
                )
            }
            return 0
        }
        return -1
    }

    fun getCRUDUpdateCities(user:String, oneCity:String, secondCity:String) = runBlocking {
        userDao!!.insertCity(Cities(secondCity))
        userDao.getCRUDUpdateCities(user, oneCity, secondCity)
    }

    fun getCRUDeleteCities(user:String, city:String) = runBlocking {
        userDao!!.getCRUDeleteCities(user, city)
    }
}
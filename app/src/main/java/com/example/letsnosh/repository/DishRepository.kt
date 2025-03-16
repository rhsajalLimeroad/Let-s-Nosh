package com.example.letsnosh.repository

import android.util.Log
import com.example.letsnosh.RetrofitInstance
import com.example.letsnosh.data.Dish

class DishRepository {

    private val api = RetrofitInstance.api

    suspend fun fetchDishes(): List<Dish>? {
        return try {
            Log.d("kya mila?", "fetchDishes")
            api.getDishes()
        } catch (e: Exception) {
            Log.d("kya mila?", "error ==> $e")
            e.printStackTrace()
            null
        }
    }
}
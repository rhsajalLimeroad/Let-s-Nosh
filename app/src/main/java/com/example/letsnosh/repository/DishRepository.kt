package com.example.letsnosh.repository

import android.util.Log
import com.example.letsnosh.RetrofitInstance
import com.example.letsnosh.data.Dish
import retrofit2.Response

class DishRepository {

    private val api = RetrofitInstance.api

    suspend fun fetchDishes(): Response<List<Dish>>? {
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
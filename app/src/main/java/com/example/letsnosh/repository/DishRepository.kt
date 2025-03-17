package com.example.letsnosh.repository

import android.util.Log
import com.example.letsnosh.RetrofitInstance
import com.example.letsnosh.data.Dish
import retrofit2.Response

class DishRepository {

    private val api = RetrofitInstance.api

    suspend fun fetchDishes(): Response<List<Dish>>? {
        return try {
            api.getDishes()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
package com.example.letsnosh.repository

import com.example.letsnosh.RetrofitInstance
import com.example.letsnosh.data.Dish

class DishRepository {

    private val api = RetrofitInstance.api

    suspend fun fetchDishes(): List<Dish>? {
        return try {
            api.getDishes()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
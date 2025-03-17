package com.example.letsnosh.helper

import android.content.Context
import com.example.letsnosh.data.Dish
import com.example.letsnosh.helper.DishStorageHelper.Constants.KEY_DISH_DATA
import com.google.gson.Gson

class DishStorageHelper(context: Context) {

    object Constants {
        const val STORAGE_NAME = "dish_storage"
        const val KEY_DISH_DATA = "key_dish_data"
    }

    private val preferences = context.getSharedPreferences(Constants.STORAGE_NAME, Context.MODE_PRIVATE)
    private val jsonConverter = Gson()

    fun storeDishData(dish: Dish) {
        val dishString = jsonConverter.toJson(dish)
        preferences.edit().apply {
            putString(KEY_DISH_DATA, dishString)
            apply()
        }
    }

    fun retrieveDishData(): Dish? = preferences.getString(KEY_DISH_DATA, null)?.let {
        jsonConverter.fromJson(it, Dish::class.java)
    }

    fun clearDishData() {
        preferences.edit().remove(KEY_DISH_DATA).apply()
    }
}
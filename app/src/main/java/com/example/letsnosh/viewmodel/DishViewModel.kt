package com.example.letsnosh.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.letsnosh.data.Dish
import com.example.letsnosh.repository.DishRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DishViewModel : ViewModel() {
    private val repository = DishRepository()

    private val _dishes = MutableLiveData<List<Dish>>()
    val dishes: LiveData<List<Dish>> get() = _dishes

    fun loadDishes() {
        Log.d("kya mila?", "loadDishes")
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.fetchDishes()
                }
                Log.d("kya mila?", "viewModelScope ==> $response")
                if (response != null) {
                    _dishes.value = response!!
                }
            } catch (e: Exception) {
                Log.d("error", "Some error occurred ==> $e")
            }
        }
    }
}
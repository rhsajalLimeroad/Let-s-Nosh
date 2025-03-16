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

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadDishes() {
        Log.d("kya mila?", "loadDishes")
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.fetchDishes()
                }
                Log.d("kya mila?", "viewModelScope ==> $response")
                if (response != null) {
                    if(response.isSuccessful && response.body() != null) {
                        _dishes.postValue(response.body())
                    } else {
                        _error.postValue("Error: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                Log.d("error", "Some error occurred ==> $e")
            }
        }
    }
}
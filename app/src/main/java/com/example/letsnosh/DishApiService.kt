package com.example.letsnosh

import com.example.letsnosh.data.Dish
import retrofit2.Response
import retrofit2.http.GET

interface DishApiService {
    @GET("dev/nosh-assignment")
    suspend fun getDishes(): Response<List<Dish>>
}
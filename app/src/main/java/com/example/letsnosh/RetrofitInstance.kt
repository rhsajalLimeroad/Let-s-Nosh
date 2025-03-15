package com.example.letsnosh

import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType

object RetrofitInstance {

    private const val BASE_URL = "https://fls8oe8xp7.execute-api.ap-south-1.amazonaws.com/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    val api: DishApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(DishApiService::class.java)
    }
}
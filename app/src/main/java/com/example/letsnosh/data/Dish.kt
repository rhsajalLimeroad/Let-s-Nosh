package com.example.letsnosh.data

import kotlinx.serialization.Serializable

@Serializable
data class Dish(
    val dishName: String,
    val dishId: String,
    val imageUrl: String,
    val isPublished: Boolean
)

package com.example.travenor_customerapp.data.model

data class Destination(
    val id: String,
    val name: String,
    val location: String,
    val imageRes: Int,
    val rating: Double,
    val pricePerPersonUsd: Int,
    val description: String
)

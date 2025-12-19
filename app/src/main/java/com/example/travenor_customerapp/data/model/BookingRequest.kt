package com.example.travenor_customerapp.data.model

data class BookingRequest(
    val id: String,
    val cityId: String?,
    val customerId: String?,
    val status: String
)
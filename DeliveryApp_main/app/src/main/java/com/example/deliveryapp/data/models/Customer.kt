package com.example.deliveryapp.data.models

data class Customer(
    val addresses: List<String>? = listOf(),
    val email: String = "",
    val name: String = "",
    val password: String = "",
    val phone: String = ""
)
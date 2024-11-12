package com.example.deliveryapp.data.models

data class OrderTracking(
    val orderid: String,
    val sender: String,
    val receiver: String,
    val expectedTime: String,
    var status: String,
    val weight: String,
    val totalAmount: String,
    val driver: String
)
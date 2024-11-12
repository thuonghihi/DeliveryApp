package com.example.deliveryapp.data.models

data class Order(
    val orderidshow: String ? = null,
    val pickupLocation: String? = null,
    val deliveryLocation: String? = null,
    val customer: String? = null,
    val driver: String? = null,
    val status: String? = null,
    val timeStamp: String? = null,
    val voucher: String? = null,
    val totalAmount: String? = null,
    val paymentMethod: String? = null,
    val weight: String? = null
) {
    // Constructor không tham số mặc định cho Firebase
    constructor() : this(null,null, null, null, null, null, null, null, null, null, null)
}

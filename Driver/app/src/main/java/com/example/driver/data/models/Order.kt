package com.example.driver.data.models

data class Order(
    var customer: String? = null,
    var deliveryAddress: String? = null,
    var deliveryLocation: Location? = null,
    var driver: String? = null,
    var orderId: String? = null,
    var orderIdShow: String? = null,
    var paymentMethod: String? = null,
    var pickupAddress: String? = null,
    var pickupLocation: Location? = null,
    var status: String? = null,
    var timeStamp: String? = null,
    var totalAmount: String? = null,
    var voucher: String? = null,
    var weight: String? = null,
    val senderPhone: String? = null,
    val receiverPhone: String? = null,
    val senderName: String? = null,
    val receiverName: String? = null,
    val paymentRecipient: Boolean? = false,
    var note: String? = null
) {
    constructor() : this(
        customer = null,
        deliveryAddress = null,
        deliveryLocation = null,
        driver = null,
        orderId = null,
        orderIdShow = null,
        paymentMethod = null,
        pickupAddress = null,
        pickupLocation = null,
        status = null,
        timeStamp = null,
        totalAmount = null,
        voucher = null,
        note = null,
        senderPhone = null,
        receiverPhone = null,
        senderName = null,
        receiverName = null,
        paymentRecipient = false,
        weight = null
    )
}

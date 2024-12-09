package com.example.driver.data.models

data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    // Constructor không tham số cho Firebase
    constructor() : this(0.0, 0.0)
}
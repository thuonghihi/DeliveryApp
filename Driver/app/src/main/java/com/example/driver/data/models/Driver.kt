package com.example.driver.data.models

data class Driver(
    val id: String = "", // Nếu bạn cần một trường ID, nhưng bạn có thể lấy nó từ DataSnapshot key
    val auto: Boolean = false,
    val availability: Boolean = false,
    val location: Location = Location(), // Thêm lớp Location
    val name: String = "",
    val phone: String = "",
    val rating: Double = 0.0,
    val status: String = ""
) {
    // Constructor không tham số cho Firebase
    constructor() : this(
        id = "",
        auto = false,
        availability = false,
        location = Location(),
        name = "",
        phone = "",
        rating = 0.0,
        status = ""
    )
}


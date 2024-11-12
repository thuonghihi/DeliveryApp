package com.example.driver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driver.data.models.Order
import com.example.driver.data.repositories.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // Hàm này sẽ gọi Repository để lắng nghe thay đổi từ Firebase
    fun getOrderByDriverIdAndStatus(driverId: String, status: String) {
        orderRepository.getOrdersByDriverIdAndStatus(driverId, status) { newOrders ->
            _orders.value = newOrders  // Cập nhật StateFlow với dữ liệu mới
        }
    }
}

package com.example.driver.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driver.data.models.Order
import com.example.driver.data.repositories.OrderRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(private val orderRepository: OrderRepository) : ViewModel() {
    private val _orders = MutableLiveData<List<Order>?>()
    val orders: LiveData<List<Order>?> get() = _orders

    fun fetchOrders(driverId: String, status: String) {
        Log.d("order12345678", "gọi viewmodel")
        viewModelScope.launch {
            val orders = orderRepository.getOrdersByDriverIdAndStatus(driverId, status)
            _orders.value = orders
            Log.d("order12345678", "list in viewmodel: " + _orders.value.toString())
        }
    }
}

package com.example.deliveryapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deliveryapp.data.models.Order
import com.example.deliveryapp.data.models.OrderTracking
import com.example.deliveryapp.data.repositories.OrderRepository
import kotlinx.coroutines.launch

class OrderViewModel(private val repository: OrderRepository) : ViewModel() {
    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders
    fun fetchOrders(customerId: String, status: List<String>) {
        viewModelScope.launch {
            repository.getOrders(customerId, status).observeForever { orderList ->
                _orders.postValue(orderList)
            }
        }
    }

    private val _addOrderResult = MutableLiveData<Result<Boolean>>()
    val addOrderResult: LiveData<Result<Boolean>> = _addOrderResult
    fun addOrder(order: Order) {
        viewModelScope.launch {
            val result = repository.addOrder(order)
            _addOrderResult.value = result
        }
    }
}
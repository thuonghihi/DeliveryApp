package com.example.deliveryapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deliveryapp.data.models.Customer
import com.example.deliveryapp.data.models.Order
import com.example.deliveryapp.data.repositories.CustomerRepository
import com.example.deliveryapp.data.repositories.OrderRepository
import com.example.deliveryapp.ui.authentication.Login
import kotlinx.coroutines.launch

class CustomerViewModel(private val repository: CustomerRepository) : ViewModel() {
    private val _customer = MutableLiveData<Customer?>()
    val customer: LiveData<Customer?> get() = _customer

    fun getCustomerById(id: String) {
        viewModelScope.launch {
            val result = repository.getCustomerById(id)
            Log.d("result", result.toString())
            _customer.value = result
        }
    }
}
package com.example.deliveryapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GraphViewModel() : ViewModel() {
    private val _graphData = MutableLiveData<String?>()
    val graphData: LiveData<String?> get() = _graphData

    // Hàm để ghi dữ liệu vào repository
    fun setGraph(value: String) {
        _graphData.value = value
    }
}
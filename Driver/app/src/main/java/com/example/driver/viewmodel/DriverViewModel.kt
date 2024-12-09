package com.example.driver.viewmodel

// DriverViewModel.kt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driver.data.models.Driver
import com.example.driver.data.repositories.DriverRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DriverViewModel(private val driverRepository: DriverRepository) : ViewModel() {
    private val _drivers = MutableStateFlow<List<Driver>?>(null)
    val drivers: StateFlow<List<Driver>?> get() = _drivers

//    init {
//        loadDrivers()
//    }

//    private fun loadDrivers() {
//        viewModelScope.launch {
//            try {
//                _drivers.value = driverRepository.getAllDrivers()
//            } catch (e: Exception) {
//                // Xử lý lỗi nếu cần
//            }
//        }
//    }

//    fun addDriver(driver: Driver) {
//        viewModelScope.launch {
//            try {
//                driverRepository.addDriver(driver)
//                loadDrivers() // Cập nhật danh sách tài xế sau khi thêm
//            } catch (e: Exception) {
//                // Xử lý lỗi nếu cần
//            }
//        }
//    }

    private val _driver = MutableLiveData<Driver?>()
    val driver: LiveData<Driver?> get() = _driver
    suspend fun getDriverById(id: String) {
        try {
            val driver = driverRepository.getDriverById(id)
            _driver.value = driver
        } catch (e: Exception) {
            // Xử lý lỗi nếu cần
        }

    }

    private val _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean> get() = _status
    fun getStatus(id: String) {
        viewModelScope.launch {
            try {
                val status = driverRepository.getStatus(id)
                _status.value = status
            }
            catch (e: Exception){

            }
        }
    }

    suspend fun changeStatus(id: String, status: Boolean){
        try {
            driverRepository.changeStatus(id, status)
            if(!status) {
                _auto.value = false
                driverRepository.changeAuto(id, false)
            }
        } catch (e: Exception) {
            // Xử lý lỗi nếu cần
        }
    }

    private val _auto = MutableLiveData<Boolean>()
    val auto: LiveData<Boolean> get() = _auto
    fun getAuto(id: String) {
        viewModelScope.launch {
            try {
                val auto = driverRepository.getAuto(id)
                _auto.value = auto
            }
            catch (e: Exception){

            }
        }
    }

    suspend fun changeAuto(id: String, auto: Boolean){
        try {
            driverRepository.changeAuto(id, auto)
        } catch (e: Exception) {
            // Xử lý lỗi nếu cần
        }
    }
}

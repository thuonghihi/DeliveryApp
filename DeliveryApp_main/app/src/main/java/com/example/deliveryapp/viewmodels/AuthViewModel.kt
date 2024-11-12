package com.example.deliveryapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.deliveryapp.data.models.Customer
import com.example.deliveryapp.data.repositories.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _loginStatus = MutableLiveData<Pair<Boolean, String?>>() // giá trị dùng bên trong viewmodel
    val loginStatus: MutableLiveData<Pair<Boolean, String?>> get() = _loginStatus //cung cấp trajgn thái đến các đối tượng dùng viewmodel

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            val (success, customerId) = repository.login(phone, password)
            if (success) {
                _loginStatus.value = Pair(true, customerId)
            } else {
                _loginStatus.value = Pair(false, null)
            }
        }
    }
}


//    fun register(email: String, password: String) {
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener { task ->
//                _authState.value = task.isSuccessful
//            }
//    }
//
//    fun logout() {
//        firebaseAuth.signOut()
//        _authState.value = false
//    }
//
//    fun checkAuthState() {
//        _authState.value = firebaseAuth.currentUser != null
//    }
//}
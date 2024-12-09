package com.example.driver.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driver.data.repositories.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine

// File: AuthViewModel.kt
// File: AuthViewModel.kt
// File: AuthViewModel.kt
class AuthViewModel(private val authRepository: AuthRepository = AuthRepository()) : ViewModel() {

    var otpSentStatus by mutableStateOf(false)
        private set

    var signInStatus = MutableLiveData<Pair<Boolean, String?>>()

    var phoneCheckStatus = MutableLiveData<Pair<Boolean, String?>>(Pair(false, null))
        private set

    private lateinit var verificationId: String

    // Hàm kiểm tra số điện thoại
    suspend fun checkPhoneNumber(phoneNumber: String) {
        val exists = authRepository.checkPhoneNumberExists(phoneNumber) // Gọi hàm suspend
        phoneCheckStatus.value = if (exists) {
            Pair(true, null) // Số điện thoại tồn tại
        } else {
            Pair(false, "Số điện thoại không tồn tại") // Số điện thoại không tồn tại
        }
    }

    // Hàm gửi OTP
    fun sendOTP(phoneNumber: String, activity: Activity) {
        viewModelScope.launch {
            val result = authRepository.sendOTP(phoneNumber, activity)
            if (result.first) {
                otpSentStatus = true
                Log.d("guiwr r", "sdfgh")
            } else {
                otpSentStatus = false
            }
        }
    }


    suspend fun verifyOTP(otp: String): Pair<Boolean, String?> {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
        return signInWithCredential(credential)
    }

    suspend fun signInWithCredential(credential: PhoneAuthCredential): Pair<Boolean, String?> {
        return authRepository.signInWithPhoneAuthCredential(credential) // Trả về trực tiếp Pair từ hàm này
    }
}




package com.example.driver.data.repositories

import android.app.Activity
import android.util.Log
import com.example.driver.ui.authentication.Login
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Context
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// File: AuthRepository.kt
class AuthRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val driverRef = FirebaseDatabase.getInstance().getReference("drivers")
    private var verificationId: String = ""

    suspend fun checkPhoneNumberExists(phoneNumber: String): Boolean {
        Log.d("okkkk", firebaseAuth.toString())
        return suspendCancellableCoroutine { continuation ->
            driverRef.orderByChild("phone").equalTo(phoneNumber).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("coooo", "sdfg")
                    continuation.resume(snapshot.exists()) // Trả về kết quả
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("kooo", "rfg")
                    continuation.resume(false) // Xử lý khi có lỗi
                }
            })
        }
    }

    suspend fun sendOTP(phoneNumber: String, activity: Activity): Pair<Boolean, String?> {
        return suspendCancellableCoroutine { continuation ->
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Nếu hoàn tất xác thực tự động
                    continuation.resume(Pair(true, null))
                    // Bạn có thể thêm logic đăng nhập tự động ở đây nếu cần
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // Trả về Pair(false, lỗi) nếu gửi OTP thất bại
                    Log.e("OTP Error", "Verification failed: ${e.message}")
                    continuation.resume(Pair(false, e.message))
                }

                override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                    // Lưu lại verificationId và trả về Pair(true, null) nếu gửi thành công
                    verificationId = id
                    continuation.resume(Pair(true, null))
                }
            }

            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()

            // Gửi OTP
            PhoneAuthProvider.verifyPhoneNumber(options)

            continuation.invokeOnCancellation {
                // Dọn dẹp nếu cần
            }
        }
    }


    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential): Pair<Boolean, String?> {
        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Nếu thành công, resume với Pair(true, null)
                        continuation.resume(Pair(true, null))
                    } else {
                        // Nếu thất bại, resume với Pair(false, message lỗi)
                        continuation.resume(Pair(false, task.exception?.message))
                    }
                }
        }
    }
}

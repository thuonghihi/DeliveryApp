package com.example.driver.data.repositories

// DriverRepository.kt
//import com.google.firebase
import android.util.Log
import com.example.driver.data.models.Driver
import com.example.driver.ui.authentication.LoginScreen
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DriverRepository {
    private val driverRef = FirebaseDatabase.getInstance().getReference("drivers")

//    suspend fun getAllDrivers(): List<Driver> {
//        return driversCollection.get().await().toObjects(Driver::class.java)
//    }
//
//    suspend fun addDriver(driver: Driver) {
//        driversCollection.document(driver.id).set(driver).await()
//    }

    suspend fun getDriverById(id: String): Driver? {
        return suspendCoroutine { continuation ->
            driverRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Log.d("co", "wsdf")
                        Log.d("snpshot", snapshot.toString())
                        val driver = snapshot.getValue(Driver::class.java)
                        continuation.resume(driver)
                    } else {
                        Log.d("ko", "wsdf")
                        continuation.resume(null)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            })
        }
    }

    suspend fun getStatus(id: String): Boolean {
        return suspendCoroutine { continuation ->
            driverRef.child(id).child("status").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val status = snapshot.getValue(String::class.java)
                        val statusB = if (status == "online") true else false
                        continuation.resume(statusB)
                    } else {
                        continuation.resume(null!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }

    suspend fun changeStatus(id: String, status: Boolean) {
        val statusFB = if (status) "online" else "offline"
        try {
            suspendCancellableCoroutine<Unit> { continuation ->
                driverRef.child(id).child("status").setValue(statusFB)
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception) // Ném lỗi khi có lỗi xảy ra
                    }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getAuto(id: String): Boolean {
        return suspendCoroutine { continuation ->
            driverRef.child(id).child("auto").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val auto = snapshot.getValue(Boolean::class.java)
                        continuation.resume(auto!!)
                    } else {
                        continuation.resume(null!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }

    suspend fun changeAuto(id: String, auto: Boolean) {
        try {
            suspendCancellableCoroutine<Unit> { continuation ->
                driverRef.child(id).child("auto").setValue(auto)
                    .addOnSuccessListener {
                        continuation.resume(Unit) // Tiếp tục khi cập nhật thành công
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception) // Ném lỗi nếu có lỗi xảy ra
                    }
            }
        } catch (e: Exception) {
            throw e
        }
    }
}

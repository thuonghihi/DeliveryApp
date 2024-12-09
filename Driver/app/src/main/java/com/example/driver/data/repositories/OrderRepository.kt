package com.example.driver.data.repositories

import android.util.Log
import com.example.driver.data.models.Driver
import com.example.driver.data.models.Order
import com.google.firebase.Firebase
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class OrderRepository {

    private val database = FirebaseDatabase.getInstance().getReference("orders")

//    fun getOrdersByDriverIdAndStatus(driverId: String, status: String, callback: (List<Order>) -> Unit) {
//        database.orderByChild("driver").equalTo(driverId)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    Log.d("order12345678", "gọi repository")
//                    val ordersList = snapshot.children.mapNotNull { child ->
//                        val order = child.getValue(Order::class.java)
//                        if (order?.status == status) order else null
//                    }
//                    Log.d("order12345678", "list in repository: " + ordersList.toString())
//                    Log.d("order12345678", "ádfgh" + ordersList.toString() + "")
//                    callback(ordersList)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.d("order12345678", "error")
//                }
//            })
//    }

    suspend fun getOrdersByDriverIdAndStatus(driverId: String, status: String): List<Order> {
        return suspendCoroutine { continuation ->
            database.orderByChild("driver").equalTo(driverId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val ordersList = snapshot.children.mapNotNull { child ->
                            val order = child.getValue(Order::class.java)
                            if (order?.status == status) order else null
                        }
                        Log.d("order12345678", "list in suspend function: " + ordersList.toString())
                        continuation.resume(ordersList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("order12345678", "error")
                        continuation.resumeWithException(error.toException())
                    }
                })
        }
    }



//    // Hàm cập nhật thông tin đơn hàng
//    suspend fun updateOrder(orderId: String, updatedOrder: Order): Boolean {
//        return try {
//            databaseReference.child(orderId).setValue(updatedOrder).await()
//            true
//        } catch (e: Exception) {
//            false
//        }
//    }


}

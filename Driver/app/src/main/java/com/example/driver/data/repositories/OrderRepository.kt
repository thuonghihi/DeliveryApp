package com.example.driver.data.repositories

import android.util.Log
import com.example.driver.data.models.Order
import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await

class OrderRepository {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("orders")

    // Hàm này sẽ gọi lại với dữ liệu mới khi Firebase có thay đổi
    fun getOrdersByDriverIdAndStatus(driverId: String, status: String, callback: (List<Order>) -> Unit) {
        databaseReference.orderByChild("driver").equalTo(driverId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val orders = snapshot.children.mapNotNull { it.getValue(Order::class.java) }
                        .filter { it.status == status }
                    callback(orders)  // Trả dữ liệu về callback
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error fetching data", error.toException())
                }
            })
    }

    // Hàm cập nhật thông tin đơn hàng
    suspend fun updateOrder(orderId: String, updatedOrder: Order): Boolean {
        return try {
            databaseReference.child(orderId).setValue(updatedOrder).await()
            true
        } catch (e: Exception) {
            false
        }
    }


}

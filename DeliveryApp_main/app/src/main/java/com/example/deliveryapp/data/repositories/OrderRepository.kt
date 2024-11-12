package com.example.deliveryapp.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.deliveryapp.data.models.Order
import com.example.deliveryapp.data.models.OrderTracking
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class OrderRepository{
    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("orders")

    suspend fun getOrders(customerId: String, statusList: List<String>): LiveData<List<Order>> {
        val ordersLiveData = MutableLiveData<List<Order>>()

        database.orderByChild("customerId").equalTo(customerId).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val orderList = mutableListOf<Order>()
                for (orderSnapshot in snapshot.children) {
                    val orderidshow = orderSnapshot.child("orderidshow").getValue(String::class.java)
                    val pickupLocation = orderSnapshot.child("pickupLocation").getValue(String::class.java)
                    val deliveryLocation = orderSnapshot.child("deliveryLocation").getValue(String::class.java)
                    val timestamp = orderSnapshot.child("timestamp").getValue(String::class.java)
                    val status = orderSnapshot.child("status").getValue(String::class.java)
                    val totalAmount = orderSnapshot.child("totalAmount").getValue(String::class.java)
                    val weight = orderSnapshot.child("weight").getValue(String::class.java)
                    val driver = orderSnapshot.child("driverId").getValue(String::class.java)
                    val customer = orderSnapshot.child("customerID").getValue(String::class.java)
                    val voucher = orderSnapshot.child("voucherId").getValue(String::class.java)
                    val paymentMethod = orderSnapshot.child("paymentMethod").getValue(String::class.java)
                    val order = Order(
                        orderidshow = orderidshow,
                        pickupLocation = pickupLocation,
                        deliveryLocation = deliveryLocation,
                        customer = customer,
                        driver = driver,
                        status = status,
                        timeStamp = timestamp,
                        voucher = voucher,
                        totalAmount = totalAmount,
                        paymentMethod = paymentMethod,
                        weight = weight
                    )
                    if (order != null && statusList.contains(order.status)) {
                        orderList.add(order)
                    }
                    else{
                        Log.d("Order bug", "Không có dữ liệu")
                    }
                }

                // Chỉ cập nhật LiveData khi có ít nhất một đơn hàng hợp lệ
                if (orderList.isNotEmpty()) {
                    ordersLiveData.postValue(orderList)
                } else {
                    Log.d("orderitem", "Không có dữ liệu đơn hàng hợp lệ.")
                }
            }



            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần
            }
        })

        return ordersLiveData
    }

    suspend fun addOrder(order: Order): Result<Boolean> {
        return try {
            val orderId = database.push().key ?: return Result.failure(Exception("Cannot generate order ID"))
            database.child(orderId).setValue(order).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
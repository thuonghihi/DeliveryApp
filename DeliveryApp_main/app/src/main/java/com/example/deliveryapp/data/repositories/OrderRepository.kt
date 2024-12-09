package com.example.deliveryapp.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.deliveryapp.data.models.Location
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
                    val orderIdShow = orderSnapshot.child("orderIdShow").getValue(String::class.java)
                    val pickupAddress = orderSnapshot.child("pickupAddress").getValue(String::class.java)
                    val deliveryAddress = orderSnapshot.child("deliveryAddress").getValue(String::class.java)

                    // Lấy và chuyển đổi deliveryLocation và pickupLocation thành đối tượng Location
                    val deliveryLocation = orderSnapshot.child("deliveryLocation").let {
                        val latitude = it.child("latitude").getValue(Double::class.java) ?: 0.0
                        val longitude = it.child("longitude").getValue(Double::class.java) ?: 0.0
                        Location(latitude, longitude)
                    }

                    val pickupLocation = orderSnapshot.child("pickupLocation").let {
                        val latitude = it.child("latitude").getValue(Double::class.java) ?: 0.0
                        val longitude = it.child("longitude").getValue(Double::class.java) ?: 0.0
                        Location(latitude, longitude)
                    }

                    val timestamp = orderSnapshot.child("timestamp").getValue(String::class.java)
                    val status = orderSnapshot.child("status").getValue(String::class.java)
                    val totalAmount = orderSnapshot.child("totalAmount").getValue(String::class.java)
                    val weight = orderSnapshot.child("weight").getValue(String::class.java)
                    val driver = orderSnapshot.child("driver").getValue(String::class.java)
                    val customer = orderSnapshot.child("customer").getValue(String::class.java)
                    val voucher = orderSnapshot.child("voucher").getValue(String::class.java)
                    val paymentMethod = orderSnapshot.child("paymentMethod").getValue(String::class.java)
                    val senderPhone = orderSnapshot.child("senderPhone").getValue(String::class.java)
                    val receiverPhone = orderSnapshot.child("receiverPhone").getValue(String::class.java)
                    val senderName = orderSnapshot.child("senderName").getValue(String::class.java)
                    val receiverName = orderSnapshot.child("receiverName").getValue(String::class.java)
                    val paymentRecipient = orderSnapshot.child("paymentRecipient").getValue(Boolean::class.java)
                    val note = orderSnapshot.child("note").getValue(String::class.java)

                    // Tạo đối tượng Order
                    val order = Order(
                        orderIdShow = orderIdShow,
                        pickupAddress = pickupAddress,
                        deliveryAddress = deliveryAddress,
                        deliveryLocation = deliveryLocation,
                        pickupLocation = pickupLocation,
                        status = status,
                        timeStamp = timestamp,
                        totalAmount = totalAmount,
                        voucher = voucher,
                        weight = weight,
                        driver = driver,
                        customer = customer,
                        paymentMethod = paymentMethod,
                        senderPhone = senderPhone,
                        receiverPhone = receiverPhone,
                        senderName = senderName,
                        receiverName = receiverName,
                        paymentRecipient = paymentRecipient ?: false,
                        note = note
                    )

                    // Kiểm tra trạng thái và thêm vào danh sách nếu hợp lệ
                    if (order != null && statusList.contains(order.status)) {
                        orderList.add(order)
                    } else {
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
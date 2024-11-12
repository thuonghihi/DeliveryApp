package com.example.deliveryapp.data.repositories

import android.util.Log
import com.example.deliveryapp.data.models.Customer
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.database
import kotlinx.coroutines.tasks.await

class CustomerRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = Firebase.database.reference

    suspend fun getCustomerById(id: String): Customer? {
        return try {
            val snapshot = database.child("customers").orderByKey().equalTo(id).get().await()
            Log.d("snapshot", snapshot.exists().toString())

            if (snapshot.exists()) {
                val user = snapshot.children.firstOrNull()
                Log.d("snapshot", user.toString())

                // Lấy dữ liệu từ user
                val addressesIndicator = object : GenericTypeIndicator<List<String>>() {}
                val addresses = user?.child("address")?.getValue(addressesIndicator)
                val email = user?.child("email")?.getValue(String::class.java)
                val name = user?.child("name")?.getValue(String::class.java)
                val savePassword = user?.child("password")?.getValue(String::class.java)
                val phoneNumber = user?.child("phone")?.getValue(String::class.java)

                // Tạo đối tượng Customer
                val customer = Customer(
                    addresses = addresses ?: listOf(), // Sử dụng danh sách trống nếu addresses là null
                    email = email ?: "",
                    name = name ?: "",
                    password = savePassword ?: "",
                    phone = phoneNumber ?: ""
                )

                Log.d("customer", customer.toString())
                return customer
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("Error", "Error fetching customer: ${e.message}")
            null
        }
    }

}
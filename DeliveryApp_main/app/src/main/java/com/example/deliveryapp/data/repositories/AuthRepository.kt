package com.example.deliveryapp.data.repositories

import android.util.Log
import androidx.compose.animation.core.snap
import com.example.deliveryapp.data.models.Customer
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: DatabaseReference = Firebase.database.reference

    suspend fun login(phone: String, password: String): Pair<Boolean, String?> {
        return try {
            val snapshot = database.child("customers").orderByChild("phone").equalTo(phone).get().await()
            if (snapshot.exists()) {
                val user = snapshot.children.firstOrNull()
                val customerId = user?.key
                val savePassword = user?.child("password")?.value.toString()

                if (savePassword == password) {
                    Pair(true, customerId)
                } else {
                    Pair(false, null)
                }
            } else {
                Pair(false, null)
            }
        } catch (e: Exception) {
            Log.d("cattt", e.message + "")
            Pair(false, null)
        }
    }

    fun register(email: String, password: String, callback: (Boolean) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}





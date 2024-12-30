package uk.ac.tees.mad.airtrack.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewmodel : ViewModel() {

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { task ->
                onSuccess()
            }.addOnFailureListener() { e ->
                onFailure(e)
            }
    }

    fun register(
        email: String,
        password: String,
        name: String,
        location: String,
        latitude: Double,
        longitude: Double,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val userMap = mapOf(
                    "name" to name,
                    "email" to email,
                    "location" to location,
                    "latitude" to latitude,
                    "longitude" to longitude
                )
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId ?: "")
                    .set(userMap)
                    .addOnSuccessListener { userTask ->
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure(e)
                    }
            }.addOnFailureListener { e ->
                onFailure(e)
            }
    }
}
package uk.ac.tees.mad.airtrack.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewmodel : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    private val _userExist = MutableStateFlow(false)
    val userExist = _userExist.asStateFlow()

    private val _currentUser = MutableStateFlow<UserModel?>(null)
    val currentUser = _currentUser.asStateFlow()

    init {
        checkIfUserExist()
    }

    private fun checkIfUserExist(){
        val currUser = auth.currentUser

        if (currUser != null){
            _userExist.value = true
            fetchCurrentUser()
        }else{
            _userExist.value = false
        }
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth
            .signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { task ->
                fetchCurrentUser()
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
        auth
            .createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                val userId = auth.currentUser?.uid
                val userMap = mapOf(
                    "name" to name,
                    "email" to email,
                    "location" to location,
                    "profileUrl" to "",
                    "latitude" to latitude,
                    "longitude" to longitude
                )
                firestore
                    .collection("users")
                    .document(userId ?: "")
                    .set(userMap)
                    .addOnSuccessListener { userTask ->
                        fetchCurrentUser()
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure(e)
                    }
            }.addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun fetchCurrentUser(){
        val userId = auth.currentUser?.uid
        firestore
            .collection("users")
            .document(userId?:"")
            .get()
            .addOnSuccessListener {
                val userInfo = UserModel(
                    name = it.getString("name"),
                    email = it.getString("email"),
                    location = it.getString("location"),
                    profileUrl = it.getString("profileUrl"),
                    latitude = it.getDouble("latitude"),
                    longitude = it.getDouble("longitude")
                )

                _currentUser.value = userInfo
                Log.i("Current User: ", "User is $userInfo")
            }
            .addOnFailureListener {
                Log.i("Current User: ", "User is not fetched")
            }
    }

    fun logOut(){
        auth.signOut()
        _userExist.value = false
        _currentUser.value = null
    }
}
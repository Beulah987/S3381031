package uk.ac.tees.mad.airtrack.ui.auth

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewmodel : ViewModel() {

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val storage =

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

    fun updateUserInformation(userInfo: UserInfo){
        viewModelScope.launch {
            val currUser = auth.currentUser
            if (currUser!=null){
                val userId = currUser.uid
                val updatedUser = UserInfo(
                    name = userInfo.name,
                    email = userInfo.email,
                    highestQualification = userInfo.highestQualification,
                    profilePictureUrl = userInfo.profilePictureUrl,
                )

                firestore.collection("users")
                    .document(userId)
                    .set(updatedUser)
                    .addOnSuccessListener {
                        fetchCurrentUser()
                        Log.i("The User update: ", "User updated successfully!")
                    }
                    .addOnFailureListener{
                        Log.i("The User update: ", "User is not updated successfully.")
                    }

            }
        }
    }

    fun updateProfileImage(uri: Uri){
        val currentUser = auth.currentUser
        if (currentUser!=null){
            val userId = currentUser.uid
            val imageRef = storage.reference.child("users/${userId}/profile.jpg")

            imageRef.putFile(uri)
                .addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener {
                        val imageLink = it.toString()

                        val userData = hashMapOf(
                            "profilePicture" to imageLink)
                        firestore.collection("users")
                            .document(userId)
                            .update(userData as Map<String, Any>)
                            .addOnSuccessListener {
                                fetchCurrentUser()
                            }
                        Log.i("The profile picture: ", "The picture is updated successfully.")
                    }
                }
                .addOnFailureListener{
                    Log.i("Error Encountered: ", "Unable to update the profile picture. ${it}")
                }
        }else{
            Log.i("Error update:", "Current User is null")
        }
    }

}
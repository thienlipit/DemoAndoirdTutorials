package com.example.familyconnect.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.familyconnect.models.User
import com.example.familyconnect.views.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso


class UserViewModel(mApplication: Application) :
    AndroidViewModel(mApplication) {
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef : DatabaseReference
    private var storageRef = Firebase.storage.reference

    companion object {
        lateinit var uriImage: Uri
    }

    suspend fun login(email: String, password: String) {
        mAuth = FirebaseAuth.getInstance()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
                    context.startActivity(intent)

                } else {
                    Toast.makeText(context, "User does no exist", Toast.LENGTH_SHORT).show()
                }
            }
    }

    suspend fun signup(name: String,email : String, password :String) {
        mAuth = FirebaseAuth.getInstance()

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(context, MainActivity::class.java )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("user").child(uid).setValue(User(name, email, uid, null))
    }

    fun changePassword(newPassword: String){
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        user!!.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "User password updated.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, MainActivity::class.java )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            }
    }

    fun setProfilePicName(): String{
        mAuth = FirebaseAuth.getInstance()

        val email = mAuth.currentUser?.email.toString()
        val list = email.split("@")
        return list[0].toString()
    }

    suspend fun uploadImageToFirebase(imageUri: Uri, imageView: ImageView, userImageName: String){
        val fileRef = storageRef.child("ImageProfile").child("$userImageName.jpg")
        fileRef.putFile(imageUri).addOnSuccessListener {
            Toast.makeText(context, "Image uploaded", Toast.LENGTH_LONG).show()
            fileRef.downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).into(imageView)

            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
        }
    }

    fun updateUserInfo(name: String, uriImage: Uri){
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser

        val profileUpdates = userProfileChangeRequest {
            displayName = name
            photoUri = Uri.parse(uriImage.toString())

            val currentUid = user?.uid.toString()
            updateUserToDB(name, currentUid, uriImage.toString())
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "User profile updated.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun updateUserToDB(name: String, uid: String, image: String) {
        mDbRef = FirebaseDatabase.getInstance().reference
        mDbRef.child("user").child(uid).child("name").setValue(name)
        mDbRef.child("user").child(uid).child("profilePic").setValue(image)
    }

    fun loadProfilePic(name: String, imageView: ImageView){
        val userImageRef = storageRef.child("ImageProfile").child("$name.jpg")
        userImageRef.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(imageView)
            uriImage = it
        }
    }
}
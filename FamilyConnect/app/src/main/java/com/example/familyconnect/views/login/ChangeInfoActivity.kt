package com.example.familyconnect.views.login

import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.familyconnect.databinding.ActivityChangeInfoBinding
import com.example.familyconnect.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class ChangeInfoActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()
    lateinit var binding: ActivityChangeInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Change Profile"

        val imageView = binding.imageView
        val btnChange = binding.button
        val edtNewUsername = binding.edtNewUsername
        val userImageName = userViewModel.setProfilePicName()
        userViewModel.loadProfilePic(userImageName, imageView)

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                lifecycleScope.launch {
                    userViewModel.uploadImageToFirebase(it, imageView, userImageName)
                }
        } )

        imageView.setOnClickListener {
            getImage.launch("image/*")
        }

        btnChange.setOnClickListener {
            if(edtNewUsername.text.toString().isEmpty()){
                edtNewUsername.error = "Please input your name."

            } else {
                val newName = edtNewUsername.text.toString()
                userViewModel.updateUserInfo(newName, UserViewModel.uriImage)
                finish()

            }
        }
    }
}
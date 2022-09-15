package com.example.familyconnect.views.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.familyconnect.databinding.ActivityChangePasswordBinding
import com.example.familyconnect.viewmodels.UserViewModel

class ChangePasswordActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()
    lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = "Change password"

        val btnUpdatePass = binding.btnUpdatePass
        val edtUpdatePass = binding.edtUpdatePass

        btnUpdatePass.setOnClickListener {
            val newPassword = edtUpdatePass.text.toString()
            when(newPassword.isNotEmpty()) {
                true -> userViewModel.changePassword(newPassword)
                false -> edtUpdatePass.error = "Please input new password"
            }
        }
    }
}
package com.example.familyconnect.views.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.familyconnect.databinding.ActivityLogInBinding
import com.example.familyconnect.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LogIn : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var binding: ActivityLogInBinding
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityLogInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mAuth = FirebaseAuth.getInstance()
        val btnLogIn = binding.btnLogIn
        val edtEmail = binding.edtEmail
        val edtPassword = binding.edtPassword

        binding.tvCreateAccount.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        btnLogIn.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if (email.isNotEmpty() and password.isNotEmpty()) {
                lifecycleScope.launch {
                    userViewModel.login(email, password)
                }

            } else {
                Toast.makeText(this@LogIn, "Email or password is empty", Toast.LENGTH_LONG).show()
            }
        }
    }
}
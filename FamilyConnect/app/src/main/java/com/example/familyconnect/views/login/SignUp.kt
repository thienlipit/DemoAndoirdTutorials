package com.example.familyconnect.views.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.familyconnect.databinding.ActivitySignUpBinding
import com.example.familyconnect.viewmodels.UserViewModel
import kotlinx.coroutines.launch

class SignUp : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Register form"

        binding.btnSignUp.setOnClickListener {
            val name = binding.edtUsername.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val confirmPassword = binding.edtConfirmPassword.text.toString()

            if (email.isNotEmpty() and password.isNotEmpty() and name.isNotEmpty() ) {
                val boolean = password == confirmPassword
                if (boolean) {
                    lifecycleScope.launch {
                        userViewModel.signup(name, email, password)
                    }

                }
                else {
                    Toast.makeText(this@SignUp, "Password not match", Toast.LENGTH_LONG).show()
                }
            }
            else {
                Toast.makeText(this@SignUp, "Name or email or password is empty", Toast.LENGTH_LONG).show()
            }

        }
    }
}
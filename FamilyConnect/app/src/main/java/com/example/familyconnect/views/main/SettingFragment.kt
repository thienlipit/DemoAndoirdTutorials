package com.example.familyconnect.views.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.familyconnect.databinding.FragmentSettingBinding
import com.example.familyconnect.views.login.ChangeInfoActivity
import com.example.familyconnect.views.login.ChangePasswordActivity
import com.example.familyconnect.views.login.LogIn
import com.google.firebase.auth.FirebaseAuth

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSignOut = binding.btnSignOut
        val btnChangeProfile = binding.btnChangeInfo
        val btnChangePass = binding.btnChangePass

        btnChangePass.setOnClickListener {
            val intent = Intent(context, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        btnChangeProfile.setOnClickListener {
            val intent = Intent(context, ChangeInfoActivity::class.java)
            startActivity(intent)
        }

        btnSignOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, LogIn::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
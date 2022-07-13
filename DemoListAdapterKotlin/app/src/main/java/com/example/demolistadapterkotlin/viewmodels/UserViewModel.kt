package com.example.demolistadapterkotlin.viewmodels

import androidx.lifecycle.ViewModel
import com.example.demolistadapterkotlin.models.UsersListening

class UserViewModel: ViewModel() {
    var list = UsersListening()
}
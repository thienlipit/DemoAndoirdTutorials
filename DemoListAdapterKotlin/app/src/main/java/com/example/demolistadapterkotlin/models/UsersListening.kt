package com.example.demolistadapterkotlin.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.demolistadapterkotlin.BR

class UsersListening: BaseObservable() {
    @get:Bindable
    var listUsers: MutableList<User> = mutableListOf()
    set(value) {
        field = value
        notifyPropertyChanged(BR.listUsers)
    }
}
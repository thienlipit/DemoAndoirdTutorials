package com.example.demolistadapterkotlin.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.example.demolistadapterkotlin.BR

class User : BaseObservable() {
    @get:Bindable
    var name: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @get:Bindable
    var age: String = ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.age)
        }
}
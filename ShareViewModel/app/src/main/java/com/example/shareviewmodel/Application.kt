package com.example.shareviewmodel

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

//class Application: android.app.Application() {
//    override fun onCreate() {
//        super.onCreate()
//        startKoin {
//            androidContext(this@Application)
//            modules(listOf(sharedViewModel))
//        }
//    }
//}
//val sharedViewModel = module {
//    viewModel {
//        SharedViewModel()
//    }
//}
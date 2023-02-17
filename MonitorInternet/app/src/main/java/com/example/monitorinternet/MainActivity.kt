package com.example.monitorinternet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private lateinit var connectionLiveData: ConnectionLiveData
//    private lateinit var networkStatusHelper: NetworkStatusHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        networkStatusHelper = NetworkStatusHelper(this)
//        networkStatusHelper.observe(this){
//            Log.d("Status", it.toString())
//        }







        connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) { isNetworkAvailable ->
            isNetworkAvailable?.let {
                updateUI(it, window.decorView.rootView)
//                updateUI1(it, window.decorView.rootView)
            }
        }

    }

    private fun updateUI1(it: String, view: View) {
        val text = view.findViewById<TextView>(R.id.tv_internet)
        text.text = it

    }

    private fun updateUI(it: Boolean, view: View) {
        val text = view.findViewById<TextView>(R.id.tv_internet)
        text.text = it.toString()

    }
}
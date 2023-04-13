package com.example.bleexample

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class RequestPermission( appCompatActivity: AppCompatActivity) {
    private var requestBluetoothOn =
        appCompatActivity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //granted
                Log.d("TAG", "Bluetooth is ON")
            } else {
                //deny
                Log.d("TAG", "Bluetooth is OFF")
            }
        }
    @RequiresApi(Build.VERSION_CODES.N)
    val requestMultiplePermissionsAPI30Below =
        appCompatActivity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("List Permissions: ", "${it.key} = ${it.value}")
            }
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, true) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, true) -> {
                    // Only approximate location access granted.
                }
                permissions.getOrDefault(Manifest.permission.BLUETOOTH, true) -> {
                    // Only approximate location access granted.
                }
                permissions.getOrDefault(Manifest.permission.BLUETOOTH_ADMIN, true) -> {
                    // Only approximate location access granted.
                }
                else -> {
                    // No location access granted.
                }
            }
        }
    private val requestMultiplePermissionsAPI31Above =
        appCompatActivity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("List Permissions: ", "${it.key} = ${it.value}")
            }
        }

    @RequiresApi(Build.VERSION_CODES.N)
    fun requestAllPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d("SDK", ">31")
            requestMultiplePermissionsAPI31Above.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        } else {
            Log.d("RUN ELSE", "<=30")
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetoothOn.launch(enableBtIntent)
            requestMultiplePermissionsAPI30Below.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    }
}
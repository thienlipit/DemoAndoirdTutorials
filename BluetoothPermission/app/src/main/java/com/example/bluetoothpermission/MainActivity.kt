package com.example.bluetoothpermission

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.bluetoothpermission.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var btManager: BluetoothManager? = null
    private var btAdapter: BluetoothAdapter? = null
    private var btScanner: BluetoothLeScanner? = null

    private var requestBluetoothOn =
        registerForActivityResult(
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
        registerForActivityResult(
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
            else -> {
                // No location access granted.
            }
        }
    }
    private val requestMultiplePermissionsAPI31Above =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                Log.d("List Permissions: ", "${it.key} = ${it.value}")
            }
        }

    @RequiresApi(Build.VERSION_CODES.N)
    fun requestAllPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d("SDK", "111")
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
            Log.d("RUN ELSE", "222")
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
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = btManager!!.adapter
        btScanner = btAdapter?.bluetoothLeScanner


        // turn on bluetooth
        binding.btnOn.setOnClickListener {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetoothOn.launch(intent)
        }

        // turn off bluetooth
        binding.btnOff.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED && btAdapter!!.isEnabled
            ) {
                btAdapter!!.disable()
                Toast.makeText(this, "BT is off", Toast.LENGTH_LONG).show()
            } else Toast.makeText(this, "BT is off and can't not turn off", Toast.LENGTH_LONG).show()

        }

        requestAllPermission()
    }
}
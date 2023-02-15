package com.example.bleexample

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.util.keyIterator
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow

const val MAC_BEACON_abc: String = "D5:43:8F:F6:0D:46"
const val MAC_BEACON_01: String = "D5:D2:C0:EC:BA:76"
const val MAC_BEACON_02: String = "DE:79:8D:06:0C:AC"
open class MainActivity : AppCompatActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
         val mBluetoothAdapter =
            (applicationContext!!.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RequestPermission(this).requestPermission()

        val btnBtOn = findViewById<Button>(R.id.BtBtn)
        val btnBtOff = findViewById<Button>(R.id.BtBtn1)
        val btnSensor = findViewById<Button>(R.id.sensor)
        val btnLocation = findViewById<Button>(R.id.btnLocation)
        val tvBt = findViewById<TextView>(R.id.BtTv)

        btnLocation.setOnClickListener {
            val intent = Intent(this, MyNavigationService::class.java)
            startActivity(intent)
        }

        btnSensor.setOnClickListener {
            val intent = Intent(this, SensorActivity::class.java)
            startActivity(intent)
        }

        if(mBluetoothAdapter.isEnabled){
            tvBt.text = getString(R.string.bluetooth_on)
        } else {
            tvBt.text = getString(R.string.bluetooth_off)
        }

        btnBtOn.setOnClickListener {
            if (mBluetoothAdapter.isEnabled) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@setOnClickListener
                }
                mBluetoothAdapter.disable()
                tvBt.text = getString(R.string.bluetooth_off)
            } else {
                mBluetoothAdapter.enable()
                tvBt.text = getString(R.string.bluetooth_on)
            }
        }

        btnBtOff.setOnClickListener {
            mBluetoothAdapter.disable()
            tvBt.text = getString(R.string.bluetooth_off)
        }
        scanLeDevice()

        TestBLEManager().demoBLE(this, this)
    }

    @SuppressLint("MissingPermission")
    private fun scanLeDevice() {
        val bluetoothAdapter =
            (applicationContext.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter

        val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        var scanning = false
        val handler = Handler()

        // Stops scanning after 10 seconds.
        val scanPeriod = 10000L

        Log.d("run ", "run here")
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_SCAN
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@postDelayed
                }
                bluetoothLeScanner?.stopScan(leScanCallback)
            }, scanPeriod)
            scanning = true
            bluetoothLeScanner?.startScan(leScanCallback)
        } else {
            scanning = false
            bluetoothLeScanner?.stopScan(leScanCallback)
        }
    }

    var arrayDistance: MutableList<Double> = mutableListOf()
    var mutableListRSSI: MutableList<Int> = mutableListOf()
    // Device scan callback.
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission", "SimpleDateFormat")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            if(result.device.toString() == MAC_BEACON_01){
//                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                val sdf = SimpleDateFormat("hh:mm:ss")
                val currentDate = sdf.format(Date())

//                Log.d("all info", result.toString())
//                Log.d("device", result.device.toString())
                Log.d("rssi", result.rssi.toString())
//                Log.d("advertisingSid", result.advertisingSid.toString())
//                Log.d("txPower", result.txPower.toString())
//                Log.d("txPowerLevel", result.scanRecord!!.txPowerLevel.toString())

                val base = 10
                val n = 3   // environment index

                val measurePower = -46.0
                val txPower = 6
                val advInterval = 300  //ADV_interval 300ms
                val realDistance = 1.0    // 1 meter


                val exponent = ((measurePower - (result.rssi).toDouble())/(10*n))
                val resultDistance = base.toDouble().pow(exponent)
                Log.d("Distance", resultDistance.toString())
                arrayDistance.add(resultDistance)
                mutableListRSSI.add(result.rssi)


                var avgDistance = 0.0
                var avgRSSI = 0.0
                if(arrayDistance.size == 3){

                    for (i in arrayDistance){
                        avgDistance += i
                    }
                    for (i in mutableListRSSI){
                        avgRSSI +=  i.toDouble()
                    }
                    avgRSSI /= 3.0
                    avgDistance /= 3.0
                    Log.d("AVGDistance: ", avgDistance.toString())
                    arrayDistance.clear()
                    mutableListRSSI.clear()

                    val writeText = currentDate + "\t" + txPower + "\t" + advInterval + "\t" + measurePower + "\t" + n + "\t" + String.format("%.3f", avgDistance) + "\t" + realDistance + "\t" + String.format("%.3f", avgRSSI) + "\n"
                    StoreData().writeDataToFile("SS_Data.txt", writeText, applicationContext)

                }

                var manufacturerId = 0
                result.scanRecord!!.manufacturerSpecificData.keyIterator().forEach { key ->
                    manufacturerId = key
                }
                showBeaconInfo(result, manufacturerId)

            }
        }
    }

    fun showBeaconInfo(result: ScanResult, manufacturerId: Int) {
        val uuid = toHexString(result.scanRecord!!.getManufacturerSpecificData(manufacturerId)!!.copyOfRange(2, 18))
        Log.d("UUID", uuid)

        val major = toHexString(result.scanRecord!!.getManufacturerSpecificData(manufacturerId)!!.copyOfRange(18, 20))
        Log.d("MAJOR", major.toInt(16).toString())

        val minor = toHexString(result.scanRecord!!.getManufacturerSpecificData(manufacturerId)!!.copyOfRange(20, 22))
        Log.d("MINOR", minor.toInt(16).toString())

        val txPower = toHexString(result.scanRecord!!.getManufacturerSpecificData(manufacturerId)!!.copyOfRange(22, 23))
        Log.d("txPower", txPower.toInt(16).toString())
    }

    private fun toHexString(bytes: ByteArray): String {
        if (bytes.isEmpty()) {
            return ""
        }
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = (bytes[j].toInt() and 0xFF)
            hexChars[j * 2] = HEX[v ushr 4]
            hexChars[j * 2 + 1] = HEX[v and 0x0F]
        }
        return String(hexChars)
    }

    companion object {
        const val TAG = "Main"
        val HEX = "0123456789ABCDEF".toCharArray()
    }
}

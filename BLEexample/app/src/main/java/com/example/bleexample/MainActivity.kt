package com.example.bleexample

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.util.keyIterator
import androidx.lifecycle.Observer
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.MonitorNotifier
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow


const val MAC_BEACON_abc: String = "D5:43:8F:F6:0D:46"
const val MAC_BEACON_01: String = "D5:D2:C0:EC:BA:76"
const val MAC_BEACON_02: String = "DE:79:8D:06:0C:AC"
class MainActivity : AppCompatActivity() {
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null

    @RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                Log.d("location 1", "Precise location access granted")
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
                Log.d("location 2", "Only approximate location access granted")
            } else -> {
            // No location access granted.
            Log.d("location 3", "No location access granted")
        }
        }
    }
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()



    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
        ))

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

        // Declaring Bluetooth adapter
        // On button Click
        if(bluetoothAdapter.isEnabled){
            tvBt.text = "Bluetooth is on"
        } else {
            tvBt.text = "Bluetooth is off"
        }

        btnBtOn.setOnClickListener {

            // Enable or disable the Bluetooth and display
            // the state in Text View
            if (bluetoothAdapter.isEnabled) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@setOnClickListener
                }
                bluetoothAdapter.disable()
                tvBt.text = "Bluetooth is OFF"
            } else {
                bluetoothAdapter.enable()
                tvBt.text = "Bluetooth is ON"
            }
        }

        btnBtOff.setOnClickListener {
            bluetoothAdapter.disable()
            tvBt.text = "Bluetooth is OFF now"
        }
        scanLeDevice()

//        val beaconManager =  BeaconManager.getInstanceForApplication(this)
//        beaconManager.beaconParsers.clear()

        // The example shows how to find iBeacon.
//        beaconManager.beaconParsers.add(
//            BeaconParser().
//            setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
//        val region = Region("all-beacons-region", null, null, null)
//        // Set up a Live Data observer so this Activity can get ranging callbacks
//        // observer will be called each time the monitored regionState changes (inside vs. outside region)
//        beaconManager.getRegionViewModel(region).rangedBeacons.observe(this, rangingObserver)
//        beaconManager.startRangingBeacons(region)

    }

    val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        Log.d(TAG, "Ranged: ${beacons.count()} beacons")
        for (beacon: Beacon in beacons) {
            Log.d(TAG, "RSSI: ${beacon.rssi}")
            Log.d(TAG, "$beacon about ${beacon.distance} meters away")
        }
    }


    private val monitoringObserver = Observer<Int> { state ->
        if (state == MonitorNotifier.INSIDE) {
            Log.d("TAG", "Detected beacons(s)")
        }
        else {
            Log.d("TAG", "Stopped detecteing beacons")
        }
    }

    private val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
    private var scanning = false
    private val handler = Handler()

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

    @SuppressLint("MissingPermission")
    private fun scanLeDevice() {
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
            }, SCAN_PERIOD)
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
                val realDistance = 2.6    // 1 meter


                val exponent = ((measurePower - (result.rssi).toDouble())/(10*n))
                val resultDistance = base.toDouble().pow(exponent)
                Log.d("Distance", resultDistance.toString())
                arrayDistance.add(resultDistance)
                mutableListRSSI.add(result.rssi)


                var avgDistance:Double = 0.0
                var avgRSSI: Double = 0.0
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

                result.scanRecord!!.manufacturerSpecificData.keyIterator().forEach { key ->
//                    Log.d("getManufacturer",  result.scanRecord!!.getManufacturerSpecificData(key)!!.copyOfRange(22, 23).toString())
//                    Log.d("txpower", toHexString(result.scanRecord!!.getManufacturerSpecificData(key)!!.copyOfRange(22, 23)).toInt(16).toString())
                }
                val uuid = toHexString(result.scanRecord!!.getManufacturerSpecificData(65535)!!.copyOfRange(2, 18))
//                Log.d("UUID", uuid)

                val major = toHexString(result.scanRecord!!.getManufacturerSpecificData(65535)!!.copyOfRange(18, 20))

//                Log.d("MAJOR", major.toInt(16).toString())

                val minor = toHexString(result.scanRecord!!.getManufacturerSpecificData(65535)!!.copyOfRange(20, 22))
//                Log.d("MINOR", minor.toInt(16).toString())

                val txpower = toHexString(result.scanRecord!!.getManufacturerSpecificData(65535)!!.copyOfRange(22, 23))
//                Log.d("txpower", txpower.toInt(16).toString())

            }

        }
    }

    fun toHexString(bytes: ByteArray): String {
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

    private val HEX = "0123456789ABCDEF".toCharArray()

    companion object {
        val TAG = "Main"
    }
}

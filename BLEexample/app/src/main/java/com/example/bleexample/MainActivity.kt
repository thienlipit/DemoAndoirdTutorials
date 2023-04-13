package com.example.bleexample

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.ParcelUuid
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.util.keyIterator
import com.example.bleexample.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

const val MAC_BEACON_abc: String = "D5:43:8F:F6:0D:46"
const val MAC_BEACON_01: String = "D5:D2:C0:EC:BA:76"
const val MAC_BEACON_02: String = "DE:79:8D:06:0C:AC"
const val MAC_BEACON_PIXEL: String = "60:CC:D6:78:D2:47"
const val FEASY_BEACON: String = "DC:0D:30:14:32:6B"
open class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
         val mBluetoothAdapter =
            (applicationContext!!.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        RequestPermission(this).requestAllPermission()
        btnLocationClick(binding.btnLocation)
        btnSensorClick(binding.sensor)
        binding.BtTv.text = if(mBluetoothAdapter.isEnabled) getString(R.string.bluetooth_on) else getString(R.string.bluetooth_off)
        btnBTOnClick(binding.btnBTOn, mBluetoothAdapter, binding.BtTv)
        btnBTOffClick(binding.btnBTOff, mBluetoothAdapter, binding.BtTv)

        val bluetoothAdapter =
            (applicationContext.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter
        btnScan(binding.btnScan, bluetoothAdapter)

        btnStop(binding.btnStop, bluetoothAdapter)


//        scanLeDevice()
//        TestBLEManager().demoBLE(this, this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    private fun scanLeDevice() {
        val bluetoothAdapter =
            (applicationContext.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter

        val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

        var scanning = false
        val handler = Handler()

        // Stops scanning after 10 seconds.
        val scanPeriod = 30000L

        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_SCAN
                    ) != PackageManager.PERMISSION_GRANTED
                ) return@postDelayed

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

            if(result.device.toString() == FEASY_BEACON){
                Log.d("ScanResult", result.scanRecord.toString())

                val SERVICE_UUID = "0000FEAA-0000-1000-8000-00805F9B34FB"
                val scanRecord = result.scanRecord
//                val serviceData = scanRecord.getServiceData( UUID.fromString(SERVICE_UUID))


/*                val base = 10
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
                }*/


                var manufacturerId = 0
                result.scanRecord!!.manufacturerSpecificData.keyIterator().forEach { key ->
                    manufacturerId = key
                }
                showBeaconInfo(result,manufacturerId )

            }
        }
    }

    fun showBeaconInfo(result: ScanResult, manufacturerId: Int) {

//        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val sdf = SimpleDateFormat("hh:mm:ss")
        val currentDate = sdf.format(Date())
        val manufacturerSpecificData = result.scanRecord!!.manufacturerSpecificData
        if(manufacturerSpecificData != null) {
            val uuid = toHexString(result.scanRecord!!.getManufacturerSpecificData(manufacturerId)!!.copyOfRange(2, 18))
            Log.d("UUID", uuid)

            val major = toHexString(result.scanRecord!!.getManufacturerSpecificData(manufacturerId)!!.copyOfRange(18, 20))
            Log.d("MAJOR", major.toInt(16).toString())

            val minor = toHexString(result.scanRecord!!.getManufacturerSpecificData(manufacturerId)!!.copyOfRange(20, 22))
            Log.d("MINOR", minor.toInt(16).toString())

            val txPower = toHexString(result.scanRecord!!.getManufacturerSpecificData(manufacturerId)!!.copyOfRange(22, 23))
            Log.d("txPower", txPower.toInt(16).toString())
        } else return

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

    private fun btnLocationClick(btnLocation: Button){
        btnLocation.setOnClickListener {
            val intent = Intent(this, MyNavigationService::class.java)
            startActivity(intent)
        }
    }
    private fun btnSensorClick(btnSensor: Button){
        btnSensor.setOnClickListener {
            val intent = Intent(this, SensorActivity::class.java)
            startActivity(intent)
        }
    }
    private fun btnBTOnClick(btnBtOn: Button, mBluetoothAdapter: BluetoothAdapter, tvBt: TextView){
        btnBtOn.setOnClickListener {
            if (mBluetoothAdapter.isEnabled) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) return@setOnClickListener
                return@setOnClickListener
            }
            else {
                mBluetoothAdapter.enable()
                tvBt.text = getString(R.string.bluetooth_on)
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun btnBTOffClick(btnBtOff: Button, mBluetoothAdapter: BluetoothAdapter, tvBt: TextView){
        btnBtOff.setOnClickListener {
            mBluetoothAdapter.disable()
            tvBt.text = getString(R.string.bluetooth_off)
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun btnScan(btnScan: Button, bluetoothAdapter: BluetoothAdapter){
        Log.d(javaClass.name, "Start scan")
        btnScan.setOnClickListener {
            val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.BLUETOOTH_SCAN
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                return@setOnClickListener}
            if(bluetoothAdapter.isLeCodedPhySupported){
                bluetoothLeScanner.startScan(leScanCallback)
            } else {
                Log.d("isLeCodedPhySupported", "false")
                bluetoothLeScanner.startScan(leScanCallback)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun btnStop(btnStop: Button, bluetoothAdapter: BluetoothAdapter){
        Log.d(javaClass.name, "Stop scan")
        btnStop.setOnClickListener {
            val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_SCAN
                ) != PackageManager.PERMISSION_GRANTED
            ) return@setOnClickListener
            if(bluetoothAdapter.isLeCodedPhySupported){
                bluetoothLeScanner.startScan(leScanCallback)
            } else {
                Log.d("isLeCodedPhySupported", "false")
                bluetoothLeScanner.startScan(leScanCallback)
            }
        }
    }

    fun getAoaAoD(){
        val service_uuid1 = "0000fef5-0000-1000-8000-00805f9b34fb"
        val SERVICE_UUID = "0000FEAA-0000-1000-8000-00805F9B34FB"
        val AOADATA_UUID = byteArrayOf(
            0xBF.toByte(),
            0x24.toByte(),
            0x1F.toByte(),
            0x16.toByte(),
            0x80.toByte(),
            0x02.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0x00.toByte(),
            0x09.toByte(),
            0x00.toByte(),
            0x03.toByte(),
            0x19.toByte(),
            0xC1.toByte()
        )

        val scanner = BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner
        val scanCallback: ScanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                val scanRecord = result.scanRecord
//                if (scanRecord != null && scanRecord.serviceUuids != null && scanRecord.serviceUuids.contains(
                if (scanRecord != null && scanRecord.serviceUuids != null && scanRecord.serviceUuids.contains(
                        ParcelUuid.fromString(SERVICE_UUID)
                    )
                ) {
                    val serviceData = scanRecord.getServiceData(ParcelUuid.fromString(SERVICE_UUID))
                    if (serviceData != null && Arrays.equals(
                            Arrays.copyOfRange(serviceData, 0, 15),
                            AOADATA_UUID
                        )
                    ) {
                        val aoa = Arrays.copyOfRange(serviceData, 15, 17)
                        val aod = Arrays.copyOfRange(serviceData, 17, 19)
                        // Do something with the AoA and AoD information
                        // ...
                    }
                }
            }
        }

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()
//        scanner.startScan(null, settings, scanCallback)
    }
}
package com.example.bleexample

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import org.altbeacon.beacon.*

class TestBLEManager {
    private val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        Log.d("LIBRARY", "Ranged: ${beacons.count()} beacons")
        for (beacon: Beacon in beacons) {
            Log.d("LIBRARY", "RSSI: ${beacon.rssi}")
            Log.d("LIBRARY", "$beacon about ${beacon.distance} meters away")
        }
    }

    private val monitoringObserver = Observer<Int> { state ->
        if (state == MonitorNotifier.INSIDE) {
            Log.d("TAG", "Detected beacons(s)")
        }
        else {
            Log.d("TAG", "Stopped detecting beacons")
        }
    }
    fun demoBLE(context: Context, lifecycleOwner: LifecycleOwner){
        val beaconManager =  BeaconManager.getInstanceForApplication(context)
        beaconManager.beaconParsers.clear()
//         The example shows how to find iBeacon.
        beaconManager.beaconParsers.add(
            BeaconParser().
            setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        val region = Region("all-beacons-region", null, null, null)
        // Set up a Live Data observer so this Activity can get ranging callbacks
        // observer will be called each time the monitored regionState changes (inside vs. outside region)
        beaconManager.getRegionViewModel(region).rangedBeacons.observe(lifecycleOwner, rangingObserver)
        beaconManager.startRangingBeacons(region)
    }
}
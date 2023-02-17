package com.example.monitorinternet

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

const val TAG = "MyTagConnectionManager"
/*
class ConnectionLiveData(context: Context) : LiveData<String>() {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val connectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks: MutableSet<String> = HashSet()

    private fun checkValidNetworks() {
//        postValue(validNetworks.size > 0)
        Log.d("Value", validNetworks.toString())
        for (item in validNetworks){
            Log.d("hehe", item.toString())
        }

//        if (validNetworks.toString() == "1752"){
//            Log.d("OK", "ok")
//        } else Log.d("No", "not ok")
        val result = when(validNetworks.size) {
            0 -> "No internet access"
            1 -> "Connected but no internet access"
            else -> "Internet access"
        }
        postValue(result)

//        if(validNetworks.size > 0){
//            postValue("ok men ok men")
//        } else  postValue("No internet access")

    }

    override fun onActive() {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {

            Log.d(TAG, "onAvailable: $network")
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            validNetworks.add(network.toString()+"first")

            val hasInternetCapability = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)
            Log.d(TAG, "onAvailable: ${network}, $hasInternetCapability")

            if (hasInternetCapability == true) {
                // Check if this network actually has internet
                CoroutineScope(Dispatchers.IO).launch {
                    val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)
                    if (hasInternet) {
                        withContext(Dispatchers.Main) {
                            if(hasInternet){
                            Log.d(TAG, "onAvailable: adding network. $network")
                            validNetworks.add(network.toString())
                            checkValidNetworks()
                        }
                    }
                }
            }
        }

        override fun onLost(network: Network) {
            Log.d(TAG, "onLost: $network")
//            validNetworks.remove(network)
            validNetworks.clear()
            checkValidNetworks()
        }
    }

    object DoesNetworkHaveInternet {

        fun execute(socketFactory: SocketFactory): Boolean {
            // Make sure to execute this on a background thread.
            return try {
                Log.d(TAG, "PINGING Google...")
                val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
                socket.connect(InetSocketAddress("www.google.com", 80), 2000)
                socket.close()
                Log.d(TAG, "PING success.")
                true
            } catch (e: IOException) {
                Log.e(TAG, "No Internet Connection. $e")
                false
            }
        }
    }
}
*/


class ConnectionLiveData(context: Context) : LiveData<Boolean>() {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private val connectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks: MutableSet<Network> = HashSet()

    private fun checkValidNetworks() {
        postValue(validNetworks.size > 0)
    }

    override fun onActive() {
        networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onInactive() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            Log.d(TAG, "onAvailable: $network")
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val hasInternetCapability = networkCapabilities?.hasCapability(NET_CAPABILITY_INTERNET)
            Log.d(TAG, "onAvailable: ${network}, $hasInternetCapability")

            if (hasInternetCapability == true) {
                // Check if this network actually has internet
                CoroutineScope(Dispatchers.IO).launch {
                    val hasInternet = DoesNetworkHaveInternet.execute(network.socketFactory)
                    if (hasInternet) {
                        withContext(Dispatchers.Main) {
                            Log.d(TAG, "onAvailable: adding network. $network")
                            validNetworks.add(network)
                            checkValidNetworks()
                        }
                    }
                }
            }
        }

        override fun onLost(network: Network) {
            Log.d(TAG, "onLost: $network")
            validNetworks.remove(network)
            checkValidNetworks()
        }
    }

    object DoesNetworkHaveInternet {

        fun execute(socketFactory: SocketFactory): Boolean {
            // Make sure to execute this on a background thread.
            return try {
                Log.d(TAG, "PINGING Google...")
                val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
                socket.connect(InetSocketAddress("www.google.com", 80), 2000)
                socket.close()
                Log.d(TAG, "PING success.")
                true
            } catch (e: IOException) {
                Log.e(TAG, "No Internet Connection. $e")
                false
            }
        }
    }
}
package com.example.bleexample

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileInputStream

class StoreData() {

    fun writeDataToFile(fileName: String, content: String, applicationContext: Context){
        val file = File(applicationContext.filesDir, fileName)
        file.createNewFile()
        file.appendText(content)
    }

    fun readDataFromFile(fileName: String, applicationContext: Context){
        val file = File(applicationContext.filesDir, fileName)
        val readResult = FileInputStream(file).bufferedReader().use { it.readText() }
        Log.d("ReadResult",readResult)
    }


}


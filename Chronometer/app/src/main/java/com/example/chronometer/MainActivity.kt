package com.example.chronometer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {
    private var running: Boolean = false
    private var pauseOffset: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val chronometer = findViewById<Chronometer>(R.id.chronometer)

        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnPause = findViewById<Button>(R.id.btnPause)
        val btnReset = findViewById<Button>(R.id.btnReset)


        btnStart.setOnClickListener {
            if(!running) {
                chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
                chronometer!!.start()
                running = true
            }
        }

        btnPause.setOnClickListener {
            if(running) {
                chronometer!!.stop()
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
                running = false
            }
        }

        btnReset.setOnClickListener {
            if(!running) {
                chronometer.base = SystemClock.elapsedRealtime()
                pauseOffset = 0
            }
        }
    }
}
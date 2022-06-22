package com.example.addviewlinear

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.IntArray
import kotlin.arrayOf


class MainActivity : AppCompatActivity() {
    var name = arrayOf(
        "TRUONG", "KY", "KHANG", "CHIEN", "NHAT", "DINH", "THANG", "LOI")
    var position = arrayOf(
        "CAN VE", "THU KY", "TINH BAO",
        "BEP", "GIAO LIEN", "HAU CAN", "THONG TIN", "LIEN LAC",
    )
    var salary = arrayOf(13000, 10000, 13000, 13000, 10000, 15000, 13000, 8000,)

    var colors = IntArray(2)

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val localLayoutInflater = baseContext.getSystemService("layout_inflater") as LayoutInflater
//        val myView: View = localLayoutInflater.inflate(R.layout.item, null)
//        val myView1: View = localLayoutInflater.inflate(R.layout.item, null)
//
//
//        val ll = findViewById<View>(R.id.ll_main) as LinearLayout
//
//        ll.addView(myView)
//        ll.addView(myView1)

        colors[0] = Color.parseColor("#559966CC")
        colors[1] = Color.YELLOW

        val linLayout = findViewById<View>(R.id.ll_main) as LinearLayout

        val ltInflater = layoutInflater

        for (i in name.indices) {
            Log.d("myLogs", "i = $i")
            val item: View = ltInflater.inflate(R.layout.item, linLayout, false)

            val tvName = item.findViewById<View>(R.id.tvName) as TextView
            tvName.text = name[i]
            val tvPosition = item.findViewById<View>(R.id.tvPosition) as TextView
            tvPosition.text = "Position: " + position[i]
            val tvSalary = item.findViewById<View>(R.id.tvSalary) as TextView
            tvSalary.text = "Salary: " + salary[i]
            item.layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            item.setBackgroundColor(colors[i % 2])
            linLayout.addView(item)
        }
    }
}
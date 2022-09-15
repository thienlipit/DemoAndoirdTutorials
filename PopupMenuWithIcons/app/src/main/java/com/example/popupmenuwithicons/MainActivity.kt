package com.example.popupmenuwithicons

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.TextView
import java.lang.reflect.Method

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)

        // show sdk version in text view
        textView.append("\nAPI " + Build.VERSION.SDK_INT)

        // text view click listener
        textView.setOnClickListener {
            showPopupMenu(textView)
        }
    }

    // method to show popup menu
    private fun showPopupMenu(textView: TextView) {
        val popup = PopupMenu(this, textView)

        popup.apply {
            // inflate the popup menu
            menuInflater.inflate(R.menu.popup_menu, menu)

            // popup menu item click listener
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.red -> {
                        textView.setTextColor(Color.RED)
                    }
                    R.id.green -> {
                        textView.setTextColor(Color.GREEN)
                    }
                    R.id.yellow -> {
                        textView.setTextColor(Color.YELLOW)
                    }
                    R.id.gray -> {
                        textView.setTextColor(Color.GRAY)
                    }
                }
                false
            }
        }

        // show icons on popup menu
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popup.setForceShowIcon(true)
        } else {
            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field[popup]
                        val classPopupHelper =
                            Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons: Method = classPopupHelper.getMethod(
                            "setForceShowIcon",
                            Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // finally, show the popup menu
        popup.show()
    }
}
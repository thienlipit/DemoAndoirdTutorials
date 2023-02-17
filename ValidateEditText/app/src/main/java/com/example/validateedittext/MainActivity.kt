package com.example.validateedittext

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registrationButton = findViewById<Button>(R.id.button)
        val nameEditText = findViewById<EditText>(R.id.editText1)
        val wordEditText = findViewById<EditText>(R.id.editText2)

        registrationButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val word = wordEditText.text.toString()
            if(name.isEmpty()){
                nameEditText.requestFocus()
                nameEditText.error = "Field cannot be empty"
            }
            else if(!name.matches(Regex("[a-zA-Z ]+")))  {
                nameEditText.requestFocus()
                nameEditText.error = "ENTER ONLY ALPHABETICAL CHARACTER"

            }
            else if(word.isEmpty()){
                wordEditText.requestFocus()
                wordEditText.error = "FIELD CANNOT BE EMPTY"
            }
            else {
                Toast.makeText(this, "Validation Successful",Toast.LENGTH_LONG).show()
            }
        }

        val til = findViewById<TextInputLayout>(R.id.textLayout)
        val edtText = findViewById<TextInputEditText>(R.id.edtLayout).text
        if(edtText!!.isEmpty()){
            til.error = "You need to enter a name"

        }
        else {

            til.error = null
        }



    }
}
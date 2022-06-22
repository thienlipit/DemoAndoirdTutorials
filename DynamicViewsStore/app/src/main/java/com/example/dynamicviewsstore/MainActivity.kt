package com.example.dynamicviewsstore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.dynamicviewsstore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // initiate viewBinding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    // create an arraylist in which
    // we will store user data
    private var languageList = ArrayList<Language>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // This addButton is used to add a new view
        // in the parent linear layout
        binding.buttonAdd.setOnClickListener {
            addNewView()
        }

        // This Submit Button is used to store all the
        // data entered by user in arraylist
        binding.buttonSubmitList.setOnClickListener {
            saveData()
        }

        // This Show button is used to show data
        // stored in the arraylist.
        binding.buttonShowList.setOnClickListener {
            showData()
        }
    }

    // This function is called after
    // user clicks on addButton
    private fun addNewView() {
        // this method inflates the single item layout
        // inside the parent linear layout
        val inflater = LayoutInflater.from(this).inflate(R.layout.row_add_language, null)
        binding.parentLinearLayout.addView(inflater, binding.parentLinearLayout.childCount)

    }

    // This function is called after user
    // clicks on Submit Button
    private fun saveData() {
        languageList.clear()
        // this counts the no of child layout
        // inside the parent Linear layout
        val count = binding.parentLinearLayout.childCount
        var v: View?

        for (i in 0 until count) {
            v = binding.parentLinearLayout.getChildAt(i)

            val languageName: EditText = v.findViewById(R.id.et_name)
            val experience: Spinner = v.findViewById(R.id.exp_spinner)

            // create an object of Language class
            val language = Language()
            language.name = languageName.text.toString()
            language.exp = experience.selectedItem as String

            // add the data to arraylist
            languageList.add(language)
        }
    }

    // This function is called after user
    // clicks on Show List data button
    private fun showData() {
        val count = binding.parentLinearLayout.childCount
        for (i in 0 until count) {
            Toast.makeText(this, "Language at $i is ${languageList[i].name} and experience is ${languageList[i].exp} ", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
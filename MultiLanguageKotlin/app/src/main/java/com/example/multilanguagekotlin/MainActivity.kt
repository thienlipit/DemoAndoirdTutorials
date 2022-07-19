package com.example.multilanguagekotlin

import android.os.Bundle
import com.example.multilanguagekotlin.databinding.ActivityMainBinding
import com.zeugmasolutions.localehelper.Locales

class MainActivity :  BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setTitle(R.string.app_name)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bntEn.setOnClickListener {
            updateLocale(Locales.English)
        }

        binding.bntVi.setOnClickListener {
            updateLocale(Locales.Vietnamese)
        }
    }

//    override fun updateLocale(locale: Locale) {
//        super.updateLocale(locale)
//        setTitle(R.string.app_name)
//    }

}
package com.example.familyconnect.views.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.familyconnect.R
import com.example.familyconnect.databinding.ActivityMainBinding
import com.example.familyconnect.views.group.GroupFragment



class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.hide()

        val bottomNav = binding.bottomNavigation

        if(savedInstanceState == null) {
            val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, GroupFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        bottomNav.setOnItemSelectedListener {
            var fragment: Fragment? = null
            when(it.itemId) {
                R.id.menu_group -> {
                    bottomNav.menu.findItem(R.id.menu_group).isChecked = true
                    fragment = GroupFragment()
                }
                R.id.menu_chat -> {
                    bottomNav.menu.findItem(R.id.menu_chat).isChecked = true
                    fragment = ChatFragment()
                }
                R.id.menu_other -> {
                    bottomNav.menu.findItem(R.id.menu_other).isChecked = true
                    fragment = SettingFragment()
                }
            }
            if( fragment != null) {
                val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
                //this is a helper class that replaces the container with the fragment. You can replace or add fragments.
                transaction.replace(R.id.container, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }

            return@setOnItemSelectedListener false
        }
    }
/*
    private fun openFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        //this is a helper class that replaces the container with the fragment. You can replace or add fragments.
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        //if you add fragments it will be added to the backStack.
        // If you replace the fragment it will add only the last fragment
        transaction.commit() // commit() performs the action
    }

 */
}
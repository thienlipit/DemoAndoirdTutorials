package com.example.demolistadapterkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.demolistadapterkotlin.adapters.UserAdapter
import com.example.demolistadapterkotlin.databinding.ActivityMainBinding
import com.example.demolistadapterkotlin.models.User
import com.example.demolistadapterkotlin.viewmodels.UserViewModel

class MainActivity : AppCompatActivity() {
    var adapter: UserAdapter? = null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listUser: MutableList<User> = mutableListOf()
        val user = UserViewModel()
        user.list.listUsers = listUser
        binding.viewModel = user

        val recyclerView = binding.recyclerView
        adapter = UserAdapter()
        recyclerView.adapter = adapter

        binding.btnAdd.setOnClickListener {
            val newUser = User()
            listUser.add(newUser)
            adapter!!.notifyDataSetChanged()
        }

        binding.btnSave.setOnClickListener {
            val count = listUser.size
            if(count > 0){
                for(item in listUser){
                    if(item.name.isEmpty() || item.age.isEmpty()){
                        Toast.makeText(this, "empty field existed", Toast.LENGTH_LONG).show()
                    } else Toast.makeText(this, "Ok user", Toast.LENGTH_LONG).show()

                }
            }
        }


    }
}
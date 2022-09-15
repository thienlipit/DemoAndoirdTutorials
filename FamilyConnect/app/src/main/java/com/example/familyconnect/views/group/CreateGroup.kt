package com.example.familyconnect.views.group

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.familyconnect.databinding.ActivityCreateGroupBinding
import com.example.familyconnect.viewmodels.GroupViewModel

class CreateGroup : AppCompatActivity() {
    private val groupViewModel: GroupViewModel by viewModels()
    private lateinit var binding: ActivityCreateGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val titleCreateGroup = "Create Group"
        supportActionBar?.title = titleCreateGroup

        val btnCreateGroup = binding.btnCreateGroup
        val groupName = binding.edtGroupName
        val groupId = binding.edtGroupId

        btnCreateGroup.setOnClickListener {
            if(groupName.text.toString().isNotEmpty() && groupId.text.toString().isNotEmpty())
            {
                groupViewModel.createGroup(groupName.text.toString(), groupId.text.toString())
            }
            else {
                Toast.makeText(this, "Group name or Group ID is null", Toast.LENGTH_LONG).show()
            }

        }
    }
}
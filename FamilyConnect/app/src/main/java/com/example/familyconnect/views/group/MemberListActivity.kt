package com.example.familyconnect.views.group

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnect.databinding.ActivityMemberListBinding
import com.example.familyconnect.models.User
import com.example.familyconnect.viewmodels.GroupViewModel
import com.example.familyconnect.views.chatgroup.ChatGroupActivity

class MemberListActivity : AppCompatActivity() {
    private val groupViewModel: GroupViewModel by viewModels()
    lateinit var binding: ActivityMemberListBinding

    private lateinit var memberRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: GroupMemberAdapter
    private val memberList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemberListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val groupName = intent.getStringExtra("groupName").toString()
        val groupId = intent.getStringExtra("groupId").toString()
        supportActionBar?.title = groupName

        val imgBtnChat = binding.imgBtnChat //ImageButton "CHAT NOW"
        imgBtnChat.setOnClickListener {
            val intent = Intent(this@MemberListActivity, ChatGroupActivity::class.java)
            intent.putExtra("room1", groupName+groupId)
            intent.putExtra("room2", groupId+groupName)
            startActivity(intent)
        }



        userList = ArrayList()
        adapter = GroupMemberAdapter(this, userList)
        memberRecyclerView = binding.rvListMember

        memberRecyclerView.layoutManager = LinearLayoutManager(this)
        memberRecyclerView.adapter = adapter

//-------------------------------------------------------------------------------------------------
        groupViewModel.showListMember(groupId, userList, memberList,adapter)

    }
}
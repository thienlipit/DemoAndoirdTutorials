package com.example.familyconnect.views.group

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familyconnect.databinding.FragmentGroupBinding
import com.example.familyconnect.models.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class GroupFragment : Fragment() {
    private lateinit var groupList: ArrayList<Group>

    private var _binding: FragmentGroupBinding? = null
    private val binding get() = _binding!!
    private  lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseReference = FirebaseDatabase.getInstance().reference
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnFoating = binding.btnFloating

        btnFoating.setOnClickListener {
            val intent = Intent(context, CreateGroup::class.java)
            startActivity(intent)
        }

        binding.recyclerView.apply {

            groupList = ArrayList()
            databaseReference.child("groups").addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    groupList.clear()
                    for(postSnapshot in snapshot.children){
                        val currentGroup = postSnapshot.getValue(Group::class.java)
                        groupList.add(currentGroup!!)
                    }
                    Log.d("userList", groupList.toString())

                    adapter?.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            layoutManager = LinearLayoutManager(activity)
            adapter = GroupAdapter(context,groupList)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
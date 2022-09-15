package com.example.familyconnect.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.familyconnect.models.Group
import com.example.familyconnect.models.User
import com.example.familyconnect.views.group.GroupMemberAdapter
import com.example.familyconnect.views.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@SuppressLint("StaticFieldLeak")
class GroupViewModel(application: Application): AndroidViewModel(application) {
    private val context = application.applicationContext
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    fun createGroup(groupName: String, groupId: String){
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid!!

        addGroupToDatabase(groupName, groupId, uid, auth.currentUser?.email!!)
        val intent = Intent(context, MainActivity::class.java )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    private fun addGroupToDatabase(groupName: String, groupId: String, uid: String, email: String) {
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("groups").child(groupId).setValue(Group(groupName, groupId, uid, email))
    }

    fun showListMember( groupId: String, userList: ArrayList<User>, memberList: ArrayList<String>,
            adapter: GroupMemberAdapter){
        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference.child("groups").child(groupId).child("members")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //userList.clear()
                    for(postSnapshot in snapshot.children){
                        val currentUser = postSnapshot.value
                        memberList.add(currentUser as String)

                    }

                    //Log.d("member", memberList.toString())
                    databaseReference.child("user").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            userList.clear()
                            for(postSnapshot in snapshot.children){
                                val currentUser = postSnapshot.getValue(User::class.java)

                                for(x in memberList){
                                    if(x == currentUser?.uid){
                                        userList.add(currentUser!!)
                                    }
                                }

                            }
                            //Log.d("userList", userList.toString())

                            adapter.notifyDataSetChanged()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}
package com.example.familyconnect.views.group

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnect.databinding.GroupFamilyBinding
import com.example.familyconnect.models.Group
import com.example.familyconnect.views.chatall.ChatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class GroupAdapter(val context: Context, val groupList : ArrayList<Group>): RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {
    lateinit var binding: GroupFamilyBinding
    lateinit var auth: FirebaseAuth
    lateinit var dbref: DatabaseReference
    val userList = arrayListOf<String>()
    private lateinit var userNow: String
    var frag : Boolean = false
    lateinit var groupIdNow: String

    inner class GroupViewHolder(itemView: GroupFamilyBinding) : RecyclerView.ViewHolder(binding.root){
        val txtGroupName = binding.tvGroupFamily
        val btnJoin = binding.btnGroupJoin

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        binding = GroupFamilyBinding.inflate(LayoutInflater.from(context), parent, false)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val currentGroup = groupList[position]
        holder.txtGroupName.text = currentGroup.groupName
        holder.itemView.setOnClickListener {
            Log.d("Row", "row clicked")
        }
        holder.btnJoin.setOnClickListener {
            Log.d("Row", "button clicked")
            auth = FirebaseAuth.getInstance()
            dbref = FirebaseDatabase.getInstance().reference

            userNow = auth.currentUser?.uid.toString()
            groupIdNow = currentGroup.groupId.toString()
//            Log.d("User: ", auth.currentUser?.uid.toString())

            dbref.child("groups").child(groupIdNow).child("members")
                .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //userList.clear()
                    for(postSnapshot in snapshot.children){
                        val currentUser = postSnapshot.value
                        userList.add(currentUser as String)

                    }
                    for(u in userList) {
                        if( u == userNow){
                            frag = true
                            break
                        } else frag = false
                    }

                    if(frag) {
                        val intent = Intent(context, MemberListActivity::class.java)
                        intent.putExtra("groupId", groupIdNow)
                        intent.putExtra("groupName", currentGroup.groupName)
                        context.startActivity(intent)
                    } else{
                        dbref.child("groups").child(groupIdNow)
                        .child("members").push().setValue(userNow)
                        val intent = Intent(context, MemberListActivity::class.java)
                        intent.putExtra("groupId", groupIdNow)
                        intent.putExtra("groupName", currentGroup.groupName)
                        context.startActivity(intent)
                    }
//                    Log.d("userList", userList.toString())
//                    Log.d("userList", userList.size.toString())
//                    Log.d("userList", userList[0])
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })

        }
    }

    override fun getItemCount(): Int {
        return groupList.size
    }
}



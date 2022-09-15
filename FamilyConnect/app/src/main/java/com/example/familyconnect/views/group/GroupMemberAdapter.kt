package com.example.familyconnect.views.group

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnect.views.chatall.ChatActivity
import com.example.familyconnect.models.User
import com.example.familyconnect.databinding.UserLayoutBinding
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class GroupMemberAdapter(val context: Context, private val memberList : ArrayList<User>):
    RecyclerView.Adapter<GroupMemberAdapter.GroupMemberViewHolder>() {

    lateinit var binding: UserLayoutBinding
    private lateinit var uri: Uri
    private lateinit var cirImg: CircleImageView

    inner class GroupMemberViewHolder(itemView: UserLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val tvMemberName = binding.tvUserName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMemberViewHolder {
        binding = UserLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return GroupMemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupMemberViewHolder, position: Int) {
        val currentMember = memberList[position]
        holder.tvMemberName.text = currentMember.name

        uri = Uri.parse(currentMember.profilePic.toString())
        cirImg = binding.profileImage
        Picasso.get().load(uri).into(cirImg)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("name", currentMember.name)
            intent.putExtra("uid", currentMember.uid)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return memberList.size
    }
}
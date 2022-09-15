package com.example.familyconnect.views.chatall

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnect.databinding.UserLayoutBinding
import com.example.familyconnect.models.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(val context: Context, private val userList: ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    lateinit var binding: UserLayoutBinding
    private lateinit var uri: Uri
    private lateinit var cirImg: CircleImageView

    inner  class UserViewHolder : RecyclerView.ViewHolder(binding.root){
        val userName = binding.tvUserName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        binding = UserLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return  UserViewHolder()
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.userName.text = currentUser.name

        uri = Uri.parse(currentUser.profilePic.toString())
        cirImg = binding.profileImage
        Picasso.get().load(uri).into(cirImg)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)

            context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
package com.example.familyconnect.views.chatall

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnect.R
import com.example.familyconnect.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class MessageAdapter(val context: Context, private val messageList : ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef : DatabaseReference
    private lateinit var uri: Uri


    private val ITEM_RECEIVE = 1
    private val ITEM_SENT = 2

    private val ITEM_IMAGE_RECEIVE = 3
    private val ITEM_IMAGE_SENT = 4


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            1 -> {
                val view: View = LayoutInflater.from(context).inflate(R.layout.receive, parent, false)
                ReceiveViewHolder(view)
            }
            2 -> {
                val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
                SentViewHolder(view)
            }
            3 -> {
                val view: View = LayoutInflater.from(context).inflate(R.layout.receiveimage, parent, false)
                ReceiveImageViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(context).inflate(R.layout.sentimage, parent, false)
                SentImageViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().reference
        val currentMessage = messageList[position]
        if (holder.javaClass == SentViewHolder::class.java) {
            // do the stuff for sent view holder
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
            holder.sentTime.text = currentMessage.createdAt
        } else {
            if (holder.javaClass == ReceiveViewHolder::class.java) {
                // do the stuff for receive view holder
                val viewHolder = holder as ReceiveViewHolder

                dbRef.child("user").child(currentMessage.senderId.toString()).child("name").get()
                    .addOnSuccessListener {
//                        Log.d("name", it.value.toString())
                        holder.receiveName.text = it.value.toString()
                    }.addOnFailureListener {
                    Log.d("firebase", "Error getting data", it)
                }

                dbRef.child("user").child(currentMessage.senderId.toString()).child("profilePic")
                    .get().addOnSuccessListener {

                    uri = Uri.parse(it.value.toString())
                    Picasso.get().load(uri).into(holder.receiveProfilePic)
                }.addOnFailureListener {
                    Log.d("firebase", "Error getting data", it)
                }

                holder.receiveMessage.text = currentMessage.message
                holder.receiveTime.text = currentMessage.createdAt
            } else {
                if(holder.javaClass == SentImageViewHolder::class.java){
                    //work for sent image ----------------------------------------------------------
                    val viewHolder = holder as SentImageViewHolder
                    uri = Uri.parse(currentMessage.message.toString())
                    Picasso.get().load(uri).into(holder.sentMessageImage)

                    holder.sentTimeImage.text = currentMessage.createdAt



                } else {
                    //work for receive image -------------------------------------------------------
                    val viewHolder = holder as ReceiveImageViewHolder

                    dbRef.child("user").child(currentMessage.senderId.toString()).child("name").get()
                        .addOnSuccessListener {
                            //Log.d("name", it.value.toString())
                            holder.receiveNameImage.text = it.value.toString()
                        }.addOnFailureListener {
                            Log.d("firebase", "Error getting data", it)
                        }

                    dbRef.child("user").child(currentMessage.senderId.toString()).child("profilePic")
                        .get().addOnSuccessListener {

                            uri = Uri.parse(it.value.toString())
                            Picasso.get().load(uri).into(holder.receiveProfilePicImage)
                        }.addOnFailureListener {
                            Log.d("firebase", "Error getting data", it)
                        }

                    uri = Uri.parse(currentMessage.message.toString())
                    Picasso.get().load(uri).into(holder.receiveMessageImage)

                    holder.receiveTimeImage.text = currentMessage.createdAt

                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        return if(currentMessage.type == true) {
            if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
                ITEM_SENT
            } else {
                ITEM_RECEIVE
            }
        } else {
            if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
                ITEM_IMAGE_SENT
            } else {
                ITEM_IMAGE_RECEIVE
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sentMessage: TextView = itemView.findViewById(R.id.txt_sent_message)
        val sentTime: TextView = itemView.findViewById(R.id.txt_sent_time)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val receiveMessage: TextView = itemView.findViewById(R.id.txt_receive_message)
        val receiveTime: TextView = itemView.findViewById(R.id.text_timestamp_other)
        val receiveName: TextView = itemView.findViewById(R.id.text_user_other)
        val receiveProfilePic: ImageView = itemView.findViewById(R.id.image_profile_other)
    }

    class SentImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sentMessageImage: ImageView = itemView.findViewById(R.id.sent_message_image)
        val sentTimeImage: TextView = itemView.findViewById(R.id.txt_sent_time_image)
    }

    class ReceiveImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val receiveMessageImage: ImageView = itemView.findViewById(R.id.message_other_image)
        val receiveTimeImage: TextView = itemView.findViewById(R.id.text_timestamp_other_image)
        val receiveNameImage: TextView = itemView.findViewById(R.id.text_user_other_image)
        val receiveProfilePicImage: ImageView = itemView.findViewById(R.id.image_profile_other_image)
    }
}
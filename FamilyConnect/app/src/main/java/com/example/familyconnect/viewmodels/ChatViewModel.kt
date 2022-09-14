package com.example.familyconnect.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnect.models.Message
import com.example.familyconnect.views.chatall.ChatActivity
import com.example.familyconnect.views.chatall.MessageAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("StaticFieldLeak")
class ChatViewModel(application: Application): AndroidViewModel(application) {

    private lateinit var mDbRef : DatabaseReference
    private val senderId = FirebaseAuth.getInstance().currentUser?.uid
    var storageRef = Firebase.storage.reference

    companion object {
        const val SENT_IMAGE = false
        const val SENT_TEXT = true
    }

    fun addMessageToDB(message: String, senderRoom: String, receiverRoom: String){
        mDbRef = FirebaseDatabase.getInstance().reference

        val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formatted = current.format(formatter)
        val messageObject = Message(message, senderId, formatted, SENT_TEXT)

        //push chat message to the real database in Firebase
        mDbRef.child("chats").child(senderRoom).child("messages").push()
            .setValue(messageObject)
            .addOnSuccessListener {
                mDbRef.child("chats").child(receiverRoom).child("messages").push()
                    .setValue(messageObject)
            }
    }

    fun chatSendImageMessage(it: Uri, senderRoom: String, receiverRoom: String){
        mDbRef = FirebaseDatabase.getInstance().reference
        val fileImageName = UUID.randomUUID()

        val fileRef = storageRef.child("ImageChats").child("$fileImageName.jpg")
        fileRef.putFile(it).addOnSuccessListener {
            val userImageRef = storageRef.child("ImageChats").child("$fileImageName.jpg")
            userImageRef.downloadUrl.addOnSuccessListener {
                val uriImage = it

                val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now()
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                val formatted = current.format(formatter)
                val messageObject =
                    Message(uriImage.toString(), senderId, formatted, SENT_IMAGE
                    )

                //push chat message to the real database in Firebase
                mDbRef.child("chats").child(senderRoom).child("messages").push()
                    .setValue(messageObject)
                    .addOnSuccessListener {
                        mDbRef.child("chats").child(receiverRoom).child("messages").push()
                            .setValue(messageObject)
                    }
            }
        }
    }

    fun addDataToRecyclerView(senderRoom: String, messageList: ArrayList<Message>, chatRecyclerView: RecyclerView,
                messageAdapter: MessageAdapter) {
        mDbRef = FirebaseDatabase.getInstance().reference

        mDbRef.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()

                    for (postSnapshot in snapshot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    chatRecyclerView.scrollToPosition(messageList.size - 1)
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    fun getSenderId(): String{
        val senderId = FirebaseAuth.getInstance().currentUser?.uid
        return senderId.toString()
    }
}
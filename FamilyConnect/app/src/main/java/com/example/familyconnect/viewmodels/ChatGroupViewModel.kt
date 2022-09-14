package com.example.familyconnect.viewmodels

import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.familyconnect.models.Message
import com.example.familyconnect.views.chatgroup.MessageGroupAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ChatGroupViewModel : ViewModel() {
    private lateinit var mDbRef : DatabaseReference
    private val senderId = FirebaseAuth.getInstance().currentUser?.uid
    var storageRef = Firebase.storage.reference

    fun addGroupMessageToDB(message: String, senderRoom: String, receiverRoom: String){
        mDbRef = FirebaseDatabase.getInstance().reference
        val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formatted = current.format(formatter)
        val messageObject = Message(message, senderId, formatted, ChatViewModel.SENT_TEXT)

        //push chat message to the real database in Firebase
        mDbRef.child("GroupChats").child(senderRoom).child("messages").push()
            .setValue(messageObject)
            .addOnSuccessListener {
                mDbRef.child("GroupChats").child(receiverRoom).child("messages").push()
                    .setValue(messageObject)
            }
    }

    fun chatGroupSendImageMessage(it: Uri, senderRoom: String, receiverRoom: String){
        mDbRef = FirebaseDatabase.getInstance().reference
        val fileImageName = UUID.randomUUID()

        val fileRef = storageRef.child("ImageChatsGroup").child("$fileImageName.jpg")
        fileRef.putFile(it).addOnSuccessListener {
            val userImageRef = storageRef.child("ImageChatsGroup").child("$fileImageName.jpg")
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
                    Message(uriImage.toString(), senderId, formatted, ChatViewModel.SENT_IMAGE
                    )

                //push chat message to the real database in Firebase
                mDbRef.child("GroupChats").child(senderRoom).child("messages").push()
                    .setValue(messageObject)
                    .addOnSuccessListener {
                        mDbRef.child("GroupChats").child(receiverRoom).child("messages").push()
                            .setValue(messageObject)
                    }
            }
        }
    }

    fun addGroupDataToRecyclerView(senderRoom: String, messageList: ArrayList<Message>, chatRecyclerView: RecyclerView,
                              messageAdapter: MessageGroupAdapter
    ) {
        mDbRef = FirebaseDatabase.getInstance().reference

        mDbRef.child("GroupChats").child(senderRoom).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    chatRecyclerView.scrollToPosition(messageList.size -1)
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

}
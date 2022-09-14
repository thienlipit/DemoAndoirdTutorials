package com.example.familyconnect.views.chatgroup

import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familyconnect.databinding.ActivityChatGroupBinding
import com.example.familyconnect.models.Message
import com.example.familyconnect.viewmodels.ChatGroupViewModel

class ChatGroupActivity : AppCompatActivity() {
    private val chatGroupViewModel: ChatGroupViewModel by viewModels()
    lateinit var binding: ActivityChatGroupBinding

    private lateinit var messageAdapter : MessageGroupAdapter
    private lateinit var messageList : ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatGroupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val senderRoom = intent.getStringExtra("room1").toString()
        val receiverRoom = intent.getStringExtra("room2").toString()

//        val senderRoom = "senderRoom"
//        val receiverRoom = "receiverRoom"

        supportActionBar?.title = "Group chat"

        val chatRecyclerView = binding.rvChatGroup
        val messageBox = binding.messageBoxGroup
        val sendButton = binding.sendMessageGroup
        val sendImageButton = binding.imgViewAlbum

        messageList = ArrayList()
        messageAdapter = MessageGroupAdapter(this, messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        //logic for adding data to recyclerview
        chatGroupViewModel.addGroupDataToRecyclerView(senderRoom, messageList,chatRecyclerView, messageAdapter)

        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                chatGroupViewModel.chatGroupSendImageMessage(it, senderRoom, receiverRoom)

            })
        sendImageButton.setOnClickListener {
            getImage.launch("image/*")
        }

        //adding the message to database
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            chatGroupViewModel.addGroupMessageToDB(message, senderRoom, receiverRoom)
            messageBox.setText("")
        }

    }
}
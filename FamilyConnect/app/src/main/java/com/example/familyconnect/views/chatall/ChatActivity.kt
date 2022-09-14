package com.example.familyconnect.views.chatall

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.familyconnect.R
import com.example.familyconnect.databinding.ActivityChatBinding
import com.example.familyconnect.models.Message
import com.example.familyconnect.viewmodels.ChatViewModel
import com.example.familyconnect.views.VideoCall

class ChatActivity : AppCompatActivity() {
    private val chatViewModel: ChatViewModel by viewModels()
    lateinit var binding: ActivityChatBinding

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        supportActionBar?.title = name

        val senderId = chatViewModel.getSenderId()
        val senderRoom = receiverUid + senderId
        val receiverRoom = senderId + receiverUid

        val chatRecyclerView = binding.rvChat
        val messageBox = binding.messageBox
        val sendButton = binding.imgView
        val sendImageButton = binding.imgViewAlbum

        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        //logic for adding data to recyclerview
        chatViewModel.addDataToRecyclerView(senderRoom, messageList, chatRecyclerView, messageAdapter )

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                chatViewModel.chatSendImageMessage(it, senderRoom, receiverRoom)
            })
        sendImageButton.setOnClickListener {
            getImage.launch("image/*")

        }

        //adding the message to database
        sendButton.setOnClickListener {
            val message = messageBox.text.toString()
            chatViewModel.addMessageToDB(message, senderRoom, receiverRoom)
            messageBox.setText("")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu to use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_call -> {
                val intent = Intent(this@ChatActivity, VideoCall::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }
}
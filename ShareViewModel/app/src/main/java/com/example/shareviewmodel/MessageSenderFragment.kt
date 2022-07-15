package com.example.shareviewmodel

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider

class MessageSenderFragment : Fragment() {
//    private val viewModel :SharedViewModel by viewModels()
//val model: SharedViewModel by activityViewModels()
private val viewModel: SharedViewModel by viewModels()
//private val model: SharedViewModel by activityViewModels()


    private val viewModel1: SharedViewModel by activityViewModels()


    // to send message
    lateinit var btn: Button

    // to write message
    lateinit var writeMSg: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message_sender, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // reference for button and EditText
        btn = view.findViewById(R.id.button)
        writeMSg = view.findViewById(R.id.writeMessage)

        // create object of SharedViewModel
        val model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // call function "sendMessage" defined in SharedVieModel
        // to store the value in message.

//        btn.setOnClickListener { model.sendMessage(writeMSg.text.toString()) }


        btn.setOnClickListener {
            Log.d("Send", writeMSg.text.toString())
            viewModel1.sendMessage(writeMSg.text.toString()) }



    }
}
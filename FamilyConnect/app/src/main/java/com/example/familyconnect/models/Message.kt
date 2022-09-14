package com.example.familyconnect.models

class Message {
    var message: String? = null
    var senderId: String? = null
    var createdAt: String? = null
    var type: Boolean? = false
    //false is sent image message
    //true is sent text message
    constructor(){}

    constructor(message: String, senderId: String?, createdAt: String, type: Boolean){
        this.message = message
        this.senderId = senderId
        this.createdAt = createdAt
        this.type = type
    }
}
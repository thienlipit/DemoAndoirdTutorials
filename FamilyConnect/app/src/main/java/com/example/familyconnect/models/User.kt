package com.example.familyconnect.models

class User {
    var name: String? = null
    var email: String? = null
    var uid: String? = null
    var profilePic: String? = null

    constructor() {}
    constructor(name: String, email: String, uid: String, profilePic: String?){
        this.name = name
        this.email = email
        this.uid = uid
        this.profilePic = profilePic
    }
}
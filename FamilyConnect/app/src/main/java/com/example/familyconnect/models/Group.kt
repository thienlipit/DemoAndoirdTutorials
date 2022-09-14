package com.example.familyconnect.models

class Group {
    var groupName : String? = null
    var groupId : String? = null
    var uid: String? = null
    var userName: String? = null

    constructor(){}
    constructor(groupName : String?, groupId: String?, uid: String?, userName: String?){
        this.groupName = groupName
        this.groupId = groupId
        this.uid = uid
        this.userName = userName
    }
}
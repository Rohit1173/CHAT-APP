package com.example.basicsocket

data class message(
    val username: String,
    val chat:String,
    var viewType:Int
)
data class senddata(
    val chat: String
)
data class recieve(
    val username:String,
    val chat:String
)
data class userdata(
    val name:String,
    val room:String
)
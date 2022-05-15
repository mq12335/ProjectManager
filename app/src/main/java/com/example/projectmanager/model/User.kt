package com.example.projectmanager.model

data class User(
    var name: String = "",
    var identity: String = "",
    var email: String = "",
    var password: String = "",
    var notification: String = "",
    var previousNotification: String = ""
)
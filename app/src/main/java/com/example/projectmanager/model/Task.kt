package com.example.projectmanager.model

data class Task(
    var name: String = "",
    var project: String = "",
    var member: String = "",
    var status: String = "",
    var deadline: String = ""
)
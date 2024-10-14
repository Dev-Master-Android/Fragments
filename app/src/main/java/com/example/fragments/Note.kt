package com.example.fragments
data class Note(
    val id: Int,
    val text: String,
    var isCompleted: Boolean,
    val dateCreated: String
)

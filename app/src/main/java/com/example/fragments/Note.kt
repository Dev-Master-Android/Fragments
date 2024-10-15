package com.example.fragments

import java.io.Serializable

data class Note(
    val id: Int,
    var text: String,
    var isCompleted: Boolean,
    val dateCreated: String
) : Serializable

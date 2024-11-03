package com.example.lab7_notesmanager

import java.io.Serializable

data class Note(
    var id: String? = null,
    var title: String = "",
    var content: String = "",
    var createdAt: Long = System.currentTimeMillis()
) : Serializable

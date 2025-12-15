package com.example.taskandnotesapp.ui.notes

data class Note(
    val id: Int,
    val userId: String,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
)


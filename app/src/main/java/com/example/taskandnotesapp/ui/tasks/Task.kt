package com.example.taskandnotesapp.ui.tasks

data class Task(
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val priority: String,
    val dueDate: Long?,
    val createdAt: Long
)


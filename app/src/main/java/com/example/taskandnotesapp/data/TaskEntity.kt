package com.example.taskandnotesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val title: String,
    val description: String = "",
    val isDone: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

package com.example.taskandnotesapp.ui.tasks

import java.text.SimpleDateFormat
import java.util.*

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}


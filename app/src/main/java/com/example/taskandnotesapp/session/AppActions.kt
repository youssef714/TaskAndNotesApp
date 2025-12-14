package com.example.taskandnotesapp.session

import com.example.taskandnotesapp.data.*

object AppActions {

    suspend fun login(userDao: UserDao, email: String, password: String): Int? {
        val user = userDao.getUserByEmail(email)
        return if (user != null && user.password == password) user.id else null
    }

    suspend fun register(userDao: UserDao, username: String, email: String, password: String): Int? {
        val existing = userDao.getUserByEmail(email)
        if (existing != null) return null
        return userDao.insertUser(UserEntity(username = username, email = email, password = password)).toInt()
    }

    suspend fun addTask(taskDao: TaskDao, task: TaskEntity): Long {
        return taskDao.insertTask(task)
    }

    suspend fun deleteTask(taskDao: TaskDao, id: Int) {
        taskDao.deleteById(id)
    }

    suspend fun pinNote(noteDao: NoteDao, id: Int, pinned: Boolean) {
        noteDao.setPinned(id, pinned)
    }
}
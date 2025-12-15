package com.example.taskandnotesapp.session

import com.example.taskandnotesapp.data.*

object AppActions {

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
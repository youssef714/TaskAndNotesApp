package com.example.taskandnotesapp.session

import com.example.taskandnotesapp.data.NoteDao
import com.example.taskandnotesapp.data.TaskDao
import com.example.taskandnotesapp.data.TaskEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AppActionsTest {

    private val taskDao: TaskDao = mock()
    private val noteDao: NoteDao = mock()



    @Test
    fun addTask_callsInsertTask() = runTest {
        val task = TaskEntity(userId = "10", title = "T1")
        whenever(taskDao.insertTask(any())).thenReturn(55L)

        val id = AppActions.addTask(taskDao, task)

        Assert.assertEquals(55L, id)
        verify(taskDao).insertTask(task)
    }

    @Test
    fun deleteTask_callsDeleteById() = runTest {
        AppActions.deleteTask(taskDao, 99)
        verify(taskDao).deleteById(99)
    }

    @Test
    fun pinNote_callsSetPinned() = runTest {
        AppActions.pinNote(noteDao, id = 5, pinned = true)
        verify(noteDao).setPinned(5, true)
    }
}
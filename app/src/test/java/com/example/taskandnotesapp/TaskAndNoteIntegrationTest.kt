package com.example.taskandnotesapp

import com.example.taskandnotesapp.ui.tasks.Task
import com.example.taskandnotesapp.ui.notes.Note
import org.junit.Test
import org.junit.Assert.*

class TaskAndNoteIntegrationTest {
    
    @Test
    fun taskAndNote_canBeCreatedForSameUser() {
        val userId = 100
        
        val task = Task(
            id = 1,
            userId = userId,
            title = "User Task",
            description = "Task Description",
            isCompleted = false,
            priority = "High",
            dueDate = null,
            createdAt = System.currentTimeMillis()
        )
        
        val note = Note(
            id = 1,
            userId = userId,
            title = "User Note",
            content = "Note Content",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        assertEquals(userId, task.userId)
        assertEquals(userId, note.userId)
        assertNotNull(task)
        assertNotNull(note)
    }
    
    @Test
    fun taskAndNote_differentUsers() {
        val task = Task(
            id = 1,
            userId = 100,
            title = "Task",
            description = "Description",
            isCompleted = false,
            priority = "Medium",
            dueDate = null,
            createdAt = System.currentTimeMillis()
        )
        
        val note = Note(
            id = 1,
            userId = 200,
            title = "Note",
            content = "Content",
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        
        assertNotEquals(task.userId, note.userId)
    }
    
    @Test
    fun task_completionFlow() {
        val task = Task(
            id = 1,
            userId = 100,
            title = "Task",
            description = "Description",
            isCompleted = false,
            priority = "High",
            dueDate = null,
            createdAt = System.currentTimeMillis()
        )
        
        assertFalse(task.isCompleted)
        
        val completedTask = task.copy(isCompleted = true)
        assertTrue(completedTask.isCompleted)
        
        val uncompletedTask = completedTask.copy(isCompleted = false)
        assertFalse(uncompletedTask.isCompleted)
    }
    
    @Test
    fun note_updateFlow() {
        val originalTimestamp = 1234567890L
        val note = Note(
            id = 1,
            userId = 100,
            title = "Original",
            content = "Original Content",
            createdAt = originalTimestamp,
            updatedAt = originalTimestamp
        )
        
        assertEquals(originalTimestamp, note.createdAt)
        assertEquals(originalTimestamp, note.updatedAt)
        
        val updatedTimestamp = originalTimestamp + 1000L
        val updatedNote = note.copy(
            title = "Updated",
            content = "Updated Content",
            updatedAt = updatedTimestamp
        )
        
        assertEquals("Updated", updatedNote.title)
        assertEquals("Updated Content", updatedNote.content)
        assertEquals(originalTimestamp, updatedNote.createdAt)
        assertTrue(updatedNote.updatedAt > originalTimestamp)
        assertEquals(updatedTimestamp, updatedNote.updatedAt)
    }
}


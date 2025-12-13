package com.example.taskandnotesapp.ui.tasks

import org.junit.Test
import org.junit.Assert.*

class TaskTest {
    
    @Test
    fun task_creation_isCorrect() {
        val task = Task(
            id = 1,
            userId = 100,
            title = "Test Task",
            description = "Test Description",
            isCompleted = false,
            priority = "High",
            dueDate = 1234567890L,
            createdAt = 1234567890L
        )
        
        assertEquals(1, task.id)
        assertEquals(100, task.userId)
        assertEquals("Test Task", task.title)
        assertEquals("Test Description", task.description)
        assertFalse(task.isCompleted)
        assertEquals("High", task.priority)
        assertEquals(1234567890L, task.dueDate)
        assertEquals(1234567890L, task.createdAt)
    }
    
    @Test
    fun task_copy_isCorrect() {
        val originalTask = Task(
            id = 1,
            userId = 100,
            title = "Original",
            description = "Original Description",
            isCompleted = false,
            priority = "Low",
            dueDate = null,
            createdAt = 1234567890L
        )
        
        val copiedTask = originalTask.copy(
            title = "Updated",
            isCompleted = true,
            priority = "High"
        )
        
        assertEquals("Updated", copiedTask.title)
        assertTrue(copiedTask.isCompleted)
        assertEquals("High", copiedTask.priority)
        assertEquals(originalTask.id, copiedTask.id)
        assertEquals(originalTask.userId, copiedTask.userId)
        assertEquals(originalTask.description, copiedTask.description)
    }
    
    @Test
    fun task_equality_isCorrect() {
        val task1 = Task(
            id = 1,
            userId = 100,
            title = "Task",
            description = "Description",
            isCompleted = false,
            priority = "Medium",
            dueDate = null,
            createdAt = 1234567890L
        )
        
        val task2 = Task(
            id = 1,
            userId = 100,
            title = "Task",
            description = "Description",
            isCompleted = false,
            priority = "Medium",
            dueDate = null,
            createdAt = 1234567890L
        )
        
        assertEquals(task1, task2)
        assertEquals(task1.hashCode(), task2.hashCode())
    }
}


package com.example.taskandnotesapp.ui.notes

import org.junit.Test
import org.junit.Assert.*

class NoteTest {
    
    @Test
    fun note_creation_isCorrect() {
        val note = Note(
            id = 1,
            userId = 100,
            title = "Test Note",
            content = "Test Content",
            createdAt = 1234567890L,
            updatedAt = 1234567890L
        )
        
        assertEquals(1, note.id)
        assertEquals(100, note.userId)
        assertEquals("Test Note", note.title)
        assertEquals("Test Content", note.content)
        assertEquals(1234567890L, note.createdAt)
        assertEquals(1234567890L, note.updatedAt)
    }
    
    @Test
    fun note_copy_isCorrect() {
        val originalNote = Note(
            id = 1,
            userId = 100,
            title = "Original",
            content = "Original Content",
            createdAt = 1234567890L,
            updatedAt = 1234567890L
        )
        
        val updatedTimestamp = 1234567900L
        val copiedNote = originalNote.copy(
            title = "Updated",
            content = "Updated Content",
            updatedAt = updatedTimestamp
        )
        
        assertEquals("Updated", copiedNote.title)
        assertEquals("Updated Content", copiedNote.content)
        assertEquals(updatedTimestamp, copiedNote.updatedAt)
        assertEquals(originalNote.id, copiedNote.id)
        assertEquals(originalNote.userId, copiedNote.userId)
        assertEquals(originalNote.createdAt, copiedNote.createdAt)
    }
    
    @Test
    fun note_equality_isCorrect() {
        val note1 = Note(
            id = 1,
            userId = 100,
            title = "Note",
            content = "Content",
            createdAt = 1234567890L,
            updatedAt = 1234567890L
        )
        
        val note2 = Note(
            id = 1,
            userId = 100,
            title = "Note",
            content = "Content",
            createdAt = 1234567890L,
            updatedAt = 1234567890L
        )
        
        assertEquals(note1, note2)
        assertEquals(note1.hashCode(), note2.hashCode())
    }
    
    @Test
    fun note_withEmptyTitle_isValid() {
        val note = Note(
            id = 1,
            userId = 100,
            title = "",
            content = "Content only",
            createdAt = 1234567890L,
            updatedAt = 1234567890L
        )
        
        assertTrue(note.title.isEmpty())
        assertTrue(note.content.isNotEmpty())
    }
}


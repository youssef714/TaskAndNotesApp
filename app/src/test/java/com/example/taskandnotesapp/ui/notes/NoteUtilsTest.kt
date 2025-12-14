package com.example.taskandnotesapp.ui.notes

import org.junit.Test
import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

class NoteUtilsTest {
    
    @Test
    fun formatDate_isCorrect() {
        val timestamp = 1704067200000L // Jan 1, 2024 00:00:00 UTC
        val formatted = formatDate(timestamp)
        
        assertNotNull(formatted)
        assertTrue(formatted.contains("2024"))
        
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val expected = sdf.format(Date(timestamp))
        assertEquals(expected, formatted)
    }
    
    @Test
    fun formatDate_withDifferentDates() {
        val dates = listOf(
            1704067200000L, // Jan 1, 2024
            1735689600000L, // Jan 1, 2025
            1609459200000L  // Jan 1, 2021
        )
        
        dates.forEach { timestamp ->
            val formatted = formatDate(timestamp)
            assertNotNull(formatted)
            assertTrue(formatted.isNotEmpty())
            assertTrue(formatted.contains("202") || formatted.contains("2021") || formatted.contains("2024") || formatted.contains("2025"))
        }
    }
    
    @Test
    fun formatDate_returnsValidFormat() {
        val timestamp = System.currentTimeMillis()
        val formatted = formatDate(timestamp)
        
        val pattern = Regex("^[A-Za-z]{3} \\d{1,2}, \\d{4}$")
        assertTrue("Formatted date should match pattern MMM dd, yyyy", pattern.matches(formatted))
    }
}


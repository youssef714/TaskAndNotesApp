package com.example.taskandnotesapp.ui.tasks

import androidx.compose.ui.graphics.Color
import org.junit.Test
import org.junit.Assert.*

class TaskColorsTest {
    
    @Test
    fun taskColors_gradientsExist() {
        assertNotNull(TaskColors.PurpleGradient)
        assertNotNull(TaskColors.BlueGradient)
        assertNotNull(TaskColors.GreenGradient)
        assertNotNull(TaskColors.OrangeGradient)
        assertNotNull(TaskColors.RedGradient)
        assertNotNull(TaskColors.TealGradient)
    }
    
    @Test
    fun taskColors_gradientsHaveTwoColors() {
        assertEquals(2, TaskColors.PurpleGradient.size)
        assertEquals(2, TaskColors.BlueGradient.size)
        assertEquals(2, TaskColors.GreenGradient.size)
        assertEquals(2, TaskColors.OrangeGradient.size)
        assertEquals(2, TaskColors.RedGradient.size)
        assertEquals(2, TaskColors.TealGradient.size)
    }
    
    @Test
    fun taskColors_priorityColorsExist() {
        assertNotNull(TaskColors.HighPriority)
        assertNotNull(TaskColors.MediumPriority)
        assertNotNull(TaskColors.LowPriority)
    }
    
    @Test
    fun taskColors_purpleGradientValues() {
        val purpleGradient = TaskColors.PurpleGradient
        assertEquals(Color(0xFF6B4CE6), purpleGradient[0])
        assertEquals(Color(0xFF9D7FEA), purpleGradient[1])
    }
    
    @Test
    fun taskColors_priorityColorValues() {
        assertEquals(Color(0xFFFF6B6B), TaskColors.HighPriority)
        assertEquals(Color(0xFFFFB74D), TaskColors.MediumPriority)
        assertEquals(Color(0xFF4CAF50), TaskColors.LowPriority)
    }
}


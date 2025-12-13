package com.example.taskandnotesapp.ui.notes

import androidx.compose.ui.graphics.Color
import org.junit.Test
import org.junit.Assert.*

class NoteColorsTest {
    
    @Test
    fun noteColors_gradientsExist() {
        assertNotNull(NoteColors.PurpleGradient)
        assertNotNull(NoteColors.BlueGradient)
        assertNotNull(NoteColors.GreenGradient)
        assertNotNull(NoteColors.OrangeGradient)
        assertNotNull(NoteColors.PinkGradient)
        assertNotNull(NoteColors.TealGradient)
    }
    
    @Test
    fun noteColors_gradientsHaveTwoColors() {
        assertEquals(2, NoteColors.PurpleGradient.size)
        assertEquals(2, NoteColors.BlueGradient.size)
        assertEquals(2, NoteColors.GreenGradient.size)
        assertEquals(2, NoteColors.OrangeGradient.size)
        assertEquals(2, NoteColors.PinkGradient.size)
        assertEquals(2, NoteColors.TealGradient.size)
    }
    
    @Test
    fun noteColors_accentColorExists() {
        assertNotNull(NoteColors.AccentPurple)
    }
    
    @Test
    fun noteColors_purpleGradientValues() {
        val purpleGradient = NoteColors.PurpleGradient
        assertEquals(Color(0xFF6B4CE6), purpleGradient[0])
        assertEquals(Color(0xFF9D7FEA), purpleGradient[1])
    }
    
    @Test
    fun noteColors_accentPurpleValue() {
        assertEquals(Color(0xFF6B4CE6), NoteColors.AccentPurple)
    }
    
    @Test
    fun noteColors_pinkGradientValues() {
        val pinkGradient = NoteColors.PinkGradient
        assertEquals(Color(0xFFE91E63), pinkGradient[0])
        assertEquals(Color(0xFFF06292), pinkGradient[1])
    }
}


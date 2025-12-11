package com.example.taskandnotesapp.ui.notes

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

// Simple data class for Note
data class Note(
    val id: Int,
    val userId: Int,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
)

// Beautiful color schemes - same as Tasks
object NoteColors {
    val PurpleGradient = listOf(Color(0xFF6B4CE6), Color(0xFF9D7FEA))
    val BlueGradient = listOf(Color(0xFF2196F3), Color(0xFF64B5F6))
    val GreenGradient = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
    val OrangeGradient = listOf(Color(0xFFFF9800), Color(0xFFFFB74D))
    val PinkGradient = listOf(Color(0xFFE91E63), Color(0xFFF06292))
    val TealGradient = listOf(Color(0xFF009688), Color(0xFF4DB6AC))

    val AccentPurple = Color(0xFF6B4CE6)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    userId: Int,
    onNavigateBack: () -> Unit = {}
) {
    var notes by remember { mutableStateOf(listOf<Note>()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedNote by remember { mutableStateOf<Note?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var viewMode by remember { mutableStateOf("grid") } // grid or list

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8F9FE),
                        Color(0xFFEEF2FF)
                    )
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            if (!isSearchActive) {
                                Column {
                                    Text(
                                        "My Notes",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    )
                                    Text(
                                        "${notes.size} notes",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = onNavigateBack,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.9f))
                            ) {
                                Icon(Icons.Default.ArrowBack, "Back", tint = NoteColors.AccentPurple)
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = { isSearchActive = !isSearchActive },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.9f))
                            ) {
                                Icon(
                                    if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                                    "Search",
                                    tint = NoteColors.AccentPurple
                                )
                            }
                            IconButton(
                                onClick = { viewMode = if (viewMode == "grid") "list" else "grid" },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.9f))
                            ) {
                                Icon(
                                    if (viewMode == "grid") Icons.Default.List else Icons.Default.GridView,
                                    "View",
                                    tint = NoteColors.AccentPurple
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )

                    // Search Bar
                    AnimatedVisibility(
                        visible = isSearchActive,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            placeholder = { Text("Search notes...") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, "Search", tint = NoteColors.AccentPurple)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, "Clear")
                                    }
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NoteColors.AccentPurple,
                                unfocusedBorderColor = Color(0xFFE0E0E0),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            singleLine = true
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = NoteColors.AccentPurple,
                    contentColor = Color.White,
                    modifier = Modifier
                        .size(64.dp)
                        .scale(1f)
                ) {
                    Icon(
                        Icons.Default.Add,
                        "Add Note",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        ) { padding ->
            val filteredNotes = notes.filter { note ->
                if (searchQuery.isBlank()) {
                    true
                } else {
                    note.title.contains(searchQuery, ignoreCase = true) ||
                            note.content.contains(searchQuery, ignoreCase = true)
                }
            }

            if (filteredNotes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            NoteColors.AccentPurple.copy(alpha = 0.2f),
                                            NoteColors.AccentPurple.copy(alpha = 0.05f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                if (searchQuery.isNotEmpty()) Icons.Default.SearchOff else Icons.Default.Note,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = NoteColors.AccentPurple
                            )
                        }
                        Text(
                            if (searchQuery.isNotEmpty()) "No notes found" else "No notes yet",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3142)
                        )
                        Text(
                            if (searchQuery.isNotEmpty())
                                "Try a different search"
                            else
                                "Create your first note!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF8B8FA3)
                        )
                    }
                }
            } else {
                if (viewMode == "grid") {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalItemSpacing = 12.dp
                    ) {
                        items(filteredNotes, key = { it.id }) { note ->
                            val colorIndex = note.id % 6
                            val gradient = when (colorIndex) {
                                0 -> NoteColors.PurpleGradient
                                1 -> NoteColors.BlueGradient
                                2 -> NoteColors.GreenGradient
                                3 -> NoteColors.OrangeGradient
                                4 -> NoteColors.PinkGradient
                                else -> NoteColors.TealGradient
                            }

                            NoteCard(
                                note = note,
                                gradient = gradient,
                                onClick = { selectedNote = note },
                                onDelete = {
                                    notes = notes.filter { it.id != note.id }
                                }
                            )
                        }
                    }
                } else {
                    androidx.compose.foundation.lazy.LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredNotes.size) { index ->
                            val note = filteredNotes[index]
                            val colorIndex = note.id % 6
                            val gradient = when (colorIndex) {
                                0 -> NoteColors.PurpleGradient
                                1 -> NoteColors.BlueGradient
                                2 -> NoteColors.GreenGradient
                                3 -> NoteColors.OrangeGradient
                                4 -> NoteColors.PinkGradient
                                else -> NoteColors.TealGradient
                            }

                            NoteCard(
                                note = note,
                                gradient = gradient,
                                onClick = { selectedNote = note },
                                onDelete = {
                                    notes = notes.filter { it.id != note.id }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Add/Edit Note Dialog
    if (showAddDialog) {
        NoteDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, content ->
                val newNote = Note(
                    id = (notes.maxOfOrNull { it.id } ?: 0) + 1,
                    userId = userId,
                    title = title,
                    content = content,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                notes = notes + newNote
                showAddDialog = false
            }
        )
    }

    if (selectedNote != null) {
        NoteDialog(
            note = selectedNote,
            onDismiss = { selectedNote = null },
            onSave = { title, content ->
                notes = notes.map {
                    if (it.id == selectedNote?.id) {
                        it.copy(
                            title = title,
                            content = content,
                            updatedAt = System.currentTimeMillis()
                        )
                    } else it
                }
                selectedNote = null
            }
        )
    }
}

@Composable
fun NoteCard(
    note: Note,
    gradient: List<Color>,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            // Colorful top bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(Brush.horizontalGradient(gradient))
            )

            Column(
                modifier = Modifier.padding(16.dp).padding(top = 6.dp)
            ) {
                // Title
                if (note.title.isNotEmpty()) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF2D3142),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Content
                if (note.content.isNotEmpty()) {
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 14.sp,
                        color = Color(0xFF8B8FA3),
                        maxLines = 8,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Footer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Date
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF8F9FE))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = gradient[0]
                        )
                        Text(
                            text = formatDate(note.updatedAt),
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = gradient[0]
                        )
                    }

                    // Delete button
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color(0xFFFFEBEE))
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    "Delete Note?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "This action cannot be undone. Are you sure?",
                    color = Color(0xFF8B8FA3)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF4444)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", color = Color(0xFF2D3142))
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDialog(
    note: Note? = null,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (note == null) "✨ Create New Note" else "✏️ Edit Note",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title (optional)") },
                    placeholder = { Text("Note title...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NoteColors.AccentPurple,
                        focusedLabelColor = NoteColors.AccentPurple
                    )
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    placeholder = { Text("Write your note here...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NoteColors.AccentPurple,
                        focusedLabelColor = NoteColors.AccentPurple
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() || content.isNotBlank()) {
                        onSave(title, content)
                    }
                },
                enabled = title.isNotBlank() || content.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NoteColors.AccentPurple
                )
            ) {
                Text("Save Note", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel", color = Color(0xFF2D3142))
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
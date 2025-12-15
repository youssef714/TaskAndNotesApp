package com.example.taskandnotesapp.ui.notes

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.taskandnotesapp.data.AppDatabase
import com.example.taskandnotesapp.data.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    userId: String,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val noteDao = remember { AppDatabase.getInstance(context).noteDao() }
    val scope = rememberCoroutineScope()

    var notes by remember { mutableStateOf(listOf<Note>()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedNote by remember { mutableStateOf<Note?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var viewMode by remember { mutableStateOf("grid") }

    // Load notes from database
    LaunchedEffect(userId) {
        notes = withContext(Dispatchers.IO) {
            noteDao.getNotesForUser(userId).map { entity ->
                Note(
                    id = entity.id,
                    userId = entity.userId,
                    title = entity.title,
                    content = entity.content,
                    createdAt = entity.createdAt,
                    updatedAt = entity.updatedAt
                )
            }
        }
    }

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
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(Icons.Default.Add, "Add Note", modifier = Modifier.size(32.dp))
                }
            }
        ) { padding ->
            val filteredNotes = notes.filter { note ->
                if (searchQuery.isBlank()) true
                else note.title.contains(searchQuery, ignoreCase = true) ||
                        note.content.contains(searchQuery, ignoreCase = true)
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
                        Icon(
                            if (searchQuery.isNotEmpty()) Icons.Default.SearchOff else Icons.Default.Note,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = NoteColors.AccentPurple
                        )
                        Text(
                            if (searchQuery.isNotEmpty()) "No notes found" else "No notes yet",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
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
                                    scope.launch {
                                        withContext(Dispatchers.IO) {
                                            noteDao.deleteNote(
                                                NoteEntity(
                                                    id = note.id,
                                                    userId = note.userId,
                                                    title = note.title,
                                                    content = note.content,
                                                    createdAt = note.createdAt,
                                                    updatedAt = note.updatedAt
                                                )
                                            )
                                        }
                                        notes = notes.filter { it.id != note.id }
                                    }
                                }
                            )
                        }
                    }
                } else {
                    LazyColumn(
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
                                    scope.launch {
                                        withContext(Dispatchers.IO) {
                                            noteDao.deleteNote(
                                                NoteEntity(
                                                    id = note.id,
                                                    userId = note.userId,
                                                    title = note.title,
                                                    content = note.content,
                                                    createdAt = note.createdAt,
                                                    updatedAt = note.updatedAt
                                                )
                                            )
                                        }
                                        notes = notes.filter { it.id != note.id }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        NoteDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, content ->
                scope.launch {
                    val id = withContext(Dispatchers.IO) {
                        noteDao.insertNote(
                            NoteEntity(
                                userId = userId,
                                title = title,
                                content = content,
                                createdAt = System.currentTimeMillis(),
                                updatedAt = System.currentTimeMillis()
                            )
                        ).toInt()
                    }
                    val newNote = Note(
                        id = id,
                        userId = userId,
                        title = title,
                        content = content,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    notes = notes + newNote
                    showAddDialog = false
                }
            }
        )
    }

    if (selectedNote != null) {
        NoteDialog(
            note = selectedNote,
            onDismiss = { selectedNote = null },
            onSave = { title, content ->
                scope.launch {
                    withContext(Dispatchers.IO) {
                        noteDao.updateNote(
                            NoteEntity(
                                id = selectedNote!!.id,
                                userId = userId,
                                title = title,
                                content = content,
                                createdAt = selectedNote!!.createdAt,
                                updatedAt = System.currentTimeMillis()
                            )
                        )
                    }
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
            }
        )
    }
}
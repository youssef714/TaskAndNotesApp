package com.example.taskandnotesapp.ui.tasks

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    userId: Int,
    onNavigateBack: () -> Unit
) {
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    var filterOption by remember { mutableStateOf("All") }
    var showMenu by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

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
                                        "My Tasks",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    )
                                    Text(
                                        "${tasks.count { !it.isCompleted }} pending tasks",
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
                                Icon(Icons.Default.ArrowBack, "Back", tint = Color(0xFF6B4CE6))
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
                                    tint = Color(0xFF6B4CE6)
                                )
                            }
                            IconButton(
                                onClick = { showMenu = !showMenu },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.9f))
                            ) {
                                Icon(Icons.Default.MoreVert, "Menu", tint = Color(0xFF6B4CE6))
                            }
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("All Tasks") },
                                    onClick = {
                                        filterOption = "All"
                                        showMenu = false
                                    },
                                    leadingIcon = { Icon(Icons.Default.List, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Completed") },
                                    onClick = {
                                        filterOption = "Completed"
                                        showMenu = false
                                    },
                                    leadingIcon = { Icon(Icons.Default.CheckCircle, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("Pending") },
                                    onClick = {
                                        filterOption = "Pending"
                                        showMenu = false
                                    },
                                    leadingIcon = { Icon(Icons.Outlined.Circle, null) }
                                )
                                DropdownMenuItem(
                                    text = { Text("High Priority") },
                                    onClick = {
                                        filterOption = "High"
                                        showMenu = false
                                    },
                                    leadingIcon = { Icon(Icons.Default.Warning, null) }
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
                            placeholder = { Text("Search tasks by title...") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, "Search", tint = Color(0xFF6B4CE6))
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
                                focusedBorderColor = Color(0xFF6B4CE6),
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
                    containerColor = Color(0xFF6B4CE6),
                    contentColor = Color.White,
                    modifier = Modifier
                        .size(64.dp)
                        .scale(1f)
                ) {
                    Icon(
                        Icons.Default.Add,
                        "Add Task",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Enhanced Filter chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EnhancedFilterChip(
                        label = "All",
                        count = tasks.size,
                        isSelected = filterOption == "All",
                        onClick = { filterOption = "All" },
                        gradient = TaskColors.PurpleGradient
                    )
                    EnhancedFilterChip(
                        label = "Pending",
                        count = tasks.count { !it.isCompleted },
                        isSelected = filterOption == "Pending",
                        onClick = { filterOption = "Pending" },
                        gradient = TaskColors.OrangeGradient
                    )
                    EnhancedFilterChip(
                        label = "Done",
                        count = tasks.count { it.isCompleted },
                        isSelected = filterOption == "Completed",
                        onClick = { filterOption = "Completed" },
                        gradient = TaskColors.GreenGradient
                    )
                }

                // Tasks list with search filtering
                val filteredTasks = tasks
                    .filter { task ->
                        val matchesSearch = if (searchQuery.isBlank()) {
                            true
                        } else {
                            task.title.contains(searchQuery, ignoreCase = true)
                        }

                        val matchesFilter = when (filterOption) {
                            "Completed" -> task.isCompleted
                            "Pending" -> !task.isCompleted
                            "High" -> task.priority == "High"
                            else -> true
                        }

                        matchesSearch && matchesFilter
                    }

                if (filteredTasks.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
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
                                                Color(0xFF6B4CE6).copy(alpha = 0.2f),
                                                Color(0xFF6B4CE6).copy(alpha = 0.05f)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (searchQuery.isNotEmpty()) Icons.Default.SearchOff else Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = Color(0xFF6B4CE6)
                                )
                            }
                            Text(
                                if (searchQuery.isNotEmpty()) "No tasks found" else "No tasks yet",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2D3142)
                            )
                            Text(
                                if (searchQuery.isNotEmpty())
                                    "Try a different search term"
                                else
                                    "Create a new task to get started!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF8B8FA3)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredTasks, key = { it.id }) { task ->
                            EnhancedTaskItem(
                                task = task,
                                onToggleComplete = {
                                    tasks = tasks.map {
                                        if (it.id == task.id) it.copy(isCompleted = !it.isCompleted)
                                        else it
                                    }
                                },
                                onEdit = { selectedTask = task },
                                onDelete = {
                                    tasks = tasks.filter { it.id != task.id }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Add/Edit Task Dialog
    if (showAddDialog) {
        EnhancedTaskDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, description, priority, dueDate ->
                val newTask = Task(
                    id = (tasks.maxOfOrNull { it.id } ?: 0) + 1,
                    userId = userId,
                    title = title,
                    description = description,
                    isCompleted = false,
                    priority = priority,
                    dueDate = dueDate,
                    createdAt = System.currentTimeMillis()
                )
                tasks = tasks + newTask
                showAddDialog = false
            }
        )
    }

    if (selectedTask != null) {
        EnhancedTaskDialog(
            task = selectedTask,
            onDismiss = { selectedTask = null },
            onSave = { title, description, priority, dueDate ->
                tasks = tasks.map {
                    if (it.id == selectedTask?.id) {
                        it.copy(
                            title = title,
                            description = description,
                            priority = priority,
                            dueDate = dueDate
                        )
                    } else it
                }
                selectedTask = null
            }
        )
    }
}

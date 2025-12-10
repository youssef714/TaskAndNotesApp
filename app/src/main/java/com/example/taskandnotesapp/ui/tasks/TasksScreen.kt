
package com.example.taskandnotesapp.ui.tasks

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

// Simple data class for Task
data class Task(
    val id: Int,
    val userId: Int,
    val title: String,
    val description: String,
    val isCompleted: Boolean,
    val priority: String,
    val dueDate: Long?,
    val createdAt: Long
)

// Beautiful color schemes
object TaskColors {
    val PurpleGradient = listOf(Color(0xFF6B4CE6), Color(0xFF9D7FEA))
    val BlueGradient = listOf(Color(0xFF2196F3), Color(0xFF64B5F6))
    val GreenGradient = listOf(Color(0xFF4CAF50), Color(0xFF81C784))
    val OrangeGradient = listOf(Color(0xFFFF9800), Color(0xFFFFB74D))
    val RedGradient = listOf(Color(0xFFEF5350), Color(0xFFE57373))
    val TealGradient = listOf(Color(0xFF009688), Color(0xFF4DB6AC))

    val HighPriority = Color(0xFFFF6B6B)
    val MediumPriority = Color(0xFFFFB74D)
    val LowPriority = Color(0xFF4CAF50)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    userId: Int,
    onNavigateBack: () -> Unit = {}
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

@Composable
fun EnhancedFilterChip(
    label: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    gradient: List<Color>
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick),
        color = if (isSelected) gradient[0] else Color.White,
        shadowElevation = if (isSelected) 8.dp else 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isSelected) Color.White else Color(0xFF2D3142)
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isSelected) Color.White.copy(alpha = 0.3f) else gradient[0].copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = count.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else gradient[0]
                )
            }
        }
    }
}

@Composable
fun EnhancedTaskItem(
    task: Task,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    val priorityColor = when (task.priority) {
        "High" -> TaskColors.HighPriority
        "Medium" -> TaskColors.MediumPriority
        else -> TaskColors.LowPriority
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (task.isCompleted) 1.dp else 4.dp
        )
    ) {
        Box {
            // Priority indicator bar
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(80.dp)
                    .align(Alignment.CenterStart)
                    .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                priorityColor.copy(alpha = 0.8f),
                                priorityColor.copy(alpha = 0.4f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEdit() }
                    .padding(20.dp)
                    .padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Custom Checkbox
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .then(
                            if (task.isCompleted)
                                Modifier.background(Brush.linearGradient(TaskColors.GreenGradient))
                            else
                                Modifier.background(Color(0xFFE8EAED))
                        )
                        .clickable { onToggleComplete() },
                    contentAlignment = Alignment.Center
                ) {
                    if (task.isCompleted) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Task content
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (task.isCompleted) Color(0xFF4CAF50) else Color(0xFF2D3142),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (task.description.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 14.sp,
                            color = Color(0xFF8B8FA3),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Priority badge
                        EnhancedPriorityBadge(priority = task.priority)

                        // Due date
                        if (task.dueDate != null) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF8F9FE))
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFF6B4CE6)
                                )
                                Text(
                                    text = formatDate(task.dueDate),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF6B4CE6)
                                )
                            }
                        }
                    }
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
                        tint = TaskColors.HighPriority,
                        modifier = Modifier.size(20.dp)
                    )
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
                    tint = TaskColors.HighPriority,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    "Delete Task?",
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
                        containerColor = TaskColors.HighPriority
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

@Composable
fun EnhancedPriorityBadge(priority: String) {
    val (gradient, text, icon) = when (priority) {
        "High" -> Triple(listOf(Color(0xFFFF6B6B), Color(0xFFEE5A6F)), "High", Icons.Default.Warning)
        "Medium" -> Triple(listOf(Color(0xFFFFB74D), Color(0xFFFFA726)), "Medium", Icons.Default.Info)
        "Low" -> Triple(listOf(Color(0xFF4CAF50), Color(0xFF66BB6A)), "Low", Icons.Default.CheckCircle)
        else -> Triple(listOf(Color.Gray, Color.LightGray), priority, Icons.Default.Circle)
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Brush.horizontalGradient(gradient))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 12.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTaskDialog(
    task: Task? = null,
    onDismiss: () -> Unit,
    onSave: (String, String, String, Long?) -> Unit
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var priority by remember { mutableStateOf(task?.priority ?: "Medium") }
    var dueDate by remember { mutableStateOf(task?.dueDate) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (task == null) "✨ Create New Task" else "✏️ Edit Task",
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
                    label = { Text("Task Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6B4CE6),
                        focusedLabelColor = Color(0xFF6B4CE6)
                    )
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF6B4CE6),
                        focusedLabelColor = Color(0xFF6B4CE6)
                    )
                )

                // Priority selector
                Column {
                    Text(
                        "Priority Level",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3142)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PriorityOption("Low", priority == "Low", TaskColors.GreenGradient) { priority = "Low" }
                        PriorityOption("Medium", priority == "Medium", TaskColors.OrangeGradient) { priority = "Medium" }
                        PriorityOption("High", priority == "High", TaskColors.RedGradient) { priority = "High" }
                    }
                }

                // Due date
                OutlinedButton(
                    onClick = {
                        dueDate = System.currentTimeMillis() + 86400000L
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF6B4CE6)
                    )
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (dueDate != null) formatDate(dueDate!!) else "Set Due Date",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(title, description, priority, dueDate)
                    }
                },
                enabled = title.isNotBlank(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6B4CE6)
                )
            ) {
                Text("Save Task", fontWeight = FontWeight.Bold)
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

@Composable
fun RowScope.PriorityOption(
    label: String,
    isSelected: Boolean,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (isSelected)
                    Modifier.background(Brush.horizontalGradient(gradient))
                else
                    Modifier.background(Color(0xFFF8F9FE))
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = if (isSelected) Color.White else Color(0xFF2D3142)
        )
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
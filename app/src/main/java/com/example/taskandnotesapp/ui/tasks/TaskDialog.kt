package com.example.taskandnotesapp.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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


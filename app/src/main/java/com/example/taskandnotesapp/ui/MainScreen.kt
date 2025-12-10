package com.example.taskandnotesapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    userId: Int,
    onLogout: () -> Unit,
    onNavigateToTasks: () -> Unit,
    onNavigateToNotes: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Task & Notes App") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Logged in as User ID: $userId")
            Spacer(Modifier.height(24.dp))

            // ----------------- TASKS BUTTON -----------------
            Button(
                onClick = onNavigateToTasks,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Go to Tasks")
            }

            Spacer(Modifier.height(16.dp))

            // ----------------- NOTES BUTTON -----------------
            Button(
                onClick = onNavigateToNotes,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Go to Notes")
            }
        }
    }
}

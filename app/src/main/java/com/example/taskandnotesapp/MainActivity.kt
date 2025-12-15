package com.example.taskandnotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.example.taskandnotesapp.data.AuthRepository
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskandnotesapp.ui.MainScreen
import com.example.taskandnotesapp.ui.auth.LoginScreen
import com.example.taskandnotesapp.ui.auth.RegisterScreen
import com.example.taskandnotesapp.ui.tasks.TasksScreen
import com.example.taskandnotesapp.ui.notes.NotesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authRepository = AuthRepository()

        setContent {
            MaterialTheme {
                Surface {
                    TaskAndNotesApp(authRepository)
                }
            }
        }
    }
}

@Composable
fun TaskAndNotesApp(authRepository: AuthRepository) {
    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        authRepository = authRepository
    )
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    authRepository: AuthRepository
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                authRepository = authRepository,
                onLoginSuccess = { userId ->
                    navController.navigate("main/$userId") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                authRepository = authRepository,
                onRegisterSuccess = { userId ->
                    navController.navigate("main/$userId") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("main/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""

            var userName by remember { mutableStateOf("") }

            LaunchedEffect(userId) {
                val user = authRepository.getCurrentUser()
                userName = user?.displayName ?: user?.email?.substringBefore("@") ?: "User"
            }

            if (userName.isNotEmpty()) {
                MainScreen(
                    userId = userId,
                    userName = userName,
                    onLogout = {
                        authRepository.signOut()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigateToTasks = {
                        navController.navigate("tasks/$userId")
                    },
                    onNavigateToNotes = {
                        navController.navigate("notes/$userId")
                    }
                )
            }
        }

        composable("tasks/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            TasksScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("notes/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            NotesScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
package com.example.taskandnotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.taskandnotesapp.data.AppDatabase
import com.example.taskandnotesapp.data.UserDao
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

        val db = AppDatabase.getInstance(this)
        val userDao = db.userDao()

        setContent {
            MaterialTheme {
                Surface {
                    TaskAndNotesApp(userDao)
                }
            }
        }
    }
}

@Composable
fun TaskAndNotesApp(userDao: UserDao) {
    val navController = rememberNavController()

    AppNavHost(
        navController = navController,
        userDao = userDao
    )
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    userDao: UserDao
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // ---------------- LOGIN --------------------
        composable("login") {
            LoginScreen(
                userDao = userDao,
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

        // ---------------- REGISTER --------------------
        composable("register") {
            RegisterScreen(
                userDao = userDao,
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ---------------- MAIN MENU --------------------
        composable("main/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")!!.toInt()

            MainScreen(
                userId = userId,
                onLogout = {
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

        // ---------------- TASKS SCREEN --------------------
        composable("tasks/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")!!.toInt()
            TasksScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ---------------- NOTES SCREEN --------------------
        composable("notes/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")!!.toInt()
            NotesScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

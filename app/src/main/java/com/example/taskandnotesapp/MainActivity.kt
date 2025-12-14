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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

        composable("main/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")!!.toInt()

            var userName by remember { mutableStateOf("") }

            LaunchedEffect(userId) {
                val user = withContext(Dispatchers.IO) {
                    userDao.getUserById(userId)
                }
                userName = user?.username ?: "User"
            }

            if (userName.isNotEmpty()) {
                MainScreen(
                    userId = userId,
                    userName = userName,
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
        }

        composable("tasks/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")!!.toInt()
            TasksScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("notes/{userId}") { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")!!.toInt()
            NotesScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
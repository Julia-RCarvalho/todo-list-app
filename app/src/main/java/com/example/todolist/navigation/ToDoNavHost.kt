package com.example.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.todolist.ui.feature.addedit.AddEditScreen
import com.example.todolist.ui.feature.list.ListScreen
import com.example.todolist.ui.feature.splash.SplashScreen
import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

@Serializable
object ListRoute

@Serializable
data class AddEditRoute(val id: Long? = null)

@Composable
fun ToDoNavHost(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit
){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SplashRoute){
        composable<SplashRoute> {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(ListRoute) {
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<ListRoute> {
            ListScreen(
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                navigateToAddEditScreen = {id ->
                    navController.navigate(AddEditRoute(id = id))
                }
            )
        }

        composable<AddEditRoute> { backStackEntry ->
            val addEditRoute = backStackEntry.toRoute<AddEditRoute>()
            AddEditScreen(
                id = addEditRoute.id,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                navigateback = {
                    navController.popBackStack()
                }
            )
        }
    }
}

package com.example.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.todolist.ui.feature.addedit.AddEditScreen
import com.example.todolist.ui.feature.list.ListScreen
import com.example.todolist.ui.feature.splash.SplashGifScreen
import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

@Serializable
object ListRoute //Define a rota para a tela principal de listagem
// Como não precisa de parâmetros (argumentos), é definida como um object (instância única)

@Serializable
data class AddEditRoute(val id: Long? = null)

@Composable //indica que a função abaixo faz parte da interface de usuário do Jetpack Compose
fun ToDoNavHost(){
    val navController = rememberNavController() //cria e lembra o controlador da navegação
    NavHost(navController = navController, startDestination = SplashRoute){
        composable<SplashRoute> {
            SplashGifScreen(
                onSplashFinished = {
                    navController.navigate(ListRoute) {
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<ListRoute> {
            ListScreen(
                navigateToAddEditScreen = {id ->
                    navController.navigate(AddEditRoute(id = id))
                }
            )
        }

        composable<AddEditRoute> { backStackEntry ->
            val addEditRoute = backStackEntry.toRoute<AddEditRoute>()
            AddEditScreen(
                id = addEditRoute.id,
                navigateback = {
                    navController.popBackStack()
                }
            )

        }

    }
}


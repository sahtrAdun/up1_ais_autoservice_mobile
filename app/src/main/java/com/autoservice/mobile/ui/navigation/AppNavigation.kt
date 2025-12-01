package com.autoservice.mobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.autoservice.mobile.ui.screen.auth.AuthState
import com.autoservice.mobile.ui.screen.auth.LoginScreen
import com.autoservice.mobile.ui.screen.cars.CarsScreen
import com.autoservice.mobile.ui.screen.main.MainScreen
import com.autoservice.mobile.ui.screen.newrequest.NewRequestScreen
import com.autoservice.mobile.ui.screen.requests.RequestsScreen
import com.autoservice.mobile.ui.viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

//import com.autoservice.mobile.ui.screen.requests.NewRequestScreen
//import com.autoservice.mobile.ui.screen.requests.RequestsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screen.Main.route) },
                viewModel = authViewModel
            )
        }
        composable(Screen.Main.route) {
            MainScreen(
                onCarsClick = { navController.navigate(Screen.Cars.route) },
                onRequestsClick = { navController.navigate(Screen.Requests.route) },
                onNewRequestClick = { navController.navigate(Screen.NewRequest.route) },
                onLogout = {
                    authViewModel.logout()
                    navController.popBackStack(Screen.Login.route, inclusive = false)
                }
            )
        }
        composable(Screen.Cars.route) {
           CarsScreen(
                onBackClick = { navController.popBackStack() },
            )
        }
        composable(Screen.Requests.route) {
            RequestsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.NewRequest.route) {
            NewRequestScreen(
                onBackClick = { navController.popBackStack() },
                onRequestCreated = { navController.popBackStack() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Main : Screen("main")
    object Cars : Screen("cars")
    object Requests : Screen("requests")
    object NewRequest : Screen("new_request")
}

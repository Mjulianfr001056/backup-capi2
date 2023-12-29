package com.polstat.pkl.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.polstat.pkl.ui.screen.ErrorScreen
import com.polstat.pkl.ui.screen.IsiRumahTanggaScreen
import com.polstat.pkl.ui.screen.LoginScreen
import com.polstat.pkl.ui.screen.SuccessScreen
import com.polstat.pkl.ui.viewmodel.LoginViewModel

@Composable
fun AppNavHost(navController: NavHostController, loginViewModel: LoginViewModel) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, loginViewModel) }
        composable("success") { SuccessScreen(loginViewModel) }
        composable("isiruta") { IsiRumahTanggaScreen() }
        composable("error/{errorMessage}", arguments = listOf(navArgument("errorMessage") { type = NavType.StringType })) { backStackEntry ->
            ErrorScreen(backStackEntry.arguments?.getString("errorMessage") ?: "")
        }
    }
}

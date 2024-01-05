package com.polstat.pkl.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.polstat.pkl.ui.screen.BerandaScreen
import com.polstat.pkl.ui.screen.IsiRumahTanggaScreen
import com.polstat.pkl.ui.screen.ListBSScreen
import com.polstat.pkl.ui.screen.ListRutaScreen
import com.polstat.pkl.ui.screen.ListSampleScreen
import com.polstat.pkl.ui.screen.LoginScreen
import com.polstat.pkl.ui.screen.PasswordMasterScreen
import com.polstat.pkl.ui.screen.SamplingScreen
import com.polstat.pkl.ui.viewmodel.LoginViewModel
import com.polstat.pkl.ui.viewmodel.PasswordMasterViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    val passwordMasterViewModel: PasswordMasterViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Capi63Screen.Login.route
    ) {
        composable(Capi63Screen.Login.route) {
            LoginScreen(
                navController = navController,
                loginViewModel = loginViewModel
            )
        }
        composable(Capi63Screen.Beranda.route) {
            BerandaScreen(
                navController = navController
            )
        }
        composable(Capi63Screen.Sampling.route) {
            SamplingScreen(
                navController = navController,
                isPml = true
            )
        }
        composable(Capi63Screen.PasswordMaster.route){
            PasswordMasterScreen(
                navController = navController,
                passwordMasterViewModel = passwordMasterViewModel
            )
        }
        composable(Capi63Screen.ListBs.route) {
            ListBSScreen(
                navController = navController
            )
        }
        composable(Capi63Screen.ListRuta.route) {
            ListRutaScreen(
                navController = navController
            )
        }
        composable(Capi63Screen.ListSample.route){
            ListSampleScreen(
                navController = navController
            )
        }
        composable(Capi63Screen.IsiRuta.route) {
            IsiRumahTanggaScreen(
                navController = navController
            )
        }
    }
}

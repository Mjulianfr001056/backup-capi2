package org.odk.collect.pkl.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.polstat.pkl.viewmodel.AuthViewModel
import com.polstat.pkl.viewmodel.PasswordMasterViewModel
import org.odk.collect.pkl.ProjectConfigurerActivity
import org.odk.collect.pkl.ui.screen.BerandaScreen
import org.odk.collect.pkl.ui.screen.IsiRumahTanggaScreen
import org.odk.collect.pkl.ui.screen.ListBSScreen
import org.odk.collect.pkl.ui.screen.ListRutaScreen
import org.odk.collect.pkl.ui.screen.ListSampleScreen
import org.odk.collect.pkl.ui.screen.LoginScreen
import org.odk.collect.pkl.ui.screen.OnBoardingScreen
import org.odk.collect.pkl.ui.screen.PasswordMasterScreen
import org.odk.collect.pkl.ui.screen.SamplingScreen

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = CapiScreen.Top.AUTH
    ) {
        navigation(
            startDestination = CapiScreen.Auth.ONBOARDING,
            route = CapiScreen.Top.AUTH
        ){
            composable(CapiScreen.Auth.ONBOARDING){
                OnBoardingScreen(
                    navController = navController
                )
            }
            composable(CapiScreen.Auth.LOGIN) {
                LoginScreen(
                    navController = navController,
                    viewModel = authViewModel
                )
            }
        }

        navigation(
            startDestination = CapiScreen.Main.BERANDA,
            route = CapiScreen.Top.MAIN
        ){
            composable(CapiScreen.Main.BERANDA) {
                BerandaNavHost(navController)
            }
            composable(CapiScreen.Main.SAMPLING) {
                SamplingNavHost(navController)
            }
            composable(CapiScreen.Main.KUESIONER) {
                KuesionerNavHost(navController)
            }
        }
    }
}

@Composable
fun SamplingNavHost(
    rootController : NavHostController
){
    val samplingNavController = rememberNavController()
    val passwordMasterViewModel: PasswordMasterViewModel = viewModel()

    NavHost(
        navController = samplingNavController,
        startDestination = CapiScreen.Sampling.START
    ){
        composable(CapiScreen.Sampling.START) {
            SamplingScreen(
                rootController = rootController,
                navController = samplingNavController,
                viewModel = hiltViewModel()
            )
        }

        navigation(
            startDestination = CapiScreen.Listing.LIST_BS,
            route = CapiScreen.Sampling.LISTING
        ){
            composable(CapiScreen.Listing.LIST_BS) {
                ListBSScreen(
                    navController = samplingNavController,
                    viewModel = hiltViewModel()
                )
            }
            composable(
                route = CapiScreen.Listing.LIST_RUTA + "/{noBS}",
                arguments = listOf(
                    navArgument("noBS") {
                        type = NavType.StringType
                    }
                )
            ) {
                ListRutaScreen(
                    navController = samplingNavController,
                    viewModel = hiltViewModel()
                )
            }
            composable(
                route = CapiScreen.Listing.LIST_SAMPLE + "/{noBS}",
                arguments = listOf(
                    navArgument("noBS") {
                        type = NavType.StringType
                    }
                )
            ){
                ListSampleScreen(
                    navController = samplingNavController,
                    viewModel = hiltViewModel()
                )
            }
            composable(
                route = CapiScreen.Listing.ISI_RUTA + "/{noBS}",
                arguments = listOf(
                    navArgument("noBS") {
                        type = NavType.StringType
                    }
                )
            ) {
                IsiRumahTanggaScreen(
                    navController = samplingNavController,
                    viewModel = hiltViewModel()
                )
            }
        }

        navigation(
            startDestination = CapiScreen.Password.PASSWORD_MASTER,
            route = CapiScreen.Sampling.PASSWORD
        ){
            composable(CapiScreen.Password.PASSWORD_MASTER) {
                PasswordMasterScreen(
                    navController = samplingNavController,
                    passwordMasterViewModel = passwordMasterViewModel
                )
            }
        }
    }
}

@Composable
fun BerandaNavHost(
    rootController : NavHostController
){
    val berandaNavController = rememberNavController()

    NavHost(
        navController = berandaNavController,
        startDestination = CapiScreen.Beranda.START
    ){
        composable(CapiScreen.Beranda.START){
            BerandaScreen(
                rootController = rootController,
                navController = berandaNavController,
                viewModel = hiltViewModel()
            )
        }
    }
}

@Composable
fun KuesionerNavHost(
    rootController : NavHostController
){
    val kuesionerNavController = rememberNavController()

    NavHost(
        navController = kuesionerNavController,
        startDestination = CapiScreen.Kuesioner.START
    ){
        composable(CapiScreen.Kuesioner.START){
            val context = LocalContext.current
            val intent = Intent(context, ProjectConfigurerActivity::class.java)
            context.startActivity(intent)
        }
    }
}
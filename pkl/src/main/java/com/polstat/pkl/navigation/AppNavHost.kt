package com.polstat.pkl.navigation

//import com.polstat.pkl.viewmodel.LocationViewModel
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.polstat.pkl.ui.screen.BerandaScreen
import com.polstat.pkl.ui.screen.IsiRumahTanggaScreen
import com.polstat.pkl.ui.screen.ListBSScreen
import com.polstat.pkl.ui.screen.ListRutaScreen
import com.polstat.pkl.ui.screen.ListSampleScreen
import com.polstat.pkl.ui.screen.LoginScreen
import com.polstat.pkl.ui.screen.OnBoardingScreen
import com.polstat.pkl.ui.screen.PasswordMasterScreen
import com.polstat.pkl.ui.screen.SamplingScreen
import com.polstat.pkl.viewmodel.AuthViewModel
import com.polstat.pkl.viewmodel.PasswordMasterViewModel

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    val authViewModel: AuthViewModel = hiltViewModel()
//    val locationViewModel: LocationViewModel = hiltViewModel()

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
    val authViewModel = hiltViewModel<AuthViewModel>()
//    val locationViewModel = hiltViewModel<LocationViewModel>()

    NavHost(
        navController = samplingNavController,
        startDestination = CapiScreen.Sampling.START
    ){
        composable(CapiScreen.Sampling.START) {
            SamplingScreen(
                rootController = rootController,
                navController = samplingNavController,
                isPml = true
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
            composable(CapiScreen.Listing.LIST_RUTA) {
                ListRutaScreen(
                    navController = samplingNavController,
                    listRutaViewModel = hiltViewModel(),
                    authViewModel = authViewModel
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
            composable(CapiScreen.Listing.ISI_RUTA) {
                IsiRumahTanggaScreen(
                    navController = samplingNavController
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

        }
    }
}

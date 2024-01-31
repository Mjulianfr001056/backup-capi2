package org.odk.collect.pkl.navigation

import EditRutaScreen
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.polstat.pkl.viewmodel.AuthViewModel
import com.polstat.pkl.viewmodel.KuesionerViewModel
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
import org.odk.collect.pkl.ui.screen.SalinRutaScreen
import org.odk.collect.pkl.ui.screen.SamplingScreen

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = CapiScreen.Top.AUTH
    ) {
        navigation(
            startDestination = CapiScreen.Auth.ONBOARDING,
            route = CapiScreen.Top.AUTH
        ){
            composable(CapiScreen.Auth.ONBOARDING){
                val viewModel = it.SharedViewModel<AuthViewModel>(navController = navController)
                OnBoardingScreen(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(CapiScreen.Auth.LOGIN) {
                val viewModel = it.SharedViewModel<AuthViewModel>(navController = navController)
                LoginScreen(
                    navController = navController,
                    viewModel = viewModel
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
                KuesionerNavHost()
                //KuesionerNavHost(navController)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
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
                    viewModel = hiltViewModel(),
                    authViewModel = hiltViewModel()
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
                    viewModel = hiltViewModel(),
                    authViewModel = hiltViewModel()
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
            composable(
                route = CapiScreen.Listing.EDIT_RUTA + "/{noBS}/{kodeKlg}/{kodeRuta}",
                arguments = listOf(
                    navArgument("noBS") {
                        type = NavType.StringType
                    },
                    navArgument("kodeKlg") {
                        type = NavType.StringType
                    },
                    navArgument("kodeRuta") {
                        type = NavType.StringType
                    }
                )
            ) {
                EditRutaScreen(
                    navController = samplingNavController,
                    viewModel = hiltViewModel()
                )
            }

            composable(
                route = CapiScreen.Listing.SALIN_RUTA + "/{noBS}/{kodeRuta}",
                arguments = listOf(
                    navArgument("noBS") {
                        type = NavType.StringType
                    },
                    navArgument("kodeRuta") {
                        type = NavType.StringType
                    }
                )
            ) {
                SalinRutaScreen(
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
    //rootController : NavHostController
){
    val kuesionerNavController = rememberNavController()
    val kuesionerViewModel: KuesionerViewModel = hiltViewModel()

    val token = kuesionerViewModel.getToken()

    NavHost(
        navController = kuesionerNavController,
        startDestination = CapiScreen.Kuesioner.START
    ){
        composable(CapiScreen.Kuesioner.START){
            val context = LocalContext.current
            val intent = Intent(context, ProjectConfigurerActivity::class.java)
            intent.putExtra("token", token)
            context.startActivity(intent)
        }
    }
}

@Composable
inline fun  <reified T : ViewModel> NavBackStackEntry.SharedViewModel(navController : NavController) : T{
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this){
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}


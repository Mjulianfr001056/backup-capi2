package org.odk.collect.pkl.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.R
import org.odk.collect.pkl.ui.screen.components.BottomNavBar
import org.odk.collect.pkl.ui.screen.components.ListPplCard
import org.odk.collect.pkl.ui.screen.components.PmlCard
import org.odk.collect.pkl.ui.screen.components.ProfileCard
import org.odk.collect.pkl.ui.screen.components.StatusListingCard
import org.odk.collect.pkl.ui.screen.components.WilayahKerjaCard
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.viewmodel.BerandaViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.odk.collect.pkl.navigation.CapiScreen
import org.odk.collect.pkl.ui.screen.components.ProgresCacahCard
import timber.log.Timber

@Preview
@Composable
fun BerandaScreenPreview() {
    Capi63Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            val rootController = rememberNavController()
            org.odk.collect.pkl.ui.screen.BerandaScreen(
                rootController = rootController,
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(
    rootController: NavHostController,
    navController: NavHostController,
    viewModel: BerandaViewModel
) {
    var showMenu by remember {
        mutableStateOf(false)
    }

    val session = viewModel.session

    val listWilayah = viewModel.listWilayah
    
    val listAllSampelRuta = viewModel.listAllSampelRuta.collectAsState()

    val listAnggotaTim = viewModel.listAnggotaTim

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "BERANDA",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PklPrimary900,
                    titleContentColor = Color.White,
                ),
                actions = {
                    IconButton(
                        onClick = {
                            showMenu = !showMenu
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White,
                            modifier = Modifier.size(25.dp)
                        )
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = {
                                showMenu = false
                            },
                            modifier = Modifier.background(Color.White)
                        ) {
//                            DropdownMenuItem(
//                                text = {
//                                    Text(
//                                        text = "Panduan Bagi PPL",
//                                        color = Color.Gray
//                                    )
//                                },
//                                onClick = { /*TODO*/ }
//                            )
//                            DropdownMenuItem(
//                                text = {
//                                    Text(
//                                        text = "Pengaturan",
//                                        color = Color.Gray
//                                    )
//                                },
//                                onClick = { /*TODO*/ }
//                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Keluar",
                                        color = Color.Gray
                                    )
                                },
                                onClick = {
                                    coroutineScope.launch {
                                        val job = async { viewModel.logout() }
                                        job.await()
                                        Timber.tag("BerandaScreen").d("BerandaScreen: Berhasil logut dari beranda!")
                                        val job2 = async { viewModel.deleteAllLocalData() }
                                        job2.await()
                                        Timber.tag("BerandaScreen").d("BerandaScreen: Berhasil menghapus semua data lokal!")
                                        rootController.navigate(CapiScreen.Top.AUTH) {
                                            popUpTo(CapiScreen.Top.MAIN) {
                                                inclusive = true
                                            }
                                        }
//                                        delay(1000)
//                                        System.exit(0)
                                    }
                                    Toast.makeText(context, "Logout berhasil!", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(rootNavController = rootController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.pb_bg_background),
                    contentScale = ContentScale.Crop
                )
//                .background(PklBase)
                .verticalScroll(rememberScrollState())
        ) {

            session?.let {
                ProfileCard(
                    session = it
                )
            }

            WilayahKerjaCard(
                listWilayah = listWilayah.value,
                listAllSampelRuta = listAllSampelRuta.value
            )

            if (session?.isKoor == true) {
                ListPplCard(listAnggotaTim = listAnggotaTim.value, context)
                StatusListingCard(listWilayah = listWilayah.value)
            } else {
                session?.let { PmlCard(session = it, context) }
                ProgresCacahCard(listWilayah = listWilayah.value, listAllSampelRuta = listAllSampelRuta.value)
            }
        }
    }
}
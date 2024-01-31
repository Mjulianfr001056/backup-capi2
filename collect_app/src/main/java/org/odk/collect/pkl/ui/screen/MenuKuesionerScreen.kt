package org.odk.collect.pkl.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.R
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import org.odk.collect.pkl.ui.screen.components.BottomNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuKuesionerScreen(
    rootController: NavHostController,
    navController: NavHostController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "KUESIONER",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PklPrimary900,
                    titleContentColor = Color.White,
                ),
                actions = {}
            )
        },
        bottomBar = {
            BottomNavBar(rootNavController = rootController)
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(color = PklBase),
                contentAlignment = Alignment.Center
            ) {
                HorizontalMenuKuesioner(
                    navController = navController,
                )
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HorizontalMenuKuesioner(navController: NavHostController) {
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        FlowRow(modifier = Modifier.padding(4.dp)) {
            MenuButton(
                image = painterResource(R.drawable.isi_kuesioner),
                name = "Isi Kuesioner",
                onCardClicked = {
                    navController.navigate("")
                }
            )
            Spacer(modifier = Modifier.width(30.dp))
            MenuButton(
                image = painterResource(R.drawable.ubah_kuesioner),
                name = "Ubah Kuesioner",
                onCardClicked = {
                    navController.navigate("")
                }
            )
        }
        FlowRow(modifier = Modifier.padding(4.dp)) {
            MenuButton(
                image = painterResource(R.drawable.kirim_kuesioner),
                name = "Kirim Kuesioner",
                onCardClicked = {
                    navController.navigate("list_bs")
                }
            )
            Spacer(modifier = Modifier.width(30.dp))
            MenuButton(
                image = painterResource(R.drawable.unduh_kuesioner),
                name = "Unduh Kuesioner",
                onCardClicked = {
                    navController.navigate("password")
                }
            )
        }
        FlowRow(modifier = Modifier.padding(4.dp)) {
            MenuButton(
                image = painterResource(R.drawable.hapus_kuesioner),
                name = "Hapus Kuesioner",
                onCardClicked = {
                    navController.navigate("list_bs")
                }
            )
            Spacer(modifier = Modifier.width(30.dp))
            MenuButton(
                image = painterResource(R.drawable.pemberitahuan),
                name = "Pemberitahuan",
                onCardClicked = {
                    navController.navigate("password")
                }
            )
        }
        FlowRow(modifier = Modifier.padding(4.dp)) {
            MenuButton(
                image = painterResource(R.drawable.arsip),
                name = "Arsip",
                onCardClicked = {
                    navController.navigate("list_bs")
                }
            )
        }
    }
}

@Preview
@Composable
fun MenuKuesionerScreenPreview () {
    Capi63Theme {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            MenuKuesionerScreen(navController, navController)
        }
    }
}
package org.odk.collect.pkl.ui.screen

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.ui.screen.components.BottomNavBar
import com.polstat.pkl.ui.screen.components.ListPplCard
import com.polstat.pkl.ui.screen.components.PmlCard
import com.polstat.pkl.ui.screen.components.ProfileCard
import com.polstat.pkl.ui.screen.components.ProgresListingCard
import com.polstat.pkl.ui.screen.components.StatusListingCard
import com.polstat.pkl.ui.screen.components.WilayahKerjaCard
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.viewmodel.BerandaViewModel

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
            BerandaScreen(
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

    val dataTimWithAll = viewModel.dataTimWithAll.collectAsState()

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
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Panduan Bagi PPL",
                                        color = Color.Gray
                                    )
                                },
                                onClick = { /*TODO*/ }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Pengaturan",
                                        color = Color.Gray
                                    )
                                },
                                onClick = { /*TODO*/ }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Keluar",
                                        color = Color.Gray
                                    )
                                },
                                onClick = { /*TODO*/ }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(rootNavController = rootController)
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(PklBase)
                .verticalScroll(rememberScrollState())
        ) {

            ProfileCard(
                session = session!!,
                dataTim = dataTimWithAll.value.dataTimWithMahasiswa!!.dataTim
            )

            if (dataTimWithAll.value.listMahasiswaWithAll!!.isNotEmpty()) {
                dataTimWithAll.value.listMahasiswaWithAll!!.forEach{ mahasiswaWithAll ->
                    if (mahasiswaWithAll.mahasiswaWithWilayah!!.mahasiswa!!.nim == session.nim) {
                        WilayahKerjaCard(
                            listWilayah = mahasiswaWithAll.mahasiswaWithWilayah!!.listWilayah!!
                        )
                    }
                }
            }

            if (session.isKoor!!) {
                ListPplCard(dataTimWithAll = dataTimWithAll.value)
                StatusListingCard(dataTimWithAll = dataTimWithAll.value)
            } else {
                PmlCard(dataTim = dataTimWithAll.value.dataTimWithMahasiswa!!.dataTim)

                if (dataTimWithAll.value.listMahasiswaWithAll!!.isNotEmpty()) {
                    dataTimWithAll.value.listMahasiswaWithAll!!.forEach{ mahasiswaWithAll ->
                        if (mahasiswaWithAll.mahasiswaWithWilayah!!.mahasiswa!!.nim == session.nim) {
                            ProgresListingCard(mahasiswaWithAll = mahasiswaWithAll)
                        }
                    }
                }
            }
        }
    }
}
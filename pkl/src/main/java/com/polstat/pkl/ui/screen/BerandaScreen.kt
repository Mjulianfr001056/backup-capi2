package com.polstat.pkl.ui.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.ui.screen.components.BottomBar
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.viewmodel.AuthViewModel

@Preview
@Composable
fun BerandaScreenPreview() {
    Capi63Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            BerandaScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    var showMenu by remember {
        mutableStateOf(false)
    }

    val user = viewModel.getUserFromSession()

    val dataTim = viewModel.getDataTimFromSession()

    val wilayah = viewModel.getWilayahFromSession()

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
            BottomBar(navController = navController)
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(PklBase)
                .verticalScroll(rememberScrollState())
        ) {
            // First Card
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .shadow(8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.Gray,
                ),
                border = BorderStroke(1.dp, Color.White),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        tint = PklPrimary900,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(80.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "${user?.nama}",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "${user?.nim}",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "   |   ",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = if (user?.isKoor!!) "PCL" else "PPL",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "${dataTim.namaTim}",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = " - ",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${dataTim.idTim}",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                        Log.d("Beranda", dataTim.anggota.size.toString())
                    }
                }
            }

            // Second Card - Add another Card element here
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .shadow(8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = PklPrimary900,
                    contentColor = Color.Gray,
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Column{
                    Text(
                        text = "PML",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .padding(12.dp)
                    ){
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Nama PML",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "NIM PML",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Second Card - Add another Card element here
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .shadow(8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = PklPrimary900,
                    contentColor = Color.Gray,
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    //modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "WILAYAH KERJA",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .padding(12.dp)
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column {
                                Text(
                                    text = "1",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 30.sp,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = "Kelurahan",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Divider(
                                color = Color.LightGray,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(2.dp)
                            )
                            Column {
                                Text(
                                    text = "0",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 30.sp,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = "Titik Sampel",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .shadow(8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = PklPrimary900,
                    contentColor = Color.Gray,
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    //modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "STATUS LISTING",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .padding(12.dp)
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column{
                                Text(
                                    text = "333E",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "KEC. Bantur",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "KEC. Bandungrejo",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Column(
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = "Listing",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
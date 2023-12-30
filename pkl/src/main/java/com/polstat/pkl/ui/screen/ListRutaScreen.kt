package com.polstat.pkl.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.navigation.Capi63Screen
import com.polstat.pkl.ui.theme.*
import com.polstat.pkl.ui.theme.PklPrimary300
import com.polstat.pkl.ui.theme.PklPrimary700

@Preview
@Composable
fun ListRutaPreview() {
    Capi63Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            ListRutaScreen(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListRutaScreen(navController: NavHostController) {
    var showMenu by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                {
                    Text(
                        text = "List Ruta",
                        fontFamily = PoppinsFontFamily
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = PklPrimary700,
                    titleContentColor = Color.White,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Capi63Screen.ListBs.route){
                                popUpTo(Capi63Screen.ListBs.route){
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back Button",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search Button",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Reload Button",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            contentDescription = "MoreVert Button",
                            tint = Color.White
                        )
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(text = { Text(text = "Menu 1") }, onClick = { /*TODO*/ })
                        DropdownMenuItem(text = { Text(text = "Menu 2") }, onClick = { /*TODO*/ })
                        DropdownMenuItem(text = { Text(text = "Menu 3") }, onClick = { /*TODO*/ })
                    }
                },
            )
        },
        content = { innerPadding ->
            Column {
                Row(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(PklPrimary300),
                    Arrangement.SpaceAround,
                    Alignment.CenterVertically,
                ) {
//                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "No",
                        color = Color.White,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
//                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "No BF",
                        color = Color.White,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
//                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "No BS",
                        color = Color.White,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
//                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "No Ruta", color = Color.White,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
//                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Nama KRT",
                        color = Color.White,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
//                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info Button",
                        Modifier.alpha(0f)
                    )
                }
                ScrollContent()
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(all = 16.dp),
                onClick = {
                          navController.navigate("isi_ruta")
                },
                containerColor = PklPrimary300
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Floating Action Button",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun ScrollContent() {
    val range = 1..50
    var openDialog by remember { mutableStateOf(false) }

    LazyColumn {
        items(range.count()) { index ->
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                Arrangement.Center
            ) {
//                    Spacer(modifier = Modifier.width(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    Arrangement.SpaceAround,
                    Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}",
                        fontFamily = PoppinsFontFamily
                    )
                    Text(
                        text = "No BF",
                        fontFamily = PoppinsFontFamily
                    )
                    Text(
                        text = "No BS",
                        fontFamily = PoppinsFontFamily
                    )
                    Text(
                        text = "No Ruta",
                        fontFamily = PoppinsFontFamily
                    )
                    Text(
                        text = "Nama KRT",
                        fontFamily = PoppinsFontFamily
                    )
                    IconButton(onClick = {
                        openDialog = true
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Info Button",
                        )
                    }

                    if (openDialog) {
                        Dialog(onDismissRequest = { openDialog = false }) {
                            Surface {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = "Aksi Untuk ART",
                                        fontFamily = PoppinsFontFamily,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Divider()
//                                    Text(
//                                        text = "You have cleared all the stages",
//                                        style = MaterialTheme.typography.titleMedium
//                                    )
//                                    Button(onClick = { openDialog = false }) {
//                                        Text(text = "Play")
//                                    }
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(30.dp)
                                            .clickable { },
                                        textAlign = TextAlign.Center,
                                        text = "Ubah",
                                        fontSize = 16.sp,
                                        fontFamily = PoppinsFontFamily
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(30.dp)
                                            .clickable { },
                                        textAlign = TextAlign.Center,
                                        text = "Salin isian ruta",
                                        fontSize = 16.sp,
                                        fontFamily = PoppinsFontFamily
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(30.dp)
                                            .clickable { },
                                        textAlign = TextAlign.Center,
                                        text = "Hapus",
                                        fontSize = 16.sp,
                                        fontFamily = PoppinsFontFamily
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}






package com.polstat.pkl.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.R
import com.polstat.pkl.navigation.Capi63Screen
import com.polstat.pkl.ui.theme.*
import com.polstat.pkl.ui.theme.PklPrimary300

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
                title = {
                    Text(
                        text = stringResource(id = R.string.list_ruta_title).uppercase(),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PklPrimary900,
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
                            imageVector = Icons.Filled.Sync,
                            contentDescription = "Reload Button",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "MoreVert Button",
                            tint = Color.White
                        )
                    }
                    DropdownMenu(
                        modifier = Modifier.background(Color.White),
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        offset = DpOffset(
                            x = 10.dp,
                            y = (-3).dp
                        )
                    ) {
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
                        .height(50.dp)
                        .background(PklPrimary300),
                    Arrangement.SpaceEvenly,
                    Alignment.CenterVertically,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 10.dp),
                        Arrangement.SpaceEvenly,
                        Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(id = R.string.no_list_ruta),
                            color = Color.White,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Text(
                            text = stringResource(id = R.string.no_bf_list_ruta),
                            color = Color.White,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Text(
                            text = stringResource(id = R.string.no_bs_list_ruta),
                            color = Color.White,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Text(
                            text = stringResource(id = R.string.no_ruta_list_ruta),
                            color = Color.White,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Text(
                            text = stringResource(id = R.string.nama_krt_list_ruta),
                            color = Color.White,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Info",
                            color = Color.White,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )

                    }

                }
//                ScrollContent()
                RutaRow()
                RutaRow()
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(all = 16.dp),
                onClick = {
                          navController.navigate("isi_ruta")
                },
                containerColor = PklPrimary900
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

@OptIn(ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@Preview(showBackground = true)
@Composable
fun RutaRow(
//    onClick: () -> Unit,
//    onEnableChange: (Boolean) -> Unit
) {
    var openDialog by remember { mutableStateOf(false) }
    var openDetail by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .combinedClickable(onLongClick = {
                openDialog = true
            },
                onClick = { }),
        Arrangement.SpaceEvenly,
        Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.no_list_ruta),
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(
            text = stringResource(id = R.string.no_bf_list_ruta),
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(
            text = stringResource(id = R.string.no_bs_list_ruta),
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(
            text = stringResource(id = R.string.no_ruta_list_ruta),
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(
            text = stringResource(id = R.string.nama_krt_list_ruta),
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        IconButton(onClick = {
            openDetail = true
        }) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Info Button",
            )
        }

        if (openDetail) {
            Dialog(
                onDismissRequest = { openDetail = false },
                content = {
                    Column(
                        Modifier
                            .background(Color.White)
                            .padding(10.dp), Arrangement.Center, Alignment.CenterHorizontally) {
                        Text(
                            text = "DETAIL RUTA",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .weight(1f)
                        ) {
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                            Text(text = "DETAIL 1",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp)
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(bottom = 10.dp),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(Color.Transparent)
                            )
                        }
                        TextButton(onClick = { openDetail = false }) {
                            Text("Close")
                        }
                    }
                })
        }

        if (openDialog) {
            Dialog(onDismissRequest = { openDialog = false },
                content = {
                    Surface {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                                text = "Aksi Untuk ART",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Divider()
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clickable { },
                                textAlign = TextAlign.Center,
                                text = "Ubah",
                                fontSize = 16.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clickable { },
                                textAlign = TextAlign.Center,
                                text = "Salin isian ruta",
                                fontSize = 16.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clickable { },
                                textAlign = TextAlign.Center,
                                text = "Hapus",
                                fontSize = 16.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                })
        }
    }
}






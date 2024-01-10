package com.polstat.pkl.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
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
import com.polstat.pkl.viewmodel.ListRutaViewModel
import com.polstat.pkl.viewmodel.RutaUiState

@Preview
@Composable
fun ListRutaPreview() {
    Capi63Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

//            ListRutaScreen(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListRutaScreen(navController: NavHostController, listRutaViewModel: ListRutaViewModel) {
    var showMenu by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

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
                    IconButton(onClick = { showSearchBar = true }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search Button",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { }) {
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

            if (showSearchBar) {
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    Arrangement.Start,
                    Alignment.CenterVertically) {
                    SearchBar(
                        query = text,
                        onQueryChange = {
                            text = it
                            listRutaViewModel.searchRuta(text)
                                        },
                        onSearch = { text = it
                            listRutaViewModel.searchRuta(text)
                                   },
                        active = false,
                        onActiveChange = { true },
                        placeholder = { Text(
                            text = stringResource(id = R.string.search_list_ruta),
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color.White) },
                        leadingIcon = {
                            IconButton(onClick = { showSearchBar = false }) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = stringResource(id = R.string.back_icon),
                                    tint = Color.White
                                )
                            }},
                        shape = RoundedCornerShape(0.dp),
                        colors = SearchBarDefaults.colors(containerColor = PklPrimary900, inputFieldColors = TextFieldDefaults.colors(Color.White)),
                        content = { listRutaViewModel.searchRuta(text) }
                    )
                }
            }
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
                RutaList(rutaUiState = listRutaViewModel.rutaUiState)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RutaRow(
    no: Int,
    noBF: String,
    noBS: String,
    noRuta: String,
    namaKRT: String
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
            text = "$no",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(
            text = noBF,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(
            text = noBS,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(
            text = noRuta,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(
            text = namaKRT,
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
            Dialog(onDismissRequest = { openDetail = false },
                content = {
                    Column(
                        modifier = Modifier
                            .background(
                                color = PklBase,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .height(500.dp),
                        Arrangement.Center,
                        Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = PklPrimary900,
                                    shape = RoundedCornerShape(
                                        topStart = 15.dp,
                                        topEnd = 15.dp
                                    )
                                )
                                .padding(
                                    top = 10.dp,
                                    bottom = 10.dp
                                ),
                            text = stringResource(id = R.string.detail_ruta).uppercase(),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .weight(1f)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.nomor_segmen_ruta),
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(
                                        top = 10.dp,
                                        bottom = 15.dp
                                    ),
                                value = "Detail1",
                                onValueChange = { },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedIndicatorColor = PklPrimary900
                                ),
                                textStyle = TextStyle(fontSize = 12.sp)
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(
                                text = stringResource(id = R.string.nomor_urut_bangunan_fisik_ruta),
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(
                                        top = 10.dp,
                                        bottom = 15.dp
                                    ),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedIndicatorColor = PklPrimary900
                                ),
                                textStyle = TextStyle(fontSize = 12.sp)
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(
                                text = stringResource(id = R.string.nomor_urut_bangunan_sensus_ruta),
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(
                                        top = 10.dp,
                                        bottom = 15.dp
                                    ),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedIndicatorColor = PklPrimary900
                                ),
                                textStyle = TextStyle(fontSize = 12.sp)
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(
                                text = stringResource(id = R.string.nomor_urut_krt_ruta),
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(
                                        top = 10.dp,
                                        bottom = 15.dp
                                    ),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedIndicatorColor = PklPrimary900
                                ),
                                textStyle = TextStyle(fontSize = 12.sp)
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(
                                text = stringResource(id = R.string.nama_krt_ruta),
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(
                                        top = 10.dp,
                                        bottom = 15.dp
                                    ),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedIndicatorColor = PklPrimary900
                                ),
                                textStyle = TextStyle(fontSize = 12.sp)
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(
                                text = stringResource(id = R.string.alamat_ruta),
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(
                                        top = 10.dp,
                                        bottom = 15.dp
                                    ),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedIndicatorColor = PklPrimary900
                                ),
                                textStyle = TextStyle(fontSize = 12.sp)
                            )
                            Spacer(modifier = Modifier.size(10.dp))
                            Text(
                                text = stringResource(id = R.string.keberadaan_genz_ortu_ruta),
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .padding(
                                        top = 10.dp,
                                        bottom = 15.dp
                                    ),
                                value = "Detail1",
                                onValueChange = {},
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedIndicatorColor = PklPrimary900
                                ),
                                textStyle = TextStyle(fontSize = 12.sp)
                            )
                        }
                        Text(modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = PklPrimary900,
                                shape = RoundedCornerShape(
                                    bottomStart = 15.dp,
                                    bottomEnd = 15.dp
                                )
                            )
                            .clickable { openDetail = false }
                            .padding(
                                top = 10.dp,
                                bottom = 10.dp
                            ),
                            textAlign = TextAlign.Center,
                            text = stringResource(id = R.string.close_popup_list_ruta).uppercase(),
                            color = Color.White,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium)
                    }
                })
        }

        if (openDialog) {
            Dialog(onDismissRequest = { openDialog = false },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = PklBase,
                                shape = RoundedCornerShape(15.dp)
                            ),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = PklPrimary900,
                                    shape = RoundedCornerShape(
                                        topStart = 15.dp,
                                        topEnd = 15.dp
                                    )
                                )
                                .padding(
                                    top = 10.dp,
                                    bottom = 10.dp
                                ),
                            text = stringResource(id = R.string.action_art),
                            color = PklBase,
                            textAlign = TextAlign.Center,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Divider()
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            Arrangement.Center,
                            Alignment.CenterHorizontally
                        ) {
                            Text(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
//                                    mungkin ini harusnya move ke screen buat ubah ruta
                                }
                                .padding(
                                    top = 10.dp,
                                    bottom = 10.dp
                                ),
                                textAlign = TextAlign.Center,
                                text = stringResource(id = R.string.ubah_action_art),
                                fontSize = 16.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium)
                            Text(modifier = Modifier
                                .fillMaxWidth()
                                .clickable { }
                                .padding(
                                    top = 10.dp,
                                    bottom = 10.dp
                                ),
                                textAlign = TextAlign.Center,
                                text = stringResource(id = R.string.salin_action_art),
                                fontSize = 16.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium)
                            Text(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
//                                    harusnya muncul dialog konfirmasi
                                }
                                .padding(
                                    top = 10.dp,
                                    bottom = 10.dp
                                ),
                                textAlign = TextAlign.Center,
                                text = stringResource(id = R.string.hapus_action_art),
                                fontSize = 16.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium)
                        }
                    }
                })
        }
    }
}

@Composable
fun RutaList(rutaUiState: RutaUiState) {
    when (rutaUiState) {
        is RutaUiState.Success -> {
            val ruta = rutaUiState.ruta
            var i = 0
            LazyColumn(modifier = Modifier.fillMaxHeight(),
                content = {
                    items(items = ruta) { ruta ->
                        i++
                        RutaRow(
                            no = i,
                            noBF = ruta.no_bg_fisik,
                            noBS = ruta.no_bg_fisik,
                            noRuta = ruta.no_urut_rt,
                            namaKRT = ruta.nama_krt
                        )
                    }
                })
        }

        is RutaUiState.Loading -> {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                text = "Loading...",
                textAlign = TextAlign.Center
            )
        }

        is RutaUiState.Error -> {}
    }
}






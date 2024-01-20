package org.odk.collect.pkl.ui.screen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.polstat.pkl.R
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.database.relation.WilayahWithAll
import com.polstat.pkl.navigation.Capi63Screen
import com.polstat.pkl.navigation.CapiScreen
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary300
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PklTertiary100
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.viewmodel.ListRutaViewModel

@Preview
@Composable
fun ListRutaPreview() {
    Capi63Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
//            val navController = rememberNavController()

//            ListRutaScreen(navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListRutaScreen(
    navController: NavHostController,
    viewModel: ListRutaViewModel
) {
    var showMenu by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var openFinalisasiBSDialog by remember { mutableStateOf(false) }
    var enableFinalisasiBSButton by remember { mutableStateOf(false) }
    var checkedCheckbox by remember { mutableStateOf(false) }
    val noBS = viewModel.noBS
    val session = viewModel.session
    val wilayahWithAll = viewModel.wilayahWithAll.collectAsState()

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
                    IconButton(onClick = {
                        viewModel.synchronizeRuta(
                            nim = session!!.nim!!,
                            noBS = noBS!!,
                            wilayahWithAll = wilayahWithAll.value
                        )
                    }) {
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
                        modifier = Modifier.background(PklBase),
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        offset = DpOffset(
                            x = 10.dp,
                            y = (-3).dp
                        )
                    ) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.finalisasi_bs)) },
                            onClick = {
                                openFinalisasiBSDialog = true
                            }
                        )
                        DropdownMenuItem(text = { Text(text = stringResource(id = R.string.ambil_sampel)) },
                            onClick = { }
                        )
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
//                            listRutaViewModel.searchRuta(text)
                        },
                        onSearch = { text = it
//                            listRutaViewModel.searchRuta(text)
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
                        content = {
//                            listRutaViewModel.searchRuta(text)
                        }
                    )
                }
            }

            if ( openFinalisasiBSDialog ) {
                AlertDialog(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onDismissRequest = { openFinalisasiBSDialog = false },
                    confirmButton = {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { },
                            enabled = enableFinalisasiBSButton,
                            content = {
                                Text(
                                    text = stringResource(id = R.string.kirim_hasil_listing).uppercase(),
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    color = PklBase
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PklPrimary900)
                        )
                    },
                    title = {
                        Text(
                            text = stringResource(id = R.string.konfirmasi_finalisasi_bs),
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 18.sp,
                            color = PklPrimary900,
                            textAlign = TextAlign.Center
                        )
                    },
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            Arrangement.Start,
                            Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checkedCheckbox,
                                onCheckedChange = { checkedCheckbox = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = PklPrimary900,
                                    checkmarkColor = PklBase,
                                )
                            )
                            Text(
                                text = stringResource(id = R.string.pernyataan_konfirmasi_finalisasi_bs),
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                    },
                    shape = RoundedCornerShape(15.dp),
                    containerColor = PklBase
                )
            }

            enableFinalisasiBSButton = checkedCheckbox

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
                RutaList(
                    wilayahWithAll =  wilayahWithAll.value,
                    navController = navController,
                    searchText = text
                )

            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(all = 16.dp),
                onClick = {
                    navController.navigate(CapiScreen.Listing.ISI_RUTA + "/$noBS")
                },
                containerColor = PklPrimary900
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.fab),
                    tint = Color.White
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RutaRow(
    keluarga: KeluargaEntity,
    ruta: RutaEntity,
    viewModel: ListRutaViewModel,
    navController: NavHostController
) {
    var openActionDialog by remember { mutableStateOf(false) }
    var openDetail by remember { mutableStateOf(false) }
    var openPasswordMasterDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .combinedClickable(onLongClick = {
                openActionDialog = true
            },
                onClick = { }),
        Arrangement.SpaceEvenly,
        Alignment.CenterVertically
    ) {
//        Spacer(modifier = Modifier.size(20.dp))
            Text(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                text = "${keluarga.noBgFisik}",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )

//        Spacer(modifier = Modifier.size(40.dp))
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            text = "${keluarga.noBgSensus}",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

//        Spacer(modifier = Modifier.size(40.dp))
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            text = "${ruta.noUrutRuta}",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

//        Spacer(modifier = Modifier.size(40.dp))
        Text(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            text = ruta.namaKrt!!,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

//        Spacer(modifier = Modifier.size(40.dp))
        IconButton(onClick = {
            openDetail = true
        }) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(id = R.string.info_icon),
            )
        }

//      menampilkan Pop Up Detail Ruta
        if (openDetail) {
//            RutaDetailPopup(showDialog = openDetail)
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
                        ) {
                            DetailRutaTextField(
                                label = R.string.sls,
                                value = "${keluarga.SLS}"
                            )
                            DetailRutaTextField(
                                label = R.string.nomor_segmen_ruta,
                                value = "${keluarga.noSegmen}"
                            )
                            DetailRutaTextField(
                                label = R.string.nomor_urut_bangunan_fisik_ruta,
                                value = "${keluarga.noBgFisik}"
                            )
                            DetailRutaTextField(
                                label = R.string.nomor_urut_bangunan_sensus_ruta,
                                value = "${keluarga.noBgSensus}"
                            )
                            DetailRutaTextField(
                                label = R.string.nomor_urut_keluarga,
                                value = "${keluarga.noUrutKlg}"
                            )
                            DetailRutaTextField(
                                label = R.string.nama_kepala_keluarga,
                                value = "${keluarga.namaKK}"
                            )
                            DetailRutaTextField(
                                label = R.string.alamat_ruta,
                                value = keluarga.alamat!!
                            )
                            DetailRutaTextField(
                                label = R.string.keberadaan_genz_ortu_keluarga,
                                value = "${keluarga.isGenzOrtu}"
                            )
                            DetailRutaTextField(
                                label = R.string.no_urut_keluarga_egb,
                                value = "${keluarga.noUrutKlgEgb}"
                            )
                            DetailRutaTextField(
                                label = R.string.jml_pengelolaan_makan_keluarga,
                                value = "${keluarga.penglMkn}"
                            )
                            DetailRutaTextField(
                                label = R.string.nomor_urut_krt_ruta,
                                value = "${ruta.noUrutRuta}"
                            )
                            DetailRutaTextField(
                                label = R.string.identifikasi_kk_krt,
                                value = "${ruta.kkOrKrt}"
                            )
                            DetailRutaTextField(
                                label = R.string.nama_krt_ruta,
                                value = ruta.namaKrt!!
                            )
                            DetailRutaTextField(
                                label = R.string.keberadaan_genz_ortu_ruta,
                                value = "${ruta.genzOrtu}"
                            )
                            DetailRutaTextField(
                                label = R.string.kategori_jml_genz,
                                value = "${ruta.katGenz}"
                            )
                            DetailRutaTextField(
                                label = R.string.nomor_urut_ruta_egb,
                                value = "${ruta.noUrutEgb}"
                            )
                            DetailRutaTextField(
                                label = R.string.catatan,
                                value = ruta.catatan!!
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

//      menampilkan Action Dialog
        if (openActionDialog) {
//            RutaActionDialog(showDialog = openActionDialog)
            Dialog(onDismissRequest = { openActionDialog = false },
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
//                          untuk edit ruta
                            Text(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
//                                    mungkin ini harusnya move ke screen buat ubah ruta
                                    navController.navigate(CapiScreen.Listing.EDIT_RUTA + "/${ruta.noBS}/${keluarga.kodeKlg}/${ruta.kodeRuta}")
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

//                          untuk menghapus ruta
                            Text(modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    openPasswordMasterDialog = true
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

//      menampilkan pop up password master jika mengeklik action hapus
        if (openPasswordMasterDialog) {
            var inputPasswordMaster by remember { mutableStateOf("") }

            AlertDialog(
                modifier = Modifier
                    .fillMaxWidth(),
                onDismissRequest = { openPasswordMasterDialog = false },
                confirmButton = {
                    Button(
                        modifier = Modifier.fillMaxWidth(0.45f),
                        onClick = {
                            viewModel.deleteRuta(
                                kodeRuta = ruta.kodeRuta
                            )
                            openPasswordMasterDialog = false
                            openActionDialog = false
                            navController.navigate(Capi63Screen.ListRuta.route + "/${ruta.noBS}"){
                                popUpTo(Capi63Screen.ListRuta.route + "/${ruta.noBS}"){
                                    inclusive = true
                                }
                            }
                            Toast.makeText(context, "Ruta ${ruta.namaKrt} berhasil dihapus", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(PklPrimary900)) {
                        Text(text = stringResource(id = R.string.hapus_pass_master))
                    } },
                dismissButton = {
                    Button(
                        modifier = Modifier.fillMaxWidth(0.45f),
                        onClick = { openPasswordMasterDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = PklTertiary100, contentColor = PklPrimary900)
                    ) {
                        Text(text = stringResource(id = R.string.batal_pass_master))
                    }},
                title = { Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.konfirmasi_pass_master),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                ) },
                containerColor = PklBase,
                titleContentColor = PklPrimary900,
                textContentColor = Color.Black,
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        Arrangement.Center,
                        Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.ket_konfirm_hapus_pass_master),
                            textAlign = TextAlign.Center,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.size(15.dp))
                        TextField(
                            singleLine = true,
                            value = inputPasswordMaster,
                            onValueChange = { inputPasswordMaster = it},
                            placeholder = {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(id = R.string.input_pass_master),
                                    textAlign = TextAlign.Center,
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp
                                )},
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                unfocusedIndicatorColor = Color.Black,
                                focusedIndicatorColor = PklPrimary900,
                                selectionColors = TextSelectionColors(handleColor = PklPrimary900, backgroundColor = PklPrimary900.copy(0.5f)),
                                focusedPlaceholderColor = Color.Black.copy(alpha = 0.7f),
                                unfocusedPlaceholderColor = Color.Black.copy(alpha = 0.7f)
                            ),
                            textStyle = TextStyle(fontSize = 16.sp, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun RutaList(
    wilayahWithAll: WilayahWithAll,
    navController: NavHostController,
    searchText: String
) {
    val filteredList = wilayahWithAll.listKeluargaWithRuta
        ?.filter { keluargaWithRuta ->
            keluargaWithRuta.listRuta.any { ruta ->
                ruta.kodeRuta.contains(searchText, ignoreCase = true) ||
                ruta.namaKrt!!.contains(searchText, ignoreCase = true)
            }
        }
        ?.sortedBy { keluargaWithRuta ->
            keluargaWithRuta.listRuta.firstOrNull()?.kodeRuta
        }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .horizontalScroll(state = rememberScrollState(), enabled = true),
        content = {
            println("List Ruta Screen: ${wilayahWithAll.listKeluargaWithRuta!!.isNotEmpty()} ${wilayahWithAll.listKeluargaWithRuta!!.size}")
//            if (wilayahWithAll.listKeluargaWithRuta!!.isNotEmpty()) {
//                wilayahWithAll.listKeluargaWithRuta!!.forEach { keluargaWithRuta ->
            filteredList?.forEach { keluargaWithRuta ->
                if (keluargaWithRuta.listRuta.isNotEmpty()) {
                    val daftarRuta = keluargaWithRuta.listRuta.filter { it.status != "delete" }
                    items(daftarRuta.size) { index ->
                        val ruta = daftarRuta[index]
                        RutaRow(
                            keluarga = keluargaWithRuta.keluarga,
                            ruta = ruta,
                            viewModel = hiltViewModel(),
                            navController = navController
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun DetailRutaTextField(
    label: Int,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 15.dp,
                end = 15.dp,
                top = 5.dp,
                bottom = 5.dp
            )
    ) {
        Text(
            text = stringResource(id = label),
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = PklPrimary900
        )
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .background(Color.Transparent)
            ,
            readOnly = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedIndicatorColor = PklPrimary900,
                unfocusedIndicatorColor = PklPrimary900
            ),
            textStyle = TextStyle(
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            ),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Composable
fun LimitedText(
    searchText: String
) {
    val limitedText = searchText.take(10)

    Text(
        text = limitedText,
        fontWeight = FontWeight.Medium,
        fontFamily = PoppinsFontFamily,
        fontSize = 14.sp
    )
}
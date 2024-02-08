package org.odk.collect.pkl.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.polstat.pkl.R
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary300
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.utils.UtilFunctions
import com.polstat.pkl.viewmodel.ListRutaViewModel


@Suppress("UNUSED_EXPRESSION")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListRutaScreen(
    navController: NavHostController,
    viewModel: ListRutaViewModel
) {
    var showMenu by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var isListRuta by remember { mutableStateOf(true) }
    var openFinalisasiBSDialog by remember { mutableStateOf(false) }
    var enableFinalisasiBSButton by remember { mutableStateOf(false) }
    var checkedCheckbox by remember { mutableStateOf(false) }
    val session = viewModel.session
    val listRutaByWilayah = viewModel.listRutaByWilayah
    val listRutaByKeluarga = viewModel.listRutaByKeluarga
    val listKeluargaByWilayah = viewModel.listKeluargaByWilayah
    val listKeluargaByRuta = viewModel.listKeluargaByRuta
    val idBS = viewModel.idBS
    val isMonitoring = viewModel.isMonitoring
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = viewModel.showSuccessToastChannel) {
        viewModel.showSuccessToastChannel.collectLatest { show ->
            if (show) {
                delay(1500)
                Toast.makeText(context, viewModel.successMessage.value, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(key1 = viewModel.showErrorToastChannel) {
        viewModel.showErrorToastChannel.collectLatest { show ->
            if (show) {
                delay(1500)
                Toast.makeText(context, viewModel.errorMessage.value, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val textId = if (isListRuta) R.string.list_ruta_title else R.string.list_keluarga
                    val status = if (isMonitoring == true) stringResource(R.string.monitoring) else stringResource(R.string.listing)

                    Text(
                        text = stringResource(id = textId).uppercase() + " ($status)",
                        style = TextStyle(
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PklPrimary900,
                    titleContentColor = Color.White,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(CapiScreen.Listing.LIST_BS + "/$isMonitoring"){
                                popUpTo(CapiScreen.Listing.LIST_BS + "/$isMonitoring"){
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                val getListKeluargaJob = async {
                                    idBS?.let { viewModel.getAllKeluargaByWilayah(it) }
                                }
                                getListKeluargaJob.await()
                                val synchronizeRutaJob = async {
                                    idBS?.let {
                                        viewModel.synchronizeRuta(
                                            nim = session?.nim.toString(),
                                            idBS = it,
                                            listKeluargaByWilayah = listKeluargaByWilayah.value
                                        )
                                    }
                                }
                                synchronizeRutaJob.await()
                                delay(1000L)
                                navController.navigate(CapiScreen.Listing.LIST_RUTA + "/${idBS}") {
                                    popUpTo(CapiScreen.Listing.LIST_BS + "/${idBS}") {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    ) {
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
                        if (session?.isKoor == true) {
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.finalisasi_bs)) },
                                onClick = {
                                    showMenu = false
                                    openFinalisasiBSDialog = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = stringResource(id = R.string.ambil_sampel)) },
                                onClick = {
                                    showMenu = false
                                    if (idBS != null) {
                                        coroutineScope.launch {
                                            val generateRutaJob = async { viewModel.generateRuta(idBS) }
                                            generateRutaJob.await()
                                            navController.navigate(CapiScreen.Listing.LIST_BS){
                                                popUpTo(CapiScreen.Listing.LIST_BS){
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    }
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = {
                                val resourceId = if (isListRuta) R.string.tampilkan_list_keluarga else R.string.tampilkan_list_ruta
                                Text(text = stringResource(id = resourceId))
                            },
                            onClick = {
                                coroutineScope.launch {
                                    if (isListRuta) {
                                        idBS?.let { viewModel.getAllRutaByWilayah(it) }
                                    } else {
                                        idBS?.let { viewModel.getAllKeluargaByWilayah(it) }
                                    }
                                }
                                showMenu = false
                                isListRuta = !isListRuta
                            }
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
                        },
                        onSearch = { text = it },
                        active = false,
                        onActiveChange = { true },
                        placeholder = {
                            if (isListRuta) {
                                Text(
                                    text = stringResource(id = R.string.search_list_ruta),
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                            else {
                                Text(
                                    text = stringResource(id = R.string.search_list_keluarga),
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            } },
                        leadingIcon = {
                            IconButton(onClick = { showSearchBar = false }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(id = R.string.back_icon),
                                    tint = Color.White
                                )
                            }},
                        shape = RoundedCornerShape(0.dp),
                        colors = SearchBarDefaults.colors(containerColor = PklPrimary900, inputFieldColors = TextFieldDefaults.colors(Color.White)),
                        content = { }
                    )
                }
            }
            if(session?.isKoor == true && isMonitoring == true){
                if ( openFinalisasiBSDialog ) {
                    AlertDialog(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onDismissRequest = { openFinalisasiBSDialog = false },
                        confirmButton = {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    openFinalisasiBSDialog = false
                                    coroutineScope.launch {
                                        val finalisasiBSJob = async {
                                            idBS?.let { viewModel.finalisasiBS(it) }
                                        }
                                        finalisasiBSJob.await()
                                        navController.navigate(CapiScreen.Listing.LIST_BS){
                                            popUpTo(CapiScreen.Listing.LIST_BS){
                                                inclusive = true
                                            }
                                        }
                                    }
                                },
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
            }
            enableFinalisasiBSButton = checkedCheckbox
        },
        content = { innerPadding ->

            val colWeight1 = .15f
            val colWeight2 = .55f

            val filteredRutaList = listRutaByWilayah.value.filter { ruta ->
                ruta.kodeRuta.contains(text, ignoreCase = true) || ruta.namaKrt.contains(text, ignoreCase = true)
            }

            val filteredKeluargaList = listKeluargaByWilayah.value.filter { keluarga ->
                keluarga.kodeKlg.contains(text, ignoreCase = true) || keluarga.namaKK.contains(text, ignoreCase = true)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = PklBase),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(PklPrimary300),
                        Arrangement.SpaceEvenly,
                        Alignment.CenterVertically,
                    ) {
                        TableCell(text = stringResource(id = R.string.no_bf_list_ruta), fontSize = 12.sp, color = Color.White, weight = colWeight1)
                        TableCell(text = stringResource(id = R.string.no_bs_list_ruta), color = Color.White, fontSize = 12.sp, weight = colWeight1)

                        if (isListRuta) {
                            TableCell(text = stringResource(id = R.string.no_ruta_list_ruta), color = Color.White, fontSize = 12.sp, weight = colWeight1)
                            TableCell(text = stringResource(id = R.string.nama_krt_list_ruta), color = Color.White, fontSize = 12.sp, weight = colWeight2)
//                        TableCell(text = stringResource(id = R.string.detail_list_ruta_klg), color = Color.White, fontSize = 12.sp, weight = colWeight1)
                        }
                        else {
                            TableCell(text = stringResource(id = R.string.no_urut_klg), color = Color.White, fontSize = 12.sp, weight = colWeight1)
                            TableCell(text = stringResource(id = R.string.nama_kepala_keluarga), color = Color.White, fontSize = 12.sp, weight = colWeight2)
//                        TableCell(text = stringResource(id = R.string.detail_list_ruta_klg), color = Color.White, fontSize = 12.sp, weight = colWeight1)
                        }
                    }
                }

                val itemsList = if (isListRuta) {
                    filteredRutaList.filter { it.status != "delete" }
                } else {
                    filteredKeluargaList.filter { it.status != "delete" }
                }

                items(itemsList.size) { index ->
                    if (isListRuta) {
                        val rutaItem = itemsList[index]
                        RutaOrKlgRow(
                            ruta = rutaItem as RutaEntity,
                            listKeluargaByRuta = listKeluargaByRuta.value,
                            viewModel = viewModel,
                            navController = navController,
                            isListRuta = isListRuta
                        )
                    } else {
                        val keluargaItem = itemsList[index]
                        RutaOrKlgRow(
                            keluarga = keluargaItem as KeluargaEntity,
                            listRutaByKeluarga = listRutaByKeluarga.value,
                            viewModel = viewModel,
                            navController = navController,
                            isListRuta = isListRuta
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (isMonitoring == false && session?.isKoor == true){
                FloatingActionButton(
                    modifier = Modifier
                        .padding(all = 16.dp),
                    onClick = {
                        navController.navigate(CapiScreen.Listing.ISI_RUTA + "/$idBS")
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
        }
    )
}

@Suppress("KotlinConstantConditions")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RutaOrKlgRow(
    ruta: RutaEntity = RutaEntity(),
    keluarga: KeluargaEntity = KeluargaEntity(),
    listKeluargaByRuta: List<KeluargaEntity> = emptyList(),
    listRutaByKeluarga: List<RutaEntity> = emptyList(),
    viewModel: ListRutaViewModel,
    navController: NavHostController,
    isListRuta: Boolean
) {
    var openActionDialog by remember { mutableStateOf(false) }
    var openDetail by remember { mutableStateOf(false) }
    var openPasswordMasterDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val colWeight1 = .15f
    val colWeight2 = .55f

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
        val selectedData = if (isListRuta) listKeluargaByRuta[0] else keluarga
        val noUrut = if (isListRuta) ruta.noUrutRuta else keluarga.noUrutKlg
        val nama = if (isListRuta) ruta.namaKrt else keluarga.namaKK

        TableCell(text = selectedData.noBgFisik, weight = colWeight1)
        TableCell(text = selectedData.noBgSensus, weight = colWeight1)
        TableCell(text = noUrut, weight = colWeight1)
        TableCell(text = viewModel.sederhanakanNama(nama), weight = colWeight2)

        IconButton(
            modifier = Modifier.weight(colWeight1),
            onClick = {
                openDetail = true
                coroutineScope.launch {
                    if (isListRuta) {
                        viewModel.getAllKeluargaByRuta(ruta.kodeRuta)
                    } else {
                        viewModel.getAllRutaByKeluarga(keluarga.kodeKlg)
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(id = R.string.info_icon),
            )
        }

//      menampilkan pop up detail ruta atau keluarga
        if (openDetail) {
//          detail ruta
            if (isListRuta) {
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
                                    .weight(1f)
                            ) {
                                LazyColumn(
                                    content = {
                                        item {
                                            DetailRutaTextField(
                                                label = R.string.kode_ruta,
                                                value = ruta.kodeRuta
                                            )
                                            DetailRutaTextField(
                                                label = R.string.nomor_urut_krt_ruta,
                                                value = UtilFunctions.padWithZeros(
                                                    ruta.noUrutRuta,
                                                    3
                                                )
                                            )
                                            DetailRutaTextField(
                                                label = R.string.nomor_urut_ruta_egb,
                                                value = UtilFunctions.convertTo3DigitsString(ruta.noUrutEgb)
                                            )
                                            DetailRutaTextField(
                                                label = R.string.identifikasi_kk_krt,
                                                value = if (ruta.kkOrKrt == "1") "Kepala Keluarga (KK) saja" else if (ruta.kkOrKrt == "2") "Kepala Rumah Tangga (KRT) saja" else "KK Sekaligus KRT",
                                            )
                                            DetailRutaTextField(
                                                label = R.string.nama_krt_ruta,
                                                value = ruta.namaKrt
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
                                                label = R.string.catatan,
                                                value = ruta.catatan.ifEmpty { "N/A" }
                                            )
                                            Spacer(modifier = Modifier.size(5.dp))
                                            Text(
                                                modifier = Modifier.fillMaxWidth(),
                                                text = stringResource(id = R.string.daftar_klg_terkait).uppercase(),
                                                fontFamily = PoppinsFontFamily,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 16.sp,
                                                color = PklPrimary900,
                                                textAlign = TextAlign.Center
                                            )
                                        }

                                        val daftarKeluargaByRuta = listKeluargaByRuta.filter { it.status != "delete" }
                                        items(daftarKeluargaByRuta.size) { itemIndex ->
                                            val keluargaByRuta = daftarKeluargaByRuta[itemIndex]
                                            var expanded by remember { mutableStateOf(false) }
                                            OutlinedCard(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(
                                                        start = 15.dp,
                                                        end = 15.dp,
                                                        top = 5.dp,
                                                        bottom = 5.dp
                                                    )
                                                    .clickable { expanded = !expanded },
                                                shape = RoundedCornerShape(16.dp),
                                                border = BorderStroke(1.dp, color = PklPrimary900),
                                                colors = CardDefaults.outlinedCardColors(
                                                    containerColor = PklBase
                                                ),
                                                elevation = CardDefaults.cardElevation(4.dp),
                                                content = {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(16.dp)
                                                    ) {
                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.SpaceBetween
                                                        ) {
                                                            Text(
                                                                text = keluargaByRuta.kodeKlg,
                                                                fontFamily = PoppinsFontFamily,
                                                                fontWeight = FontWeight.Medium,
                                                                fontSize = 18.sp,
                                                            )
                                                            Icon(
                                                                imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                                                contentDescription = stringResource(
                                                                    id = R.string.toggle_icon
                                                                ),
                                                                modifier = Modifier.size(30.dp),
                                                                tint = PklPrimary900
                                                            )
                                                        }
                                                        Text(
                                                            text = keluargaByRuta.namaKK,
                                                            fontFamily = PoppinsFontFamily,
                                                            fontWeight = FontWeight.Medium,
                                                            fontSize = 16.sp
                                                        )
                                                        if (expanded) {
                                                            Spacer(modifier = Modifier.size(5.dp))
                                                            Divider(
                                                                thickness = 1.dp,
                                                                color = Color.Black
                                                            )
                                                            Spacer(modifier = Modifier.height(8.dp))
                                                            DetailCard(
                                                                keluarga = keluarga,
                                                                isListRuta = isListRuta
                                                            )
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
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

//          detail keluarga
            else {
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
                                text = stringResource(id = R.string.detail_keluarga).uppercase(),
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) {
                                LazyColumn(
                                    content = {
                                        item {
                                            DetailRutaTextField(
                                                label = R.string.kode_klg,
                                                value = keluarga.kodeKlg
                                            )
                                            DetailRutaTextField(
                                                label = R.string.sls,
                                                value = keluarga.banjar
                                            )
                                            DetailRutaTextField(
                                                label = R.string.nomor_segmen_ruta,
                                                value = keluarga.noSegmen
                                            )
                                            DetailRutaTextField(
                                                label = R.string.nomor_urut_bangunan_fisik_ruta,
                                                value = UtilFunctions.padWithZeros(
                                                    keluarga.noBgFisik,
                                                    3
                                                )
                                            )
                                            DetailRutaTextField(
                                                label = R.string.nomor_urut_bangunan_sensus_ruta,
                                                value = UtilFunctions.padWithZeros(
                                                    keluarga.noBgSensus,
                                                    3
                                                )
                                            )
                                            DetailRutaTextField(
                                                label = R.string.nomor_urut_keluarga,
                                                value = UtilFunctions.padWithZeros(
                                                    keluarga.noUrutKlg,
                                                    3
                                                )
                                            )
                                            DetailRutaTextField(
                                                label = R.string.nama_kepala_keluarga,
                                                value = keluarga.namaKK
                                            )
                                            DetailRutaTextField(
                                                label = R.string.alamat_ruta,
                                                value = keluarga.alamat
                                            )
                                            DetailRutaTextField(
                                                label = R.string.keberadaan_genz_ortu_keluarga,
                                                value = "${keluarga.isGenzOrtu}"
                                            )
                                            DetailRutaTextField(
                                                label = R.string.no_urut_keluarga_egb,
                                                value = UtilFunctions.convertTo3DigitsString(
                                                    keluarga.noUrutKlgEgb
                                                )
                                            )
                                            DetailRutaTextField(
                                                label = R.string.jml_pengelolaan_makan_keluarga,
                                                value = "${keluarga.penglMkn}"
                                            )
                                            Text(
                                                modifier = Modifier.fillMaxWidth(),
                                                text = stringResource(id = R.string.daftar_ruta_terkait).uppercase(),
                                                fontFamily = PoppinsFontFamily,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 16.sp,
                                                color = PklPrimary900,
                                                textAlign = TextAlign.Center
                                            )
                                        }

                                        val daftarRutaByKlg =
                                            listRutaByKeluarga.filter { it.status != "delete" }
                                        items(daftarRutaByKlg.size) { itemIndex ->
                                            val rutaByKlg = daftarRutaByKlg[itemIndex]
                                            var expanded by remember { mutableStateOf(false) }
                                            OutlinedCard(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(
                                                        start = 15.dp,
                                                        end = 15.dp,
                                                        top = 5.dp,
                                                        bottom = 5.dp
                                                    )
                                                    .clickable { expanded = !expanded },
                                                shape = RoundedCornerShape(16.dp),
                                                border = BorderStroke(
                                                    1.dp,
                                                    color = PklPrimary900
                                                ),
                                                colors = CardDefaults.outlinedCardColors(
                                                    containerColor = PklBase,
                                                ),
                                                elevation = CardDefaults.cardElevation(4.dp),
                                                content = {
                                                    Column(
                                                        modifier = Modifier
                                                            .fillMaxSize()
                                                            .padding(16.dp)
                                                    ) {
                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.SpaceBetween
                                                        ) {
                                                            Text(
                                                                text = ruta.kodeRuta,
                                                                fontFamily = PoppinsFontFamily,
                                                                fontWeight = FontWeight.Medium,
                                                                fontSize = 18.sp,
                                                            )
                                                            Icon(
                                                                imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                                                contentDescription = stringResource(
                                                                    id = R.string.toggle_icon
                                                                ),
                                                                modifier = Modifier.size(30.dp),
                                                                tint = PklPrimary900
                                                            )
                                                        }
                                                        Text(
                                                            text = rutaByKlg.namaKrt,
                                                            fontFamily = PoppinsFontFamily,
                                                            fontWeight = FontWeight.Medium,
                                                            fontSize = 16.sp
                                                        )
                                                        if (expanded) {
                                                            Spacer(
                                                                modifier = Modifier.size(
                                                                    5.dp
                                                                )
                                                            )
                                                            Divider(
                                                                thickness = 1.dp,
                                                                color = Color.Black
                                                            )
                                                            Spacer(
                                                                modifier = Modifier.height(
                                                                    8.dp
                                                                )
                                                            )
                                                            DetailCard(
                                                                ruta = rutaByKlg,
                                                                isListRuta = isListRuta
                                                            )
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
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

        }

//      menampilkan action dialog
        if (openActionDialog) {
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
                                    navController.navigate(CapiScreen.Listing.EDIT_RUTA + "/${ruta.idBS}/${keluarga.kodeKlg}/${ruta.kodeRuta}")
//                                    navController.navigate(CapiScreen.Listing.EDIT_RUTA + "/${ruta.noBS}/${keluarga.kodeKlg}/${ruta.kodeRuta}")
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
                                .clickable {
//                                    navController.navigate(CapiScreen.Listing.SALIN_RUTA + "/${ruta.noBS}/${ruta.kodeRuta}")
//                                    navController.navigate(CapiScreen.Listing.SALIN_RUTA + "/${ruta.idBS}/${ruta.kodeRuta}")
                                }
                                .padding(
                                    top = 10.dp,
                                    bottom = 10.dp
                                ),
                                textAlign = TextAlign.Center,
                                text = stringResource(id = R.string.salin_action_art),
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
//                            viewModel.deleteRuta(
//                                kodeRuta = ruta.kodeRuta
//                            )
//                            openPasswordMasterDialog = false
//                            openActionDialog = false
//                            navController.navigate(CapiScreen.Listing.LIST_RUTA + "/${ruta.noBS}"){
//                                popUpTo(CapiScreen.Listing.LIST_RUTA + "/${ruta.noBS}"){
//                                    inclusive = true
//                                }
//                            }
//                            Toast.makeText(context, "Ruta ${ruta.namaKrt} berhasil dihapus", Toast.LENGTH_SHORT).show()
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

//@Composable
//fun RutaList(
//    wilayahWithAll: WilayahWithAll,
//    navController: NavHostController,
//    viewModel: ListRutaViewModel,
//    searchText: String
//) {
//    val filteredList = wilayahWithAll.listKeluargaWithRuta
//        ?.filter { keluargaWithRuta ->
//            keluargaWithRuta.listRuta.any { ruta ->
//                ruta.kodeRuta.contains(searchText, ignoreCase = true) ||
//                ruta.namaKrt.contains(searchText, ignoreCase = true)
//            }
//        }
//        ?.sortedBy { keluargaWithRuta ->
//            keluargaWithRuta.listRuta.firstOrNull()?.kodeRuta
//        }
//
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .horizontalScroll(
//                state = rememberScrollState(),
//                enabled = true
//            ),
//        content = {
//            println("List Ruta Screen: ${wilayahWithAll.listKeluargaWithRuta!!.isNotEmpty()} ${wilayahWithAll.listKeluargaWithRuta!!.size}")
//
//            filteredList?.forEach { keluargaWithRuta ->
//                if (keluargaWithRuta.listRuta.isNotEmpty()) {
//                    val daftarRuta = keluargaWithRuta.listRuta.filter { it.status != "delete" }
//                    items(daftarRuta.size) { index ->
//                        val ruta = daftarRuta[index]
//                        RutaRow(
//                            keluarga = keluargaWithRuta.keluarga,
//                            ruta = ruta,
//                            viewModel = viewModel,
//                            navController = navController,
//                            isListRuta = true
//                        )
//                    }
//                }
//            }
//        }
//    )
//}

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
fun DetailCard(
    keluarga: KeluargaEntity = KeluargaEntity(),
    ruta: RutaEntity = RutaEntity(),
    isListRuta: Boolean,
    fontFamily: FontFamily = PoppinsFontFamily,
    fontWeight: FontWeight = FontWeight.Medium,
    fontSize: TextUnit = 14.sp,
    titleColor: Color = PklPrimary900,
    contentColor: Color = Color.Black
) {
    val resources = if (isListRuta) {
        listOf(
            R.string.kode_klg to keluarga.kodeKlg,
            R.string.sls to keluarga.banjar,
            R.string.no_bf_list_ruta to keluarga.noBgFisik,
            R.string.no_bs_list_ruta to keluarga.noBgSensus,
            R.string.nomor_segmen_ruta to keluarga.noSegmen,
            R.string.nomor_urut_keluarga to keluarga.noUrutKlg,
            R.string.no_urut_keluarga_egb to keluarga.noUrutKlgEgb,
            R.string.nama_kepala_keluarga to keluarga.namaKK,
            R.string.alamat_ruta to keluarga.alamat,
            R.string.keberadaan_genz_ortu_keluarga to keluarga.isGenzOrtu,
            R.string.jml_pengelolaan_makan_keluarga to keluarga.penglMkn
        )
    } else {
        listOf(
            R.string.kode_ruta to ruta.kodeRuta,
            R.string.no_list_ruta to ruta.noUrutRuta,
            R.string.nomor_urut_ruta_egb to ruta.noUrutEgb,
            R.string.identifikasi_kk_krt to ruta.kkOrKrt,
            R.string.nama_krt_list_ruta to ruta.namaKrt,
            R.string.keberadaan_genz_ortu_ruta to ruta.genzOrtu,
            R.string.kategori_jml_genz to ruta.katGenz,
            R.string.catatan to ruta.catatan
        )
    }

    resources.forEach { (resId, value) ->
        Text(
            text = stringResource(id = resId),
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            fontSize = fontSize,
            color = titleColor
        )
        Text(
            text = "$value",
            fontFamily = fontFamily,
            fontWeight = fontWeight,
            fontSize = fontSize,
            color = contentColor
        )
    }
}


@Composable
fun RowScope.TableCell(
    text: String,
    fontSize: TextUnit = 16.sp,
    color: Color = Color.Black,
    weight: Float
) {
    Text(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .weight(weight),
        text = text,
        color = color,
        fontFamily = PoppinsFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = fontSize
    )
}

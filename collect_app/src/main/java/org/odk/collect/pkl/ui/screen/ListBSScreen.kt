package org.odk.collect.pkl.ui.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.R
import com.polstat.pkl.database.entity.WilayahEntity
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.viewmodel.ListBSViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.odk.collect.pkl.navigation.CapiScreen
import org.odk.collect.pkl.ui.screen.components.LoadingDialog
import java.util.Date

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListBSScreen(
    navController: NavHostController,
    viewModel: ListBSViewModel
) {

    val listWilayah = viewModel.listWilayah

    val context = LocalContext.current

    val isMonitoring = viewModel.isMonitoring

    val coroutineScope = rememberCoroutineScope()

    val showLoading by viewModel.showLoadingChannel.collectAsState(true)

    val isSuccessed = viewModel.isSuccesed.collectAsState()

    LaunchedEffect(key1 = viewModel.showErrorToastChannel) {
        viewModel.updateShowLoading(isSuccessed.value)
        viewModel.showErrorToastChannel.collectLatest { isError ->
            if (isError) {
                delay(1500)
                Toast.makeText(context, viewModel.errorMessage.value, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(key1 = viewModel.showSuccessToastChannel) {
        viewModel.updateShowLoading(isSuccessed.value)
        viewModel.showSuccessToastChannel.collectLatest { isSuccess ->
            if (isSuccess) {
                delay(1500)
                Toast.makeText(context, viewModel.successMessage.value, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LoadingDialog(
        showDialog = showLoading
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PklPrimary900
                ),
                modifier = Modifier.shadow(10.dp),
                title = {
                    Text(
                        text = stringResource(R.string.title_list_bs) + " (" + if (isMonitoring == true) { stringResource(R.string.monitoring) } else { stringResource(R.string.listing) } + ")",
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
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(CapiScreen.Sampling.START){
                                popUpTo(CapiScreen.Sampling.LISTING){
                                    inclusive = false
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            tint = Color.White,
                            contentDescription = stringResource(R.string.back_icon),
                            modifier = Modifier.size(25.dp)
                        )
                    }
                },
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    navController.navigate(CapiScreen.Listing.LIST_BS + "/$isMonitoring"){
                                        popUpTo(CapiScreen.Listing.LIST_BS + "/$isMonitoring"){
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Sync,
                                tint = Color.White,
                                contentDescription = stringResource(R.string.sync_icon),
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            if (isMonitoring != null) {
                val listWilayahValue = listWilayah.value

                ListBS(
                    listWilayah = listWilayahValue,
                    navController = navController,
                    modifier = Modifier.padding(innerPadding),
                    isMonitoring = isMonitoring,
                    viewModel = viewModel
                )
            }
        },
    )
}

@Composable
private fun BlokSensus(
    onLihatRutaClicked: () -> Unit,
    onLihatSampleClicked: () -> Unit,
    wilayah: WilayahEntity,
    viewModel: ListBSViewModel,
    navController: NavHostController
) {
    var expanded by remember { mutableStateOf(false) }

    val isMonitoring = viewModel.isMonitoring?: false

    var showMenu by remember { mutableStateOf(false) }

    var openFinalisasiBSDialog by remember { mutableStateOf(false) }

    var enableFinalisasiBSButton by remember { mutableStateOf(false) }

    var checkedCheckbox by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(top = 20.dp, bottom = 8.dp, start = 10.dp, end = 10.dp)
            .shadow(elevation = 15.dp, shape = RoundedCornerShape(10.dp))
            .background(color = Color.White),
    ) {
        Column (
            modifier = Modifier
                .padding(9.dp),
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.blok_sensus) + wilayah.noBS,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = Color.DarkGray
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(0.9f)
                ) {
                    Row {
                        Text(
                            text = stringResource(R.string.kec) + wilayah.namaKec + stringResource(R.string.kel) + wilayah.namaKel,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.DarkGray
                        )
                    }
                    Row {
                        Text(
                            text = wilayah.namaKab + ", " + "Bali",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.DarkGray
                        )
                    }
                }
            }
            Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)) {
                Column {
                    if (wilayah.status == "listing") {
                        Text(text = stringResource(R.string.proses_n_listing), softWrap = true, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = PklPrimary, fontFamily = PoppinsFontFamily, fontSize = 14.sp)
                    } else {
                        Text(text = stringResource(R.string.proses_n_listing), softWrap = true, textAlign = TextAlign.Center, color = Color.LightGray, fontFamily = PoppinsFontFamily, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }

                }
                Column {
                    if (wilayah.status == "listing-selesai") {
                        Text(text = stringResource(R.string.listing_n_selesai), softWrap = true, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = PklPrimary, fontFamily = PoppinsFontFamily, fontSize = 14.sp)
                    } else {
                        Text(text = stringResource(R.string.listing_n_selesai), softWrap = true, textAlign = TextAlign.Center, color = Color.LightGray, fontFamily = PoppinsFontFamily, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                Column {
                    if (wilayah.status == "telah-disampel") {
                        Text(text = stringResource(R.string.telah_n_disampel), softWrap = true, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = PklPrimary, fontFamily = PoppinsFontFamily, fontSize = 14.sp)
                    } else {
                        Text(text = stringResource(R.string.telah_n_disampel), softWrap = true, textAlign = TextAlign.Center, color = Color.LightGray, fontFamily = PoppinsFontFamily, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
//                Column {
//                    if (status === "Sampel Terkirim") {
//                        Text(text = stringResource(R.string.sampel_n_terkirim), softWrap = true, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = PklPrimary, fontFamily = PoppinsFontFamily, fontSize = 14.sp)
//                    } else {
//                        Text(text = stringResource(R.string.sampel_n_terkirim), softWrap = true, textAlign = TextAlign.Center, color = Color.LightGray, fontFamily = PoppinsFontFamily, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
//                    }
//                }
            }
            Row (
                modifier = Modifier.padding(vertical = 8.dp)
            ){
                Divider(color = Color.Gray, thickness = 2.dp, modifier = Modifier
                    .padding(horizontal = 16.dp))
            }
            if (expanded) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column (modifier = Modifier.padding(start = 20.dp)) {
                        Row {
                            Text(
                                text = stringResource(R.string.rekapitulasi),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.padding(3.dp))
                        Row {
                            Text(
                                text = stringResource(R.string.jmlKlg),
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = wilayah.jmlKlg.toString(),
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = PklPrimary
                            )
                        }
                        Spacer(modifier = Modifier.padding(2.dp))
                        Row {
                            Text(
                                text = stringResource(R.string.jmlKlgEgb),
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = wilayah.jmlKlgEgb.toString(),
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = PklPrimary
                            )
                        }
                        Spacer(modifier = Modifier.padding(2.dp))
                        Row {
                            Text(
                                text = stringResource(R.string.jmlRuta),
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = wilayah.jmlRuta.toString(),
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = PklPrimary
                            )
                        }
                        Spacer(modifier = Modifier.padding(2.dp))
                        Row {
                            Text(
                                text = stringResource(R.string.jmlRutaEgb),
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = wilayah.jmlRutaEgb.toString(),
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = PklPrimary
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable {
                        expanded = !expanded
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = stringResource(R.string.toggle),
                        tint = Color.Black,
                        modifier = Modifier
                            .clickable {
                                expanded = !expanded
                            }
                            .padding(16.dp)
                    )
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { onLihatRutaClicked() },
                            shape = MaterialTheme.shapes.small,
                            contentPadding = PaddingValues(10.dp),
                            modifier = Modifier.padding(horizontal = 2.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PklPrimary)) {
                            Text(stringResource(R.string.lihat_ruta), fontFamily = PoppinsFontFamily, fontSize = 13.sp, color = Color.White, textAlign = TextAlign.Center)
                        }
                        if (wilayah.status == "listing" && isMonitoring) {
                            Button(onClick = {
                                showMenu = false
                                openFinalisasiBSDialog = true
                            },
                                shape = MaterialTheme.shapes.small,
                                contentPadding = PaddingValues(10.dp),
                                modifier = Modifier.padding(horizontal = 2.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PklPrimary)) {
                                Text("Finalisasi", fontFamily = PoppinsFontFamily, fontSize = 13.sp, color = Color.White, textAlign = TextAlign.Center)
                            }
                        }
                        if (wilayah.status == "listing-selesai" && isMonitoring) {
                            Button(onClick = {
                                coroutineScope.launch {
                                    val generateRutaJob =
                                        async { viewModel.generateSampel(wilayah.idBS) }
                                    generateRutaJob.await()

                                    viewModel.navigateToListBS.collect { shouldNavigate ->
                                        if (shouldNavigate == true) {
                                            navController.navigate(CapiScreen.Listing.LIST_BS + "/$isMonitoring") {
                                                popUpTo(CapiScreen.Listing.LIST_BS + "/$isMonitoring") {
                                                    inclusive = true
                                                }
                                            }
                                            viewModel.resetNavigateToListBS()
                                        }
                                    }
                                }
                            },
                                shape = MaterialTheme.shapes.small,
                                contentPadding = PaddingValues(10.dp),
                                modifier = Modifier.padding(horizontal = 2.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PklPrimary)) {
                                Text("Ambil Sampel", fontFamily = PoppinsFontFamily, fontSize = 13.sp, color = Color.White, textAlign = TextAlign.Center)
                            }
                        }
                        if (wilayah.status == "telah-disampel") {
                            Button(onClick = {
                                onLihatSampleClicked()
                            },
                                shape = MaterialTheme.shapes.small,
                                contentPadding = PaddingValues(10.dp),
                                modifier = Modifier.padding(horizontal = 2.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PklPrimary)) {
                                Text(stringResource(R.string.lihat_sampel), fontFamily = PoppinsFontFamily, fontSize = 13.sp, color = Color.White, textAlign = TextAlign.Center)
                            }
                        }
                    }

                    if (openFinalisasiBSDialog) {
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
                                                wilayah.idBS?.let { viewModel.finalisasiBS(it) }
                                            }
                                            finalisasiBSJob.await()

                                            viewModel.navigateToListBS.collect { shouldNavigate ->
                                                if (shouldNavigate == true) {
                                                    navController.navigate(CapiScreen.Listing.LIST_BS + "/$isMonitoring") {
                                                        popUpTo(CapiScreen.Listing.LIST_BS + "/$isMonitoring") {
                                                            inclusive = true
                                                        }
                                                    }
                                                    viewModel.resetNavigateToListBS()
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
                    enableFinalisasiBSButton = checkedCheckbox
                }
            }
        }
    }
}

@Composable
private fun ListBS(
    listWilayah: List<WilayahEntity>?,
    navController: NavHostController,
    isMonitoring : Boolean,
    modifier: Modifier = Modifier,
    viewModel: ListBSViewModel
) {
    if (!listWilayah?.isEmpty()!!){
        LazyColumn(
            modifier
                .fillMaxSize()
//            .background(color = PklBase)
                .paint(
                    painter = painterResource(id = R.drawable.pb_bg_background),
                    contentScale = ContentScale.Crop
                )
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            listWilayah?.let { wilayahList ->
                val sortedList = wilayahList.sortedBy { it.noBS }

                items(sortedList.size) { index ->
                    val wilayah = listWilayah[index]
                    BlokSensus(
                        onLihatRutaClicked = {
                            val isListRuta = true
                            navController.navigate(CapiScreen.Listing.LIST_RUTA + "/${wilayah.idBS}/$isMonitoring/$isListRuta")
                        },
                        onLihatSampleClicked = {
                            navController.navigate(CapiScreen.Listing.LIST_SAMPLE + "/${wilayah.idBS}/$isMonitoring")
                        },
                        wilayah = wilayah,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(color = Color.Transparent),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dokumen_hilang),
                    contentDescription = "Empty List Image",
                    modifier = Modifier
                        .size(250.dp, 250.dp)
                        .padding(16.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Tidak Ditemukan!",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }

}

@Preview
@Composable
fun PreviewListBSScreen() {
    Capi63Theme {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            ListBSScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
    }
}

@Preview
@Composable
fun BS() {
    BlokSensus(
        onLihatRutaClicked = { /*TODO*/ },
        onLihatSampleClicked = { /*TODO*/ },
        wilayah = WilayahEntity(
            noBS = "444C",
            namaKab = "Buleleng",
            namaKec = "Kecamatan A",
            namaKel = "Kelurahan B",
            jmlKlg = 0,
            jmlKlgEgb = 0,
            jmlRuta = 0,
            jmlRutaEgb = 0,
            status = "telah-disampel",
            tglListing = Date(),
            tglPeriksa = Date()
        ),
        viewModel = hiltViewModel(),
        navController = rememberNavController()
    )
}
package org.odk.collect.pkl.ui.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.polstat.pkl.viewmodel.AuthViewModel
import com.polstat.pkl.viewmodel.ListBSViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.odk.collect.pkl.navigation.CapiScreen
import java.util.Date

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListBSScreen(
    navController: NavHostController,
    viewModel: ListBSViewModel,
    authViewModel: AuthViewModel
) {

    val listWilayah = viewModel.listWilayahByNIM

    val mahasiswaWithWilayah = viewModel.mahasiswaWithWilayah.collectAsState()

    val context = LocalContext.current

    val session = viewModel.session

    val coroutineScope = rememberCoroutineScope()

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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PklPrimary900
                ),
                modifier = Modifier.shadow(10.dp),
                title = {
                    Text(
                        text = stringResource(R.string.title_list_bs),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        color = Color.White,
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
                                    val lastJob = async { authViewModel.login(session?.nim.toString(), session?.password.toString()) }
                                    lastJob.await()
                                    delay(2000)
                                    navController.navigate(CapiScreen.Listing.LIST_BS){
                                        popUpTo(CapiScreen.Listing.LIST_BS){
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
            ListBS(
                listWilayah = mahasiswaWithWilayah.value.listWilayah,
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        },
    )
}

@Composable
private fun BlokSensus(
    onLihatRutaClicked: () -> Unit,
    onLihatSampleClicked: () -> Unit,
    wilayah: WilayahEntity
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(10.dp)
            .shadow(elevation = 15.dp, shape = RoundedCornerShape(10.dp))
            .background(color = PklBase),
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
                    style = MaterialTheme.typography.titleLarge,
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
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.DarkGray
                        )
                    }
                    Row {
                        Text(
                            text = wilayah.namaKab + ", " + "Bali",
                            style = MaterialTheme.typography.bodyMedium,
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
                Row (verticalAlignment = Alignment.CenterVertically){
                    Column (modifier = Modifier.padding(start = 20.dp)) {
                        Row {
                            Text(
                                text = stringResource(R.string.rekapitulasi),
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
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
//                    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//                        Row {
//                            Text(text = stringResource(R.string.total), fontFamily = PoppinsFontFamily, fontSize = 15.sp, color = Color.Gray)
//                        }
//                        Row {
//                            Text(text = "0", style = MaterialTheme.typography.headlineLarge, color = PklPrimary, fontWeight = FontWeight.Bold)
//                        }
//                    }
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
                    ) {
                        Button(onClick = {
                            onLihatRutaClicked()
                        },
                            shape = MaterialTheme.shapes.small,
                            contentPadding = PaddingValues(10.dp),
                            modifier = Modifier.padding(horizontal = 2.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PklPrimary)) {
                            Text(stringResource(R.string.lihat_ruta), fontFamily = PoppinsFontFamily, fontSize = 13.sp, color = Color.White, textAlign = TextAlign.Center)
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
                }
            }
        }
    }
}

@Composable
private fun ListBS(
    listWilayah: List<WilayahEntity>?,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier
            .fillMaxSize()
            .background(color = PklBase),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        listWilayah?.let { wilayahList ->
            val sortedList = wilayahList.sortedBy { it.noBS }

            sortedList.size.let {
                items(it) { index ->
                    val wilayah = listWilayah[index]
                    BlokSensus(
                        onLihatRutaClicked = {
                            navController.navigate(CapiScreen.Listing.LIST_RUTA + "/${wilayah.noBS}")
                        },
                        onLihatSampleClicked = {
                            navController.navigate(CapiScreen.Listing.LIST_SAMPLE + "/${wilayah.noBS}")
//                            navController.navigate(Capi63Screen.ListSample.route + "/444B")
                        },
                        wilayah = wilayah
                    )
                }
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
            ListBSScreen(navController, viewModel = hiltViewModel(), authViewModel = hiltViewModel())
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
            idKab = "001",
            idKec = "001",
            idKel = "002",
            namaKab = "Buleleng",
            namaKec = "Kecamatan A",
            namaKel = "Kelurahan B",
            catatan = "",
            jmlKlg = 0,
            jmlKlgEgb = 0,
            jmlRuta = 0,
            jmlRutaEgb = 0,
            status = "telah-disampel",
            tglListing = Date(),
            tglPeriksa = Date(),
            nim = ""
        )
    )
}
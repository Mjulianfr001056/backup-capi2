package org.odk.collect.pkl.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.R
import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PklTertiary100
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.viewmodel.ListSampelViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.odk.collect.pkl.navigation.CapiScreen
import org.odk.collect.pkl.ui.screen.components.LoadingDialog


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSampleScreen(
    navController: NavHostController,
    viewModel: ListSampelViewModel
){

    val noBS = viewModel.noBS

    val isMonitoring = viewModel.isMonitoring

    val context = LocalContext.current

    val listSampelRuta = viewModel.listSampelRuta.collectAsState()

    var searchText by remember { mutableStateOf("") }

    var showSearchBar by remember { mutableStateOf(false) }

    val showLoading by viewModel.showLoadingChannel.collectAsState(true)

    val isDataInserted = viewModel.isDataInserted.collectAsState()

    LaunchedEffect(key1 = viewModel.showErrorToastChannel) {
        viewModel.showErrorToastChannel.collectLatest { show ->
            if (show) {
                delay(1500)
                Toast.makeText(context, viewModel.errorMessage.value, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(key1 = isDataInserted.value) {
        viewModel.updateShowLoading(isDataInserted.value)

        viewModel.sampelRutaResponse.collectLatest { response ->
            if (isDataInserted.value) {
//                viewModel.getSampelByBSFromDB(noBS!!)
                viewModel.getSampelByBSFromDB("5104030014007B")
            }
        }
    }

    LoadingDialog(
        showDialog = showLoading
    )

    var isDescending by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PklPrimary900
                ),
                modifier = Modifier.shadow(10.dp),
                title = {
                    Text(
                        text = "List Sample-${noBS}",
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
                            navController.navigate(CapiScreen.Listing.LIST_BS + "/$isMonitoring"){
                                popUpTo(CapiScreen.Listing.LIST_BS + "/$isMonitoring"){
                                    inclusive = false
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            tint = Color.White,
                            contentDescription = "Back Icon",
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
                        IconButton(onClick = { showSearchBar = true }) {
                            Icon(
                                imageVector = Icons.Outlined.Search,
                                contentDescription = "Search Button",
                                tint = Color.White
                            )
                        }
                        val descending = painterResource(R.drawable.descending)
                        val ascending = painterResource(R.drawable.ascending)

                        IconButton(onClick = {
                            isDescending = !isDescending
                        }) {
                            Icon(
                                painter = if (isDescending) ascending else descending,
                                tint = Color.White,
                                contentDescription = "Sort Icon",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        IconButton(onClick = {
//                            navController.navigate(CapiScreen.Listing.LIST_SAMPLE + "/${noBS}")
                            navController.navigate(CapiScreen.Listing.LIST_SAMPLE + "/5104030014007B")
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Sync,
                                tint = Color.White,
                                contentDescription = "Sync Icon",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                }
            )

            if (showSearchBar) {
                Row(modifier = Modifier
                    .fillMaxWidth(),
                    Arrangement.Start,
                    Alignment.CenterVertically) {
                    SearchBar(
                        query = searchText,
                        onQueryChange = {
                            searchText = it
                        },
                        onSearch = {
                            searchText = it

                        },
                        active = false,
                        onActiveChange = { true },
                        placeholder = { Text(
                            text = stringResource(id = R.string.search_list_sampel),
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

                        }
                    )
                }
            }
        },
        content = { innerPadding ->
            ListSample(
                viewModel = viewModel,
                navController = navController,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                listSampelRuta = listSampelRuta.value,
                searchText = searchText,
                context = context,
                isDescending = isDescending
            )
        },
    )
}

@Preview
@Composable
fun PreviewListSampleScreen(){
    ListSampleScreen(
        navController = rememberNavController(),
        viewModel = hiltViewModel()
    )
}

@Preview
@Composable
fun PreviewSample(){
    val context = LocalContext.current
    Sample(
        sampelRuta = SampelRutaEntity(
            kodeRuta = "",
            SLS = "Krapyak",
            noSegmen = "002",
            noBgFisik = "001, 002",
            noBgSensus = "002",
            noUrutKlg = "003",
            noUrutRuta = 2,
            noUrutRutaEgb = 2,
            genzOrtuKeluarga = "2",
            alamat = "Krapyak, Bantul, DIY",
            namaKrt = "KH Ahmad Syamsuri",
            genzOrtuRuta = 2,
            long = 0.0,
            lat = 0.0,
            status = "1"
        ),
        onPetunjukArahClicked = {},
        context = context,
        navController = rememberNavController(),
        viewModel = hiltViewModel()
    )
}

@Composable
private fun ListSample(
    viewModel: ListSampelViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    listSampelRuta: List<SampelRutaEntity>,
    searchText: String,
    context: Context,
    isDescending: Boolean
){
    LazyColumn(
        modifier
            .fillMaxSize()
            .background(color = com.polstat.pkl.ui.theme.PklBase),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        val filteredList = viewModel.filteredList(searchText, listSampelRuta)

        val sortedList = if (isDescending) {
            viewModel.sortedListByRutaDescending(filteredList)
        } else {
            viewModel.sortedListByRutaAscending(filteredList)
        }

        items(sortedList.size) { index ->
            val sampelRuta = sortedList[index]
            Sample(
                onPetunjukArahClicked = {},
                sampelRuta = sampelRuta,
                context,
                navController,
                viewModel
            )
        }

    }
}

@Composable
private fun Sample(
    onPetunjukArahClicked: () -> Unit,
    sampelRuta: SampelRutaEntity,
    context: Context,
    navController: NavHostController,
    viewModel: ListSampelViewModel
){

    var showConfirmDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(10.dp)
            .shadow(elevation = 15.dp, shape = RoundedCornerShape(10.dp))
            .background(color = Color.White),
    ){
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.HomeWork,
                    contentDescription = "Location",
                    tint = PklPrimary900,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "${sampelRuta.namaKrt}",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = PklPrimary900,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SLS : ",
                    color = Color.Gray,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = " ${sampelRuta.SLS}",
                    color = PklPrimary900,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row {
                Text(
                    text = "No. Segmen : ",
                    color = Color.Gray,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = " ${sampelRuta.noSegmen}",
                    color = PklPrimary900,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row(
//                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(0.9f)
                ) {
                    Row {
                        Text(
                            text = "No. BF : ",
                            color = Color.Gray,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = " ${sampelRuta.noBgFisik}",
                            color = PklPrimary900,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Row {
                        Text(
                            text = "No. BS : ",
                            color = Color.Gray,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = " ${sampelRuta.noBgSensus}",
                            color = PklPrimary900,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row {
                        Text(
                            text = "No. Kel : ",
                            color = Color.Gray,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = " ${sampelRuta.noUrutKlg}",
                            color = PklPrimary900,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Row {
                        Text(
                            text = "No. Ruta : ",
                            color = Color.Gray,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = " ${String.format("%03d", sampelRuta.noUrutRuta)}",
                            color = PklPrimary900,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Alamat : ",
                    color = Color.Gray,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = " ${sampelRuta.alamat}",
                    color = PklPrimary900,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Divider(
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp, bottom = 2.dp)
                    .height(1.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kode Eligible : ",
                    color = Color.Gray,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = " ${String.format("%03d", sampelRuta.noUrutRutaEgb)}",
                    color = PklPrimary900,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Column (
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 3.dp)
                ) {
                    Button(
                        onClick = {
                            val latitude = sampelRuta.lat
                            val longitude = sampelRuta.long

                            val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")

                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")

                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(mapIntent)
                            } else {
                                Toast.makeText(context, "Google Maps tidak terinstal.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PklPrimary900)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddLocation,
                            contentDescription = "Location",
                            tint = Color.White,
                            modifier = Modifier
                                .size(22.dp)
                                .padding(3.dp)
                        )
                        Text(
                            text ="PETA",
                            color = Color.White,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 13.sp
                        )
                    }
                }

                if (sampelRuta.status == "1") {
                    Column (
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 3.dp)
                    ) {
                        Button(
                            onClick = {
                                showConfirmDialog = true
                            },
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PklPrimary900)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Location",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(22.dp)
                                    .padding(3.dp)
                            )

                            Text(
                                text ="DICACAH",
                                color = Color.White,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                if (sampelRuta.status == "2") {
                    Column (
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 3.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Location",
                            tint = Color.Green,
                            modifier = Modifier
                                .size(40.dp)
                                .fillMaxWidth()
                        )
                    }
                }

                if (showConfirmDialog) {
                    AlertDialog(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onDismissRequest = { showConfirmDialog = false },
                        confirmButton = {
                            Button(
                                modifier = Modifier.fillMaxWidth(0.45f),
                                onClick = {
                                    viewModel.confirmSampel(sampelRuta.kodeRuta)
                                    showConfirmDialog = false
                                    navController.navigate(CapiScreen.Listing.LIST_SAMPLE + "/${sampelRuta.idBS}"){
                                        popUpTo(CapiScreen.Listing.LIST_SAMPLE + "/${sampelRuta.idBS}"){
                                            inclusive = true
                                        }
                                    }
                                    Toast.makeText(context, "Ruta ${sampelRuta.kodeRuta} berhasil dicacah", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(PklPrimary900)) {
                                Text(text = "Yakin")
                            } },
                        dismissButton = {
                            Button(
                                modifier = Modifier.fillMaxWidth(0.45f),
                                onClick = { showConfirmDialog = false },
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
                    )
                }
            }
        }
    }
}
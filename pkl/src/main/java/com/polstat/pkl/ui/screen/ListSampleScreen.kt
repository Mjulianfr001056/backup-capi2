package com.polstat.pkl.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.database.entity.SampelRutaEntity
import com.polstat.pkl.navigation.Capi63Screen
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.viewmodel.ListSampelViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSampleScreen(navController: NavHostController, viewModel: ListSampelViewModel){

//    val noBS = navController.currentBackStackEntry?.arguments?.getString("noBS")
    val noBS = "444B"

    LaunchedEffect(key1 = true) {
        if (noBS != null) {
            viewModel.getSampelByBS(noBS)
            viewModel.fetchSampelRuta(noBS)
        }
    }

    val listSampelRuta = viewModel.listSampelRuta

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PklPrimary900
                ),
                modifier = Modifier.shadow(10.dp),
                title = {
                    Text(
                        text = "List Sample-026",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        color = Color.White,
                    )
                },
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
                        IconButton(onClick = {
                            /*TODO*/
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                tint = Color.White,
                                contentDescription = "Search Icon",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        IconButton(onClick = {
                            /*TODO*/
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                tint = Color.White,
                                contentDescription = "Menu Icon",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        IconButton(onClick = {
                            /*TODO*/
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
        },
        content = { innerPadding ->
            ListSample(
                navController = navController,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                listSampelRuta = listSampelRuta.value
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

@Composable
private fun ListSample(
    navController: NavController,
    modifier: Modifier = Modifier,
    listSampelRuta: List<SampelRutaEntity>
){
    val itemCount = 4

    LazyColumn(
        modifier
            .fillMaxSize()
            .background(color = com.polstat.pkl.ui.theme.PklBase),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        listSampelRuta.size.let {
            items(it) { index ->
                val sampelRuta = listSampelRuta[index]
                Sample(
                    onPetunjukArahClicked = {},
                    sampelRuta
                )
            }
        }

    }
}

@Composable
private fun Sample(
    onPetunjukArahClicked: () -> Unit,
    sampelRuta: SampelRutaEntity
){
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "No. BS : ",
                        color = Color.Gray,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = " ${sampelRuta.noBS}",
                        color = PklPrimary900,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "No. Urut Ruta : ",
                        color = Color.Gray,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = " ${sampelRuta.noUrutRuta}",
                        color = PklPrimary900,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyMedium
                    )
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
                    text = " ${sampelRuta.noUrutRtEgb}",
                    color = PklPrimary900,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = { /*TODO*/ },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PklPrimary900)
            ) {
                Text(
                    text ="PETUNJUK ARAH",
                    color = Color.White,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 18.sp
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewSample(){
//    Sample(
//        onPetunjukArahClicked = {}
//    )
//}


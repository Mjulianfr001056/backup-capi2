package com.polstat.pkl.ui.screen

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.navigation.Capi63Screen
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary
import com.polstat.pkl.ui.theme.PklPrimary700
import com.polstat.pkl.ui.theme.PoppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListBSScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PklPrimary700
                ),
                modifier = Modifier.shadow(10.dp),
                title = {
                    Text(
                        text = "List Blok Sensus",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        color = Color.White,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Capi63Screen.Sampling.route){
                                popUpTo(Capi63Screen.Sampling.route){
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
                        IconButton(onClick = {}) {
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
            ListBS(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        },
    )
}

@Composable
private fun BlokSensus(
    onLihatRutaClicked: () -> Unit,
    onLihatSampleClicked: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("Sampel Terkirim") }

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
                    text = "Blok Sensus " + "444A",
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
                            text = "Kec. " + "Batu" + ", Kel. " + "Oro-Oro Ombo",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.DarkGray
                        )
                    }
                    Row {
                        Text(
                            text = "Batu" + ", " + "Jawa Timur",
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
                Column () {
                    if (status === "Proses Listing") {
                        Text(text = "Proses\nListing", softWrap = true, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = PklPrimary, fontFamily = PoppinsFontFamily, fontSize = 15.sp)
                    } else {
                        Text(text = "Proses\nListing", softWrap = true, textAlign = TextAlign.Center, color = Color.LightGray, fontFamily = PoppinsFontFamily, fontSize = 13.sp)
                    }

                }
                Column () {
                    if (status === "Listing Selesai") {
                        Text(text = "Listing\nSelesai", softWrap = true, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = PklPrimary, fontFamily = PoppinsFontFamily, fontSize = 15.sp)
                    } else {
                        Text(text = "Listing\nSelesai", softWrap = true, textAlign = TextAlign.Center, color = Color.LightGray, fontFamily = PoppinsFontFamily, fontSize = 13.sp)
                    }
                }
                Column () {
                    if (status === "Telah Disampel") {
                        Text(text = "Telah\nDisampel", softWrap = true, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = PklPrimary, fontFamily = PoppinsFontFamily, fontSize = 15.sp)
                    } else {
                        Text(text = "Telah\nDisampel", softWrap = true, textAlign = TextAlign.Center, color = Color.LightGray, fontFamily = PoppinsFontFamily, fontSize = 13.sp)
                    }
                }
                Column () {
                    if (status === "Sampel Terkirim") {
                        Text(text = "Sampel\nTerkirim", softWrap = true, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = PklPrimary, fontFamily = PoppinsFontFamily, fontSize = 15.sp)
                    } else {
                        Text(text = "Sampel\nTerkirim", softWrap = true, textAlign = TextAlign.Center, color = Color.LightGray, fontFamily = PoppinsFontFamily, fontSize = 13.sp)
                    }
                }
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
                                text = "Rekapitulasi",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.Gray
                            )
                        }
                        Row {
                            Text(
                                text = "Jumlah Rumah Tangga: ",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "3",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = PklPrimary
                            )
                        }
                        Row {
                            Text(
                                text = "Jumlah Rumah Tangga dengan ART Z: ",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "3",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = PklPrimary
                            )
                        }
                        Row (modifier = Modifier.padding(top = 10.dp)) {
                            Text(
                                text = "Gen Z dalam Satu Blok Sensus",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                        Row {
                            Text(
                                text = "Jumlah Gen Z: ",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "3",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = PklPrimary
                            )
                        }
                        Row {
                            Text(
                                text = "Jumlah Gen Z Dewasa: ",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "1",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = PklPrimary
                            )
                        }
                        Row (modifier = Modifier.padding(bottom = 10.dp)) {
                            Text(
                                text = "Jumlah Gen Z Anak: ",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "2",
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = PklPrimary
                            )
                        }
                    }
                    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Row {
                            Text(text = "Total", fontFamily = PoppinsFontFamily, fontSize = 15.sp)
                        }
                        Row {
                            Text(text = "3", style = MaterialTheme.typography.headlineLarge, color = PklPrimary, fontWeight = FontWeight.Bold)
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle",
                        tint = Color.Black,
                        modifier = Modifier
                            .clickable {
                                expanded = !expanded
                            }
                            .padding(16.dp)
                    )
                    Row(){
                        Button(onClick = {
                            onLihatRutaClicked()
                        },
                            shape = MaterialTheme.shapes.small,
                            contentPadding = PaddingValues(10.dp),
                            modifier = Modifier.padding(horizontal = 2.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PklPrimary)) {
                            Text("Lihat Ruta", fontFamily = PoppinsFontFamily, fontSize = 15.sp)
                        }
                        if (status === "Sampel Terkirim") {
                            Button(onClick = {
                                onLihatSampleClicked()
                            },
                                shape = MaterialTheme.shapes.small,
                                contentPadding = PaddingValues(10.dp),
                                modifier = Modifier.padding(horizontal = 2.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PklPrimary)) {
                                Text("Lihat Sampel", fontFamily = PoppinsFontFamily, fontSize = 15.sp)
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
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val itemCount = 2

    LazyColumn (
        modifier
            .fillMaxSize()
            .background(color = PklBase),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top){
        items(itemCount) {
            BlokSensus(
                onLihatRutaClicked = {
                    navController.navigate("list_ruta")
                },
                onLihatSampleClicked = {}
            )
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
            ListBSScreen(navController)
        }
    }
}
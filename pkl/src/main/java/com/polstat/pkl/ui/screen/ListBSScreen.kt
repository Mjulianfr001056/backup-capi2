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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
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
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary
import com.polstat.pkl.ui.theme.PklPrimary700

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListBSScreen() {
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
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            tint = Color.White,
                            contentDescription = ""
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
                                imageVector = Icons.Filled.Refresh,  //masih sementara
                                tint = Color.White,
                                contentDescription = ""
                            )
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            ListBS(Modifier.padding(innerPadding))
        },
    )
}

@Composable
private fun BlokSensus() {
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
                    style = MaterialTheme.typography.headlineLarge,
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
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.DarkGray
                        )
                    }
                    Row {
                        Text(
                            text = "Batu" + ", " + "Jawa Timur",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp),
                            color = Color.DarkGray
                        )
                    }
                }
            }
            Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 20.dp)) {
                Column () {
                    if (status === "Proses Listing") {
                        Text(text = "Proses\nListing", softWrap = true, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = PklPrimary)
                    } else {
                        Text(text = "Proses\nListing", softWrap = true, textAlign = TextAlign.Center, color = Color.LightGray)
                    }

                }
                Column () {
                    if (status === "Listing Selesai") {
                        Text(text = "Listing\nSelesai", softWrap = true, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = PklPrimary)
                    } else {
                        Text(text = "Listing\nSelesai", softWrap = true, textAlign = TextAlign.Center, color = Color.LightGray)
                    }
                }
                Column () {
                    if (status === "Telah Disampel") {
                        Text(text = "Telah\nDisampel", softWrap = true, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = PklPrimary)
                    } else {
                        Text(text = "Telah\nDisampel", softWrap = true, textAlign = TextAlign.Center, color = Color.LightGray)
                    }
                }
                Column () {
                    if (status === "Sampel Terkirim") {
                        Text(text = "Sampel\nTerkirim", softWrap = true, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = PklPrimary)
                    } else {
                        Text(text = "Sampel\nTerkirim", softWrap = true, textAlign = TextAlign.Center, color = Color.LightGray)
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
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Gray
                            )
                        }
                        Row {
                            Text(
                                text = "Jumlah Rumah Tangga: ",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = "3",
                                style = MaterialTheme.typography.bodySmall,
                                color = PklPrimary
                            )
                        }
                        Row {
                            Text(
                                text = "Jumlah ART yang mengelola UUP: ",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = "3",
                                style = MaterialTheme.typography.bodySmall,
                                color = PklPrimary
                            )
                        }
                        Row (modifier = Modifier.padding(top = 10.dp)) {
                            Text(
                                text = "Jumlah UUP tiap Jenis",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Row {
                            Text(
                                text = "Jasa Transportasi Wisata: ",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = "0",
                                style = MaterialTheme.typography.bodySmall,
                                color = PklPrimary
                            )
                        }
                        Row {
                            Text(
                                text = "Jasa Penyedia Makanan dan Minuman: ",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = "1",
                                style = MaterialTheme.typography.bodySmall,
                                color = PklPrimary
                            )
                        }
                        Row (modifier = Modifier.padding(bottom = 10.dp)) {
                            Text(
                                text = "Penyedia Akomodasi: ",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = "2",
                                style = MaterialTheme.typography.bodySmall,
                                color = PklPrimary
                            )
                        }
                    }
                    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Row {
                            Text(text = "Total")
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
                        Button(onClick = {},
                            shape = MaterialTheme.shapes.small,
                            contentPadding = PaddingValues(10.dp),
                            modifier = Modifier.padding(horizontal = 2.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PklPrimary)) {
                            Text("Lihat Ruta")
                        }
                        if (status === "Sampel Terkirim") {
                            Button(onClick = {},
                                shape = MaterialTheme.shapes.small,
                                contentPadding = PaddingValues(10.dp),
                                modifier = Modifier.padding(horizontal = 2.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PklPrimary)) {
                                Text("Lihat Sampel")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ListBS(modifier: Modifier = Modifier) {
    val itemCount = 2

    LazyColumn (
        modifier
            .fillMaxSize()
            .background(color = PklBase),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top){
        items(itemCount) {
            BlokSensus()
        }
    }
}

@Preview
@Composable
fun PreviewListBSScreen() {
    Capi63Theme {
        ListBSScreen()
    }
}
package com.polstat.pkl.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.ui.theme.PklPrimary900

@Preview(showBackground = true)
@Composable
fun BerandaScreenPreview(){
    BerandaScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BerandaScreen(){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "BERANDA",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PklPrimary900,
                    titleContentColor = Color.White,
                ),
                actions = {
                    IconButton(
                        onClick = { /* Handle navigation icon click */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = PklPrimary900,
                modifier = Modifier.height(64.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column{
                        IconButton(
                            onClick = { /* Handle navigation icon click */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Sampling",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = "Listing",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    Column {
                        IconButton(
                            onClick = { /* Handle navigation icon click */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Beranda",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = "Beranda",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                    Column {
                        IconButton(
                            onClick = { /* Handle navigation icon click */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Kuesioner",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = "Kuesioner",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(PklBase)
                .verticalScroll(rememberScrollState())
        ) {
            // First Card
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .shadow(8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.Gray,
                ),
                border = BorderStroke(1.dp, Color.White),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        tint = PklPrimary900,
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(80.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = "Nama Mahasiswa",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "NIM Mahasiswa",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "   |   ",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "PPL/PCL",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Tim Dummy Modul 1",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = " - ",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "Nomor Tim",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Second Card - Add another Card element here
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .shadow(8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = PklPrimary900,
                    contentColor = Color.Gray,
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Column{
                    Text(
                        text = "PML",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .padding(12.dp)
                    ){
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Nama PML",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "NIM PML",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Second Card - Add another Card element here
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .shadow(8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = PklPrimary900,
                    contentColor = Color.Gray,
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    //modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "WILAYAH KERJA",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .padding(12.dp)
                    ){
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column {
                                Text(
                                    text = "1",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 30.sp,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = "Kelurahan",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Divider(
                                color = Color.LightGray,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(2.dp)
                            )
                            Column {
                                Text(
                                    text = "0",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 30.sp,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = "Titik Sampel",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .shadow(8.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = PklPrimary900,
                    contentColor = Color.Gray,
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    //modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "STATUS LISTING",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .padding(12.dp)
                    ){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column{
                                Text(
                                    text = "333E",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "KEC. Bantur",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "KEC. Bandungrejo",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Column(
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = "Listing",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
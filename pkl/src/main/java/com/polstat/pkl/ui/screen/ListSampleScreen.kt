package com.polstat.pkl.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.polstat.pkl.navigation.Capi63Screen
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSampleScreen(navController: NavController){
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
                    .fillMaxSize()
            )
        },
    )
}

@Preview
@Composable
fun PreviewListSampleScreen(){
    ListSampleScreen(
        navController = NavController(LocalContext.current)
    )
}

@Composable
private fun ListSample(
    navController: NavController,
    modifier: Modifier = Modifier
){
    val itemCount = 4

    LazyColumn(
        modifier
            .fillMaxSize()
            .background(color = com.polstat.pkl.ui.theme.PklBase),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){ items(itemCount) {
            Sample(
                onPetunjukArahClicked = {}
            )
        }
    }
}

@Composable
private fun Sample(
    onPetunjukArahClicked: () -> Unit,
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
                    text = "DANANG",
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
                        text = " 001",
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
                        text = " S001",
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
                        text = " 001",
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
                        text = " 001",
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
                    text = " RT 001/RW 001",
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
                    text = " 1",
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

@Preview(showBackground = true)
@Composable
fun PreviewSample(){
    Sample(
        onPetunjukArahClicked = {}
    )
}


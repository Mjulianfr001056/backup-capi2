package com.polstat.pkl.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.R
import com.polstat.pkl.ui.screen.components.BottomBar
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SamplingScreen(navController: NavHostController, isPml: Boolean) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SAMPLING MANAGEMENT",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PklPrimary900,
                    titleContentColor = Color.White,
                ),
                actions = {}
            )
        },
        bottomBar = {
            BottomBar(navController = navController)
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(color = PklBase),
                contentAlignment = Alignment.Center
            ) {
                HorizontalMenu(
                    navController = navController,
                    isPml = isPml
                )
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HorizontalMenu(navController: NavHostController, isPml: Boolean) {
    Column {
        FlowRow(modifier = Modifier.padding(4.dp)) {
            if (isPml) {
                MenuButton(
                    image = painterResource(R.drawable.listing),
                    name = "Listing",
                    onCardClicked = {
                        navController.navigate("list_bs")
                    }
                )
                Spacer(modifier = Modifier.width(30.dp))
                MenuButton(
                    image = painterResource(R.drawable.password_master),
                    name = "Password Master",
                    onCardClicked = {
                        navController.navigate("password")
                    }
                )
            } else {
                MenuButton(
                    image = painterResource(R.drawable.listing),
                    name = "Listing",
                    onCardClicked = {
                        navController.navigate("list_bs")
                    }
                )
            }
        }
    }
}

@Composable
fun MenuButton(image: Painter, name: String, onCardClicked: () -> Unit) {
    Card(
        modifier = Modifier
            .size(160.dp, 134.dp)
            .padding(4.dp)
            .clickable {
                onCardClicked()
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 200.dp
        ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
    ) {
        Row(
            modifier = Modifier
                .background(
                    color = PklPrimary900,
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
                .fillMaxWidth()
                .height(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    modifier = Modifier
                        .size(18.dp),
                    contentDescription = "More",
                    tint = Color.White
                )
            }
            Row {
                Icon(
                    imageVector = Icons.Default.Close,
                    modifier = Modifier
                        .size(15.dp),
                    contentDescription = "Close",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(5.dp))
            }

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .size(100.dp)
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            Image(
                painter = image,
                contentDescription = name,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = name,
                fontSize = 12.sp,
                color = PklPrimary900
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        )
        {
            Row(
                modifier = Modifier
                    .background(
                        color = PklPrimary900,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .fillMaxWidth(0.75F)
                    .height(3.dp)
            ){}
        }
    }
}

@Preview
@Composable
fun SamlingScreenPreview () {
    Capi63Theme {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            SamplingScreen(navController, isPml = true)
        }
    }
}
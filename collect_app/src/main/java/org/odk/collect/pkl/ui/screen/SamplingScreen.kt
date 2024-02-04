package org.odk.collect.pkl.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.R
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.viewmodel.SamplingViewModel
import org.odk.collect.pkl.ui.screen.components.BottomNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SamplingScreen(
    rootController: NavHostController,
    navController: NavHostController,
    viewModel: SamplingViewModel
) {
    val session = viewModel.session

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
            BottomNavBar(rootNavController = rootController)
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
                    isPml = session!!.isKoor
                )
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HorizontalMenu(navController: NavHostController, isPml: Boolean?) {
    Column {
        FlowRow(modifier = Modifier.padding(4.dp)) {
            if (isPml == true) {
                MenuButton(
                    image = painterResource(R.drawable.listing),
                    name = "Listing",
                    onCardClicked = {
                        val isMonitoring = false
                        navController.navigate("list_bs/$isMonitoring")
                    }
                )
                Spacer(modifier = Modifier.width(30.dp))
//                MenuButton(
//                    image = painterResource(R.drawable.password_master),
//                    name = "Password Master",
//                    onCardClicked = {
//                        navController.navigate("password")
//                    }
//                )
                MenuButton(
                    image = painterResource(R.drawable.password_master),
                    name = "Monitoring",
                    onCardClicked = {
                        val isMonitoring = true
                        navController.navigate("list_bs/$isMonitoring")
                    }
                )
            } else {
                MenuButton(
                    image = painterResource(R.drawable.listing),
                    name = "Listing",
                    onCardClicked = {
                        val isMonitoring = false
                        navController.navigate("list_bs/$isMonitoring")
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
            containerColor = Color.Transparent,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = image,
                contentDescription = name,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = name,
                fontSize = 12.sp,
                color = PklPrimary900
            )
        }
    }
}

@Preview
@Composable
fun SamplingScreenPreview () {
    Capi63Theme {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            SamplingScreen(navController, navController, hiltViewModel())
        }
    }
}
package org.odk.collect.pkl.ui.screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.navigation.Capi63Screen
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.viewmodel.PasswordMasterViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordMasterScreen(
    navController: NavHostController,
    passwordMasterViewModel: PasswordMasterViewModel
) {
    val randomString by passwordMasterViewModel.randomString.collectAsState()
    val countdown by passwordMasterViewModel.countdown.collectAsState()
    val showToast = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "PASSWORD MASTER",
                        style = TextStyle(
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PklPrimary900,
                    titleContentColor = PklBase,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Capi63Screen.Sampling.route) {
                                popUpTo(Capi63Screen.Sampling.route) {
                                    inclusive = true
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            tint = PklBase,
                            contentDescription = "Back Icon",
                            modifier = Modifier.size(25.dp)
                        )
                    }
                },
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(color = PklBase),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier
                        .size(width = 340.dp, height = 290.dp)
                        .padding(
                            top = 40.dp,
                            start = 1.dp,
                            end = 1.dp
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = PklPrimary900
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .size(340.dp, 80.dp)
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.WarningAmber,
                            tint = PklBase,
                            contentDescription = "Warning",
                            modifier = Modifier.size(50.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = "Hanya PML yang boleh mengetahui password ini.",
                            style = TextStyle(
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            ),
                            color = PklBase
                        )
                    }
                    Column(
                        modifier = Modifier
                            .size(340.dp, 120.dp)
                            .background(
                                color = PklBase
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        ContentCard(
                            password = randomString,
                            countdown = "${
                                String.format(
                                    "%02d:%02d",
                                    countdown / 60,
                                    countdown % 60
                                )
                            }"
                        )
                    }
                    Row(
                        modifier = Modifier
                            .size(340.dp, 50.dp)
                            .clickable(
                                onClick = {
                                    val clip = ClipData.newPlainText("randomString", randomString)
                                    clipboardManager.setPrimaryClip(clip)
                                    showToast.value = true
                                }
                            ),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SALIN PASSWORD",
                            style = TextStyle(
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            ),
                            color = PklBase
                        )
                    }
                }
            }
            if (showToast.value) {
                Toast.makeText(LocalContext.current, "Password disalin", Toast.LENGTH_SHORT).show()
                showToast.value = false
            }
        }
    )
}

@Composable
fun ContentCard(
    password: String,
    countdown: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = password,
            style = TextStyle(
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 40.sp,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            modifier = Modifier.padding(
                top = 15.dp
            ),
            color = PklPrimary900
        )
        Text(
            text = "Password akan direset dalam ${countdown}",
            style = TextStyle(
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Light,
                fontSize = 16.sp,
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            modifier = Modifier.padding(
                bottom = 15.dp
            ),
            color = Color.DarkGray
        )
    }
}

@Preview
@Composable
fun PasswordMasterScreenPreview() {
    Capi63Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            val passwordMasterViewModel: PasswordMasterViewModel = viewModel()

            PasswordMasterScreen(navController = navController, passwordMasterViewModel = passwordMasterViewModel)
        }
    }
}
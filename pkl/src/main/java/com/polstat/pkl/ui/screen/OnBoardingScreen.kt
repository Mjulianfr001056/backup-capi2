package com.polstat.pkl.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.polstat.pkl.navigation.Capi63Screen
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklQuaternary
import com.polstat.pkl.ui.theme.PklSecondary


@Composable
fun OnBoardingScreen(
    navController: NavHostController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(color = PklBase)
            .wrapContentHeight()
    ) {
        Image(
            painter = painterResource(id = com.polstat.pkl.R.drawable.pb_login2),
            contentDescription = "",
            modifier = Modifier.size(360.dp),
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ){
            Text(
                text = "Selamat datang!",
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold,
                color = PklSecondary
            )
            Spacer(
                modifier = Modifier.height(10.dp)
            )
            Text(
                text = "Aplikasi ini merupakan aplikasi pendukung kegiatan PKL D-IV angkatan 63",
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(end = 50.dp),
                fontSize = 13.sp
            )
            Text(
                text = "Silakan login untuk melanjutkan",
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(48.dp),
                    onClick = {
                        navController.navigate(Capi63Screen.Login.route)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PklSecondary
                    ),
                    shape = RoundedCornerShape(15.dp),
                ){
                    Row{
                        Text(
                            text = "Masuk sekarang!",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 45.dp)
            ){
                Text(
                    text = "Created by Tim CAPI, SPD PKL 63",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = PklQuaternary
                )
            }

        }
        //Spacer(modifier = Modifier.height(80.dp))
        //LogoTitle()
        //Spacer(modifier = Modifier.height(20.dp))
//        Card(
//            modifier = Modifier
//                .size(270.dp, 340.dp)
//                .padding(bottom = 15.dp)
//                .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp)),
//            shape = RoundedCornerShape(20.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = PklPrimary900
//            ),
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.height(310.dp)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.AccountCircle,
//                    contentDescription = "User Icon",
//                    tint = Color.White,
//                    modifier = Modifier.size(48.dp)
//                )
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(
//                            color = Color.White,
//                            shape = RoundedCornerShape(20.dp)
//                        )
//                ) {
//                    Column(
//                        modifier = Modifier.padding(15.dp)
//                    ) {
//                        NimTextField(
//                            value = state.nim,
//                            onValueChange = {
//                                viewModel.onEvent(LoginScreenEvent.NimChanged(it))
//                            },
//                            errorMessage = state.nimError
//                        )
//                        PasswordTextField(
//                            value = state.password,
//                            onValueChange = {
//                                viewModel.onEvent(LoginScreenEvent.PasswordChanged(it))
//                            },
//                            errorMessage = state.passwordError,
//                        )
//
//                        Spacer(modifier = Modifier.height(15.dp))
//                        LoginButton(
//                            onClick = {
//                                isLoginProcess = true
//                                viewModel.onEvent(LoginScreenEvent.submit)
//                            }
//                        )
//                    }
//                }
//            }
//        }
    }
}
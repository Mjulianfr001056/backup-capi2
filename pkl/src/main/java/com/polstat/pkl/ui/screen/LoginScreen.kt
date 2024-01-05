package com.polstat.pkl.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.polstat.pkl.R
import com.polstat.pkl.ui.state.NimState
import com.polstat.pkl.ui.state.NimStateSaver
import com.polstat.pkl.ui.state.PasswordState
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.viewmodel.LoginViewModel


@Composable
fun LoginScreen(navController: NavHostController, loginViewModel: LoginViewModel) {
    val focusRequester = remember { FocusRequester() }
    val nimState by rememberSaveable(stateSaver = NimStateSaver) {
        mutableStateOf(NimState(""))
    }
    val passwordState = remember { PasswordState() }
    val context = LocalContext.current
    val loginState by loginViewModel.loginState.observeAsState()

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        LogoTitle()
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier
                .size(270.dp, 340.dp)
                .padding(bottom = 15.dp)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = PklPrimary900
            ),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.height(310.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "User Icon",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Column (
                        modifier = Modifier.padding(15.dp)
                    ) {
                        Nim(nimState, onImeAction = { focusRequester.requestFocus() })
                        Password(
                            label = stringResource(id = R.string.password),
                            passwordState = passwordState,
                            modifier = Modifier.focusRequester(focusRequester),
                            onImeAction = {}
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        LoginButton(onClick = {
//                            loginViewModel.login(nimState.text, passwordState.text)
                            navController.navigate("beranda")
                        })
                        // Menangani state dari login
                        loginState?.let {
//                            if (it.nim != "") {
                                navController.navigate("beranda")
//                            }
                        }
                    }
                }
            }
        }
    }
}

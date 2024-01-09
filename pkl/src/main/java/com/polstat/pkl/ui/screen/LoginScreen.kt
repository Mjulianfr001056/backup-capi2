package com.polstat.pkl.ui.screen

import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.polstat.pkl.navigation.Capi63Screen
import com.polstat.pkl.ui.event.LoginScreenEvent
import com.polstat.pkl.ui.screen.components.LoadingDialog
import com.polstat.pkl.ui.screen.components.LoginButton
import com.polstat.pkl.ui.screen.components.LogoTitle
import com.polstat.pkl.ui.screen.components.NimTextField
import com.polstat.pkl.ui.screen.components.PasswordTextField
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest


@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthViewModel
) {
    val focusRequester = remember { FocusRequester() }
//    val nimState by rememberSaveable(stateSaver = NimStateSaver) {
//        mutableStateOf(NimState(""))
//    }
//    val passwordState = remember { PasswordState() }
    val state = viewModel.state
    val context = LocalContext.current
    var isLoginProcess by remember { mutableStateOf(false) }
    val showLoading by viewModel.showLoadingChannel.collectAsState(false)

    LaunchedEffect(key1 = viewModel.showErrorToastChannel) {
        viewModel.showErrorToastChannel.collectLatest { show ->
            if (show) {
                isLoginProcess = false
                delay(1500)
                Toast.makeText(context, viewModel.errorMessage.value, Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(viewModel.authResponse) {
        viewModel.authResponse.collectLatest { response ->
            if (response.status == "success") {
                isLoginProcess = false
                delay(1000)
                Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                navController.navigate(Capi63Screen.Beranda.route)
            }
        }
    }

    LoadingDialog(
        showDialog = showLoading
    )

    Column(
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
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {
                        NimTextField(
                            value = state.nim,
                            onValueChange = {
                                viewModel.onEvent(LoginScreenEvent.NimChanged(it))
                            },
                            errorMessage = state.nimError,
                            onImeAction = { focusRequester.requestFocus() }
                        )
                        PasswordTextField(
                            value = state.password,
                            onValueChange = {
                                viewModel.onEvent(LoginScreenEvent.PasswordChanged(it))
                            },
                            errorMessage = state.passwordError,
                            modifier = Modifier.focusRequester(focusRequester),
                        )

                        Spacer(modifier = Modifier.height(15.dp))
                        LoginButton(
                            onClick = {
                                isLoginProcess = true
                                viewModel.onEvent(LoginScreenEvent.submit)
                            }
                        )
                    }
                }
            }
        }
    }
}
package org.odk.collect.pkl.ui.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.polstat.pkl.R
import com.polstat.pkl.ui.event.LoginScreenEvent
import com.polstat.pkl.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.odk.collect.pkl.navigation.CapiScreen
import org.odk.collect.pkl.ui.screen.components.LoadingDialog
import org.odk.collect.pkl.ui.screen.components.LoginButton
import org.odk.collect.pkl.ui.screen.components.LogoTitle
import org.odk.collect.pkl.ui.screen.components.NimTextField
import org.odk.collect.pkl.ui.screen.components.PasswordTextField

//@Preview(showBackground = true, apiLevel = 28)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
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
            println("Login screen: $isLoginProcess")
            if (response.status == "success" && isLoginProcess) {
                isLoginProcess = false
                delay(1000)
                Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show()
                navController.navigate(CapiScreen.Top.MAIN){
                    popUpTo(CapiScreen.Top.AUTH){
                        inclusive = true
                    }
                }
            }
        }
    }

    LoadingDialog(
        showDialog = showLoading
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.pb_bg_login),
                contentScale = ContentScale.Crop
            )
            .padding(bottom = 150.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoTitle()
        Spacer(modifier = Modifier.padding(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NimTextField(
                value = state.nim,
                onValueChange = {
                    viewModel.onEvent(LoginScreenEvent.NimChanged(it))
                                },
                errorMessage = state.nimError
            )
            Spacer(modifier = Modifier.height(10.dp))
            PasswordTextField(
                value = state.password,
                onValueChange = {
                    viewModel.onEvent(LoginScreenEvent.PasswordChanged(it))
                },
                errorMessage = state.passwordError,
            )
            Spacer(modifier = Modifier.height(20.dp))
            LoginButton(
                onClick = {
                    isLoginProcess = true
                    viewModel.onEvent(LoginScreenEvent.submit)
                }
            )
        }
    }
}
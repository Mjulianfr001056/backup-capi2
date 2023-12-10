package com.polstat.pkl.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.polstat.pkl.R
import com.polstat.pkl.ui.state.NimState
import com.polstat.pkl.ui.state.NimStateSaver
import com.polstat.pkl.ui.state.PasswordState
import com.polstat.pkl.ui.state.TextFieldState
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PklQuaternary
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.ui.theme.typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Nim(
    nimState: TextFieldState = remember { NimState() },
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    Column {
        val characterCount = remember { mutableStateOf(0) }

        OutlinedTextField(
            value = nimState.text,
            onValueChange = {
                if (it.matches(Regex("\\d*"))) {
                    if (it.length <= 9) {
                        nimState.text = it
                        characterCount.value = it.length
                    }
                }
            },
            label = {
                Text(
                    text = stringResource(id = R.string.nim),
                    style = typography.titleSmall,
                    color = if (nimState.showErrors()) PklPrimary900 else PklQuaternary
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 0.dp)
                .onFocusChanged { focusState ->
                    nimState.onFocusChange(focusState.isFocused)
                    if (!focusState.isFocused) {
                        nimState.enableShowErrors()
                    }
                },
            colors = outlinedTextFieldColors(
                unfocusedBorderColor = PklQuaternary,
//                    textColor = PklPrimary900,
                cursorColor = PklPrimary900
            ),
            textStyle = typography.bodyMedium,
            isError = nimState.showErrors(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(onDone = {
                onImeAction()
            }),
            singleLine = true
        )
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.wajib),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.W600,
                fontSize = 10.sp,
                color = PklQuaternary
            )
            Text(
                text = "${characterCount.value}/9",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.W600,
                fontSize = 10.sp,
                color = PklQuaternary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Password(
    label: String,
    passwordState: TextFieldState,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    Column {
        val showPassword = rememberSaveable { mutableStateOf(false) }
        OutlinedTextField(value = passwordState.text, onValueChange = {
            passwordState.text = it
            passwordState.enableShowErrors()
        }, modifier = modifier
            .fillMaxWidth()
            .padding(all = 0.dp)
            .onFocusChanged { focusState ->
                passwordState.onFocusChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    passwordState.enableShowErrors()
                }
            }, textStyle = TextStyle(
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp,
        ), label = {
            Text(
                text = label,
                style = typography.titleSmall,
                color = if (passwordState.showErrors()) PklPrimary900 else PklQuaternary
            )
        }, colors = outlinedTextFieldColors(
            unfocusedBorderColor = PklQuaternary,
//            textColor = PklPrimary900,
            cursorColor = PklPrimary900
        ),
            trailingIcon = {
                if (showPassword.value) {
                    IconButton(onClick = { showPassword.value = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = stringResource(id = R.string.hide_password),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                } else {
                    IconButton(onClick = { showPassword.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = stringResource(id = R.string.show_password),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }, visualTransformation = if (showPassword.value) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            }, isError = passwordState.showErrors(), supportingText = {
                passwordState.getError()?.let { error -> TextFieldError(textError = error) }
            }, keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction, keyboardType = KeyboardType.Password
            ), keyboardActions = KeyboardActions(onDone = {
                onImeAction()
            }), singleLine = true
        )
        Text(
            text = stringResource(id = R.string.wajib),
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.W600,
            fontSize = 10.sp,
            color = PklQuaternary
        )
    }
}

@Composable
fun LogoTitle() {
    Row(
        modifier = Modifier.wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_pkl_63),
            contentDescription = "Logo PKL 63",
            modifier = Modifier.size(100.dp, 100.dp)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Column() {
            Text(
                text = stringResource(id = R.string.capi),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.W400,
                fontSize = 28.sp,
                lineHeight = 0.sp,
                color = PklPrimary900,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
            Text(
                text = stringResource(id = R.string.pkl_63),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.W400,
                fontSize = 28.sp,
                lineHeight = 0.em,
                color = PklPrimary900,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }
    }
}

/**
 * To be removed when [TextField]s support error
 */
@Composable
fun TextFieldError(textError: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun LoginButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(240.dp, 45.dp),
        contentPadding = PaddingValues(4.dp),
        shape = RoundedCornerShape(10.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(PklPrimary900)
                    .padding(start = 10.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login),
                    contentDescription = "Login",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.White, shape = RoundedCornerShape(
                            topEnd = 10.dp, bottomEnd = 10.dp
                        )
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.login),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp,
                    letterSpacing = 5.sp,
                    color = PklQuaternary
                )
            }
        }
    }
}

@Preview
@Composable
fun LogoTitlePreview() {
    Capi63Theme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            LogoTitle()
        }
    }
}

@Preview
@Composable
fun NimPreview() {
    val focusRequester = remember { FocusRequester() }
    val nimState by rememberSaveable(stateSaver = NimStateSaver) {
        mutableStateOf(NimState(""))
    }

    Capi63Theme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Nim(nimState = nimState, onImeAction = { focusRequester.requestFocus() })
        }
    }
}

@Preview
@Composable
fun PasswordPreview() {
    val passwordState = remember { PasswordState() }
    val focusRequester = remember { FocusRequester() }

    Capi63Theme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Password(label = stringResource(id = R.string.password),
                passwordState = passwordState,
                modifier = Modifier.focusRequester(focusRequester),
                onImeAction = {})
        }
    }
}

@Preview
@Composable
fun LoginButtonPreview() {
    Capi63Theme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            LoginButton(onClick = {})
        }
    }
}

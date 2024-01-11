package com.polstat.pkl.ui.screen.components

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PklQuaternary
import com.polstat.pkl.ui.theme.PklSecondary
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.ui.theme.typography

@Preview(showBackground = true, apiLevel = 28)
@Composable
fun NimTextField(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    errorMessage: String? = null,
) {

    Column {
        val characterCount = remember { mutableIntStateOf(0) }
        val isError: Boolean = errorMessage.isNullOrBlank()

        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            label = {
                Text(
                    text = stringResource(id = R.string.nim),
                    style = typography.titleSmall,
                    color = if (isError) PklPrimary900 else PklSecondary
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 0.dp),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = PklPrimary900,
                focusedTextColor = PklPrimary900,
                cursorColor = PklPrimary900,
            ),
            textStyle = typography.bodyMedium,
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            supportingText = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = errorMessage ?: "",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.W600,
                        fontSize = 10.sp,
                        color = PklPrimary900
                    )
                    Text(
                        text = "${characterCount.intValue}/9",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.W600,
                        fontSize = 10.sp,
                        color = PklQuaternary
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true, apiLevel = 28)
@Composable
fun PasswordTextField(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    errorMessage: String? = null,
) {
    val isError: Boolean = errorMessage.isNullOrBlank()

    Column {
        val showPassword = rememberSaveable { mutableStateOf(false) }
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 0.dp),
            textStyle = TextStyle(
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp
            ),
            label = {
                Text(
                    text = stringResource(id = R.string.password),
                    style = typography.titleSmall,
                    color = if (isError) PklPrimary900 else PklQuaternary
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = PklPrimary900,
                focusedTextColor = PklPrimary900,
                cursorColor = PklPrimary900,
            ),
            shape = RoundedCornerShape(10.dp),
            trailingIcon = {
                if (showPassword.value) {
                    IconButton(
                        onClick = {
                            showPassword.value = false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = stringResource(id = R.string.hide_password),
                            modifier = Modifier.size(15.dp)
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            showPassword.value = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = stringResource(id = R.string.show_password),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } },
            visualTransformation =
            if (showPassword.value)
                VisualTransformation.None
            else PasswordVisualTransformation(),
            isError = isError,
            supportingText = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ){
                    Text(
                        text = errorMessage ?: "",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.sp,
                        color = PklPrimary900
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password),
            singleLine = true
        )
    }
}

@Preview(showBackground = true, apiLevel = 28)
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
        Column {
            Text(
                text = stringResource(id = R.string.capi),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.W400,
                fontSize = 28.sp,
                lineHeight = 0.sp,
                color = PklSecondary,
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
                color = PklSecondary,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }
    }
}

@Preview(showBackground = true, apiLevel = 28)
@Composable
fun LoginButton(
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(210.dp, 35.dp),
        contentPadding = PaddingValues(5.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PklSecondary
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .background(PklSecondary),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.login),
                    contentDescription = "Login",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = PklBase,
                        shape = RoundedCornerShape(
                            topEnd = 10.dp, bottomEnd = 10.dp
                        )
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Masuk",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = PklSecondary
                )
            }
        }
    }
}



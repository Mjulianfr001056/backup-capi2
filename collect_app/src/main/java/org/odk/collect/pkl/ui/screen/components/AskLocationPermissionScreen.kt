package org.odk.collect.pkl.ui.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.R
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklSecondary
import com.polstat.pkl.viewmodel.BerandaViewModel

@Composable
fun AskLocationPermissionScreen(
    viewModel: BerandaViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.pb_bg_background),
                contentScale = ContentScale.Crop
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Eits, harus nyalain location permission dulu!",
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            color = PklSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.allow_your_location),
            contentDescription = "Allow Your Location Image",
            modifier = Modifier.size(300.dp),
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Text(
            text = "Cara mengaktifkannya",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Masuk ke Pengaturan > Aplikasi & Perizinan > Capi PKL 63 > Perizinan > Lokasi",
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(horizontal = 20.dp),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun AskLocationPermissionScreenPreview() {
    Capi63Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AskLocationPermissionScreen(viewModel = hiltViewModel())
        }
    }
}
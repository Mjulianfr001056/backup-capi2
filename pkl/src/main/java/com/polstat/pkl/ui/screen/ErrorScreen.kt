package com.polstat.pkl.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorScreen(errorMessage: String) = // Tampilan untuk screen gagal
    Column {
        Text("Login gagal")
        Text(errorMessage)
    }


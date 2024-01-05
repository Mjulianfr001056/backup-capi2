package com.polstat.pkl.ui.navigation

import android.media.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.polstat.pkl.R

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        name = "Sampling",
        route = "sampling",
        icon = Icons.Default.List
    ),
    BottomNavItem(
        name = "Beranda",
        route = "beranda",
        icon = Icons.Default.Home
    ),
    BottomNavItem(
        name = "Kuesioner",
        route = "kuesioner",
        icon = Icons.Default.Assignment
    )
)
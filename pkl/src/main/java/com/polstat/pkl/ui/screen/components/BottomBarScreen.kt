package com.polstat.pkl.ui.screen.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.polstat.pkl.R

@Deprecated("Akan diganti")
sealed class BottomBarScreen (
    val route: String,
    val title: String,
    val image: @Composable () -> Painter
) {
    object sampling: BottomBarScreen(
        route = "sampling",
        title = "Sampling",
        image = { painterResource(id = R.drawable.ic_numbered_list) }
    )

    object beranda: BottomBarScreen(
        route = "beranda",
        title = "Beranda",
        image = { painterResource(id = R.drawable.ic_home) }
    )

    object kuesioner: BottomBarScreen(
        route = "kuesioner",
        title = "Kuesioner",
        image = { painterResource(id = R.drawable.ic_clipboard) }
    )
}

data class BottomNavigationItem(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
)

val items = listOf(
    BottomNavigationItem(
        title = "sampling",
        unselectedIcon = Icons.Outlined.List,
        selectedIcon = Icons.Filled.List
    ),
    BottomNavigationItem(
        title = "beranda",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
    ),
    BottomNavigationItem(
        title = "kuesioner",
        unselectedIcon = Icons.Outlined.Assignment,
        selectedIcon = Icons.Filled.Assignment
    )
)
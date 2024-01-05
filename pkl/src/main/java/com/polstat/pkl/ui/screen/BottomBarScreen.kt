package com.polstat.pkl.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.polstat.pkl.R

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
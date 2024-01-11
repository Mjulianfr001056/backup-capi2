package com.polstat.pkl.ui.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily

@Composable
@Deprecated("Akan diganti")
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.sampling,
        BottomBarScreen.beranda,
        BottomBarScreen.kuesioner,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation (
        backgroundColor = PklPrimary900,
        contentColor = Color.White
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(
                text = screen.title,
                style = TextStyle(
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                )
            )
        },
        icon = {
            Icon(
                painter = screen.image(),
                contentDescription = "Navigation Icon",
                modifier = Modifier.size(24.dp)
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}

@Composable
fun BottomNavBar(rootNavController : NavHostController){
    val navBackStackEntry by rootNavController.currentBackStackEntryAsState()
    BottomNavigation(
        backgroundColor = PklPrimary900,
        contentColor = Color.White
    ) {
        items.forEach {
            val isSelected = it.title.lowercase() == navBackStackEntry?.destination?.route
            BottomNavigationItem(
                selected = isSelected,
                label = {
                    Text(
                        text = it.title,
                        style = TextStyle(
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Light,
                            fontSize = 12.sp
                        ),

                    )
                },
                onClick = {
                    rootNavController.navigate(it.title) {
                        popUpTo(rootNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) it.selectedIcon else it.unselectedIcon,
                        contentDescription = it.title,
                        modifier = Modifier.size(28.dp),
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true, apiLevel = 28)
@Composable
fun PreviewBottomBar() {
    val navController = rememberNavController()
    Capi63Theme {
        Column{
            BottomNavBar(rootNavController = navController)
            BottomBar(navController = navController)
        }
    }
}
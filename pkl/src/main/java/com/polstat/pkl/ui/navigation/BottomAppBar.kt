package com.polstat.pkl.ui.navigation

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily

@Composable
fun CapiBottomAppBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier.height(56.dp),
        containerColor = PklPrimary900
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column{
                IconButton(
                    onClick = { /* Handle navigation icon click */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Sampling",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Listing",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Column {
                IconButton(
                    onClick = { /* Handle navigation icon click */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Beranda",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Beranda",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Column {
                IconButton(
                    onClick = { /* Handle navigation icon click */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Kuesioner",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Kuesioner",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}
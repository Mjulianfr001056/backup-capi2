package org.odk.collect.pkl.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklSecondary
import com.polstat.pkl.ui.theme.PoppinsFontFamily

@Composable
fun Form() {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Card(
            border = BorderStroke(1.dp, PklSecondary),
            colors = CardDefaults.cardColors(
                containerColor = PklBase
            ),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Text(
                    text = "Isian Listing Terakhir",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.padding(10.dp))

                val weight = 0.5f
                Row{
                    TableCellForm(label = "No. Segmen", value = ": S010", fontSize = 14.sp, weight = weight)
                    TableCellForm(label = "No. Klg", value = ": 0001", fontSize = 14.sp, weight = weight)
                }
                Row{
                    TableCellForm(label = "No. Bg Fisik", value = ": 0001", fontSize = 14.sp, weight = weight)
                    TableCellForm(label = "No. Klg Egb", value = ": 0001", fontSize = 14.sp, weight = weight)
                }
                Row{
                    TableCellForm(label = "No. Bg Sensus", value = ": 0001", fontSize = 14.sp, weight = weight)
                    TableCellForm(label = "No. Ruta", value = ": 0001", fontSize = 14.sp, weight = weight)
                }
            }
        }
    }
}

@Composable
fun RowScope.TableCellForm(
    label: String,
    value: String,
    fontSize: TextUnit = 16.sp,
    color: Color = Color.Black,
    weight: Float
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.weight(weight)
    ) {
        Text(
            modifier = Modifier
                .padding(start = 10.dp, end = 0.dp),
            text = label,
            color = color,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = fontSize
        )
        Text(
            modifier = Modifier
                .padding(start = 0.dp, end = 10.dp),
            text = value,
            color = color,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Light,
            fontSize = fontSize
        )
    }
}

@Preview
@Composable
fun Prev() {
    Capi63Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Form()
        }
    }
}
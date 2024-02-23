package org.odk.collect.pkl.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
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

//@Composable
//fun ImagePicker() {
//    Box(
//        modifier = Modifier
//            .background(
//                color = PklPrimary100.copy(alpha = 0.2f),
//                shape = RoundedCornerShape(16.dp)
//            )
//            .padding(4.dp)
//            .dashedBorder(
//                color = PklSecondary,
//                shape = RoundedCornerShape(12.dp)
//            )
//            .padding(8.dp)
//            .size(200.dp, 75.dp),
//        contentAlignment = Alignment.Center,
//        content = {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier.fillMaxSize()
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Upload,
//                    contentDescription = "Upload",
//                    tint = PklPrimary300
//                )
//                Text(
//                    text = "Unggah Foto Rumah Tangga",
//                    color = PklSecondary,
//                    fontFamily = PoppinsFontFamily,
//                    fontWeight = FontWeight.Medium,
//                    fontSize = 10.sp,
//                    modifier = Modifier.padding(vertical = 5.dp)
//                )
//                Row {
//                    OutlinedIconButton(
//                        onClick = {},
//                        colors = IconButtonDefaults.outlinedIconButtonColors(
//                            containerColor = PklPrimary300,
//                            contentColor = PklBase
//                        ),
//                        border = BorderStroke(width = 1.dp, color = PklPrimary900),
//                        modifier = Modifier.size(24.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.PhotoCamera,
//                            contentDescription = "Camera",
//                            modifier = Modifier.size(12.dp)
//                        )
//                    }
//                    Spacer(modifier = Modifier.width(10.dp))
//                    OutlinedIconButton(
//                        onClick = {},
//                        colors = IconButtonDefaults.outlinedIconButtonColors(
//                            containerColor = PklPrimary300,
//                            contentColor = PklBase
//                        ),
//                        border = BorderStroke(width = 1.dp, color = PklPrimary900),
//                        modifier = Modifier.size(24.dp)
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.PhotoLibrary,
//                            contentDescription = "PhotoLibrary",
//                            modifier = Modifier.size(12.dp)
//                        )
//                    }
//                }
//            }
//        }
//    )
//}
//
//fun Modifier.dashedBorder(
//    color: Color,
//    shape: Shape,
//    strokeWidth: Dp = 1.dp,
//    dashWidth: Dp = 4.dp,
//    gapWidth: Dp = 4.dp,
//    cap: StrokeCap = StrokeCap.Round
//) = this.drawWithContent {
//    val outline = shape.createOutline(size, layoutDirection, this)
//
//    val path = Path()
//    path.addOutline(outline)
//
//    val stroke = Stroke(
//        cap = cap,
//        width = strokeWidth.toPx(),
//        pathEffect = PathEffect.dashPathEffect(
//            intervals = floatArrayOf(dashWidth.toPx(), gapWidth.toPx()),
//            phase = 0f
//        )
//    )
//
//    this.drawContent()
//
//    drawPath(
//        path = path,
//        style = stroke,
//        color = color
//    )
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownRuta2(listKodeRuta: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItemIndex by remember { mutableStateOf(0) }
    val listDropDownMenu = listKodeRuta.toMutableList()
    listDropDownMenu.add(0, "Pilih salah satu ruta")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = listDropDownMenu[selectedItemIndex],
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                focusedIndicatorColor = PklSecondary,
                unfocusedIndicatorColor = PklSecondary,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            ),
            textStyle = TextStyle(
                fontFamily = PoppinsFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.W600
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listDropDownMenu.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 14.sp,
                            fontWeight = if (index == selectedItemIndex) FontWeight.Bold else null
                        )
                    },
                    onClick = {
                        selectedItemIndex = index
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun ImagePickerPreview() {
    Capi63Theme {
        Surface(
            color = PklBase,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val listKodeRuta = listOf("1. Falana", "2. Rofako", "3. Hakam")
                DropDownRuta2(listKodeRuta = listKodeRuta)
            }
        }
    }
}
//
//@Preview
//@Composable
//fun ButtonPrev() {
//    Capi63Theme {
//        Surface(
////            modifier = Modifier.fillMaxSize(),
//            color = PklBase
//        ) {
//            Row {
//                OutlinedIconButton(
//                    onClick = {},
//                    colors = IconButtonDefaults.outlinedIconButtonColors(
//                        containerColor = PklPrimary300,
//                        contentColor = PklBase
//                    ),
//                    border = BorderStroke(width = 2.dp, color = PklPrimary900)
//                ) {
//                    Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = "Camera")
//                }
//                OutlinedIconButton(
//                    onClick = {},
//                    colors = IconButtonDefaults.outlinedIconButtonColors(
//                        containerColor = PklPrimary300,
//                        contentColor = PklBase
//                    ),
//                    border = BorderStroke(width = 2.dp, color = PklPrimary900)
//                ) {
//                    Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = "PhotoLibrary")
//                }
//            }
//        }
//    }
//}
//
//
//@Preview
//@Composable
//fun Prev() {
//    Capi63Theme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            Form()
//        }
//    }
//}
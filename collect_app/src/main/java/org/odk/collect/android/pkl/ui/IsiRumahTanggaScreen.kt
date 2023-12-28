package org.odk.collect.android.pkl.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val PklPrimary700 = Color(0XFFC4314E)
val PklPrimary300 = Color(0xFFD93F57)
val PklPrimary100 = Color(0xFFF7A180)

val PklTertiary900 = Color(0xFFCA2128)
val PklTertiary700 = Color(0xFFEC2B26)
val PklTertiary300 = Color(0xFFFBAC1B)
val PklTertiary100 = Color(0xFFFBe47E)

val PklSecondary700 = Color(0xFF03396C)
val PklSecondary300 = Color(0xFF005B96)
val PklSecondary100 = Color(0xFF6497B1)
val PklSecondary50 = Color(0xFFB3CDE0)

val PklPrimary = Color(0xFF951A2E)
val PklSecondary = Color(0xFF011f4B)
val PklAccent = Color(0xFFF58020)
val PklBase = Color(0xFFFFFAE6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IsiRumahTanggaScreen(
    initialNoSegmen: String = "S001",
    initialNoBangunanFisik: String = "001",
    initialNoBangunanSensus: String = "001",
    initialNoUrutRuta: String = "001"
) {
    val genZOptions = listOf("Ya", "Tidak")

    var noSegmen by rememberSaveable { mutableStateOf(initialNoSegmen) }
    var noBangunanFisik by rememberSaveable { mutableStateOf(initialNoBangunanFisik) }
    var noBangunanSensus by rememberSaveable { mutableStateOf(initialNoBangunanSensus) }
    var noUrutRuta by rememberSaveable { mutableStateOf(initialNoUrutRuta) }
    var namaKepalaRuta by rememberSaveable { mutableStateOf("") }
    var alamat by rememberSaveable { mutableStateOf("") }
    var selectedGenZOption by rememberSaveable { mutableStateOf(genZOptions[0]) }

    Surface(
        color = PklBase,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                IsiRumahTanggaTopBar()
            },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Box(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = PklBase)
            ) {
                Column(
                    modifier = Modifier
                        .padding(15.dp)
                        .verticalScroll(rememberScrollState())
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
                                text = "Isian Listing Ruta Terakhir"
                            )
                            
                            Spacer(modifier = Modifier.padding(10.dp))
                            
                            Text(
                                text = "No. Segmen: $noSegmen | No. BF: $noBangunanFisik | No. BS: $noBangunanSensus"
                            )
                        }
                    }
                    
                    InputNomor(
                        value = noSegmen,
                        onValueChange = { noSegmen = it },
                        label = { Text(text = "1. Nomor Segmen") },
                        onIncrement = { noSegmen = increment(noSegmen) },
                        onDecrement = { noSegmen = decrement(noSegmen) }
                    )

                    Spacer(modifier = Modifier.padding(10.dp))

                    InputNomor(
                        value = noBangunanFisik,
                        onValueChange = { noBangunanFisik = it },
                        label = { Text(text = "2. Nomor Urut Bangunan Fisik") },
                        onIncrement = { noBangunanFisik = increment(noBangunanFisik) },
                        onDecrement = { noBangunanFisik = decrement(noBangunanFisik) }
                    )

                    Spacer(modifier = Modifier.padding(10.dp))

                    InputNomor(
                        value = noBangunanSensus,
                        onValueChange = { noBangunanSensus = it },
                        label = { Text(text = "3. Nomor Urut Bangunan Sensus") },
                        onIncrement = { noBangunanSensus = increment(noBangunanSensus) },
                        onDecrement = { noBangunanSensus = decrement(noBangunanSensus) }
                    )

                    Spacer(modifier = Modifier.padding(10.dp))

                    InputNomor(
                        value = noUrutRuta,
                        onValueChange = { noUrutRuta = it },
                        label = { Text(text = "4. Nomor Urut Rumah Tangga") },
                        onIncrement = { noUrutRuta = increment(noUrutRuta) },
                        onDecrement = { noUrutRuta = decrement(noUrutRuta) }
                    )

                    Spacer(modifier = Modifier.padding(10.dp))

                    TextField(
                        value = namaKepalaRuta,
                        onValueChange = { namaKepalaRuta = it },
                        label = { Text(text = "5. Nama Kepala Rumah Tangga") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = PklPrimary700,
                            unfocusedIndicatorColor = PklAccent
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(modifier = Modifier.padding(10.dp))

                    TextField(
                        value = alamat,
                        onValueChange = { alamat = it },
                        label = { Text(text = "6. Alamat") },
                        singleLine = false,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = PklPrimary700,
                            unfocusedIndicatorColor = PklAccent
                        )
                    )

                    Spacer(modifier = Modifier.padding(10.dp))

                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "7. Keberadaan Gen Z dan Orang Tua",
                            style = TextStyle.Default.copy(
                                fontSize = 16.sp
                            )
                        )

                        RadioButtons(
                            options = genZOptions,
                            selectedOption = selectedGenZOption,
                            onOptionSelected = { option -> selectedGenZOption = option }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RadioButtons(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = {
                            onOptionSelected(option)
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { onOptionSelected(option) }
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodySmall.merge(),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputNomor(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {}
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedIndicatorColor = PklPrimary700,
            unfocusedIndicatorColor = PklAccent
        ),
        trailingIcon = {
            Row {
                IconButton(onClick = onDecrement) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = null,
                        modifier = Modifier
                            .background(PklPrimary, RoundedCornerShape(1.dp))
                            .padding(5.dp),
                        tint = PklBase
                    )
                }

                IconButton(onClick = onIncrement) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .background(PklPrimary, RoundedCornerShape(1.dp))
                            .padding(5.dp),
                        tint = PklBase
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IsiRumahTanggaTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "ISI RUMAH TANGGA",
                color = PklBase
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = PklPrimary700),
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = PklBase
                )
            }
        }
    )
}

fun increment(input: String): String {
    val numericPart = input.filter { it.isDigit() }
    val number = numericPart.toInt()
    val formattedNumber = String.format("%0${numericPart.length}d", number + 1)
    return input.replaceFirst(numericPart, formattedNumber)
}

fun decrement(input: String): String {
    val numericPart = input.filter { it.isDigit() }
    val number = numericPart.toInt()
    val formattedNumber = String.format("%0${numericPart.length}d", number - 1)
    return input.replaceFirst(numericPart, formattedNumber)
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun IsiRumahTanggaPreview() {
    IsiRumahTanggaScreen()
}

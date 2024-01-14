package org.odk.collect.pkl.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.navigation.Capi63Screen
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklAccent
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary
import com.polstat.pkl.ui.theme.PklPrimary700
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PklSecondary
import com.polstat.pkl.ui.theme.PoppinsFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IsiRumahTanggaScreen(
    initialNoSegmen: String = "S001",
    initialNoBangunanFisik: String = "001",
    initialNoBangunanSensus: String = "001",
    initialNoUrutRuta: String = "001",
    navController: NavHostController
) {
    val genZOptions = listOf("Ya", "Tidak")

    var noSegmen by rememberSaveable { mutableStateOf(initialNoSegmen) }
    var noBangunanFisik by rememberSaveable { mutableStateOf(initialNoBangunanFisik) }
    var noBangunanSensus by rememberSaveable { mutableStateOf(initialNoBangunanSensus) }
    var noUrutRuta by rememberSaveable { mutableStateOf(initialNoUrutRuta) }
    var namaKepalaRuta by rememberSaveable { mutableStateOf("") }
    var alamat by rememberSaveable { mutableStateOf("") }
    var selectedGenZOption by rememberSaveable { mutableStateOf(genZOptions[0]) }
    var jumlahGenZ by rememberSaveable { mutableStateOf("") }
    var noUrutRutaEligible by rememberSaveable { mutableStateOf(initialNoUrutRuta) }
    var catatan by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            IsiRumahTanggaTopBar(navController = navController)
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
                            text = "Isian Listing Ruta Terakhir",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.padding(10.dp))

                        Text(
                            text = "No. Segmen: $noSegmen | No. BF: $noBangunanFisik | No. BS: $noBangunanSensus",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                InputNomor(
                    value = noSegmen,
                    onValueChange = { noSegmen = it },
                    label = {
                        Text(
                            text = "1. Nomor Segmen",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        ) },
                    onIncrement = { noSegmen = increment(noSegmen) },
                    onDecrement = { noSegmen = decrement(noSegmen) }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                InputNomor(
                    value = noBangunanFisik,
                    onValueChange = { noBangunanFisik = it },
                    label = {
                        Text(
                            text = "2. Nomor Urut Bangunan Fisik",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        ) },
                    onIncrement = { noBangunanFisik = increment(noBangunanFisik) },
                    onDecrement = { noBangunanFisik = decrement(noBangunanFisik) }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                InputNomor(
                    value = noBangunanSensus,
                    onValueChange = { noBangunanSensus = it },
                    label = {
                        Text(
                            text = "3. Nomor Urut Bangunan Sensus",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        ) },
                    onIncrement = { noBangunanSensus = increment(noBangunanSensus) },
                    onDecrement = { noBangunanSensus = decrement(noBangunanSensus) }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                InputNomor(
                    value = noUrutRuta,
                    onValueChange = { noUrutRuta = it },
                    label = {
                        Text(
                            text = "4. Nomor Urut Rumah Tangga",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        ) },
                    onIncrement = { noUrutRuta = increment(noUrutRuta) },
                    onDecrement = { noUrutRuta = decrement(noUrutRuta) }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                TextField(
                    value = namaKepalaRuta,
                    onValueChange = { namaKepalaRuta = it },
                    label = {
                        Text(
                            text = "5. Nama Kepala Rumah Tangga",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        ) },
                    textStyle = TextStyle.Default.copy(
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold
                    ),
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
                    label = {
                        Text(
                            text = "6. Alamat",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        ) },
                    textStyle = TextStyle.Default.copy(
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold
                    ),
                    singleLine = false,
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

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "7. Keberadaan Gen Z dan Orang Tua",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )

                    RadioButtons(
                        options = genZOptions,
                        selectedOption = selectedGenZOption,
                        onOptionSelected = { option -> selectedGenZOption = option }
                    )
                }

                if (selectedGenZOption == "Ya") {
                    Spacer(modifier = Modifier.padding(10.dp))

                    TextField(
                        value = jumlahGenZ,
                        onValueChange = { jumlahGenZ = it },
                        label = {
                            Text(
                                text = "8. Jumlah Gen Z yang belum kawin dalam rumah tangga",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.SemiBold
                            ) },
                        textStyle = TextStyle.Default.copy(
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        ),
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

                    InputNomor(
                        value = noUrutRutaEligible,
                        onValueChange = { noUrutRutaEligible = it },
                        label = {
                            Text(
                                text = "9. Nomor urut rumah tangga eligible",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.SemiBold
                            ) },
                        onIncrement = { noUrutRutaEligible = increment(noUrutRutaEligible) },
                        onDecrement = { noUrutRutaEligible = decrement(noUrutRutaEligible) }
                    )
                }
                
                Spacer(modifier = Modifier.padding(10.dp))

                TextField(
                    value = catatan,
                    onValueChange = { catatan = it },
                    label = {
                        Text(
                            text = "10. Catatan",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        ) },
                    textStyle = TextStyle.Default.copy(
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold
                    ),
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = PklPrimary700,
                        unfocusedIndicatorColor = PklAccent
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    )
                )
                
                Spacer(modifier = Modifier.padding(10.dp))

                Button(
                    onClick = { /*TODO*/ },
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier.padding(horizontal = 2.dp).fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PklPrimary)
                ) {
                    Text(
                        text = "Kirim",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold
                    )
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
                    modifier = Modifier.padding(start = 16.dp),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold
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
        textStyle = TextStyle.Default.copy(
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold
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
fun IsiRumahTanggaTopBar(navController: NavHostController) {
    TopAppBar(
        title = {
            Text(
                text = "ISI RUMAH TANGGA",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PklPrimary900,
            titleContentColor = Color.White,
        ),
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigate(Capi63Screen.ListRuta.route){
                        popUpTo(Capi63Screen.ListRuta.route){
                            inclusive = true
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "kembali",
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
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
    if (number < 1) {
        return input
    }
    val formattedNumber = String.format("%0${numericPart.length}d", number - 1)
    return input.replaceFirst(numericPart, formattedNumber)
}

@Preview
@Composable
fun IsiRumahTanggaPreview() {
    val navController = rememberNavController()

    Capi63Theme {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            IsiRumahTanggaScreen(
                navController = navController
            )
        }
    }
}

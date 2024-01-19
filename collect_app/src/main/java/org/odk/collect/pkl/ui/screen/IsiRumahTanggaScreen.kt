package org.odk.collect.pkl.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.navigation.Capi63Screen
import com.polstat.pkl.ui.event.IsiRutaScreenEvent
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklAccent
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary
import com.polstat.pkl.ui.theme.PklPrimary700
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PklSecondary
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.utils.UtilFunctions
import com.polstat.pkl.viewmodel.IsiRutaViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IsiRumahTanggaScreen(
    navController: NavHostController,
    viewModel: IsiRutaViewModel
) {
    val state = viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val noBS = viewModel.noBS
//    var isSetInitinialValue by remember { mutableStateOf(false) }
//
//    LaunchedEffect(key1 = isSetInitinialValue) {
//        if (!isSetInitinialValue) {
//
//
//            isSetInitinialValue = false
//        }
//    }

    Scaffold(
        topBar = {
            IsiRumahTanggaTopBar(
                navController = navController,
                noBS = noBS!!
            )
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
                if (state.value.noSegmen != "S000" || state.value.noBgFisik != 0 || state.value.noBgSensus != 0) {
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
                                text = "No. Segmen: ${state.value.noSegmen} | No. BF: ${state.value.noBgFisik} | No. BS: ${state.value.noBgSensus}",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Light
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))

                TextField(
                    value = state.value.SLS!!,
                    onValueChange = { coroutineScope.launch{viewModel.onEvent(IsiRutaScreenEvent.SLSChanged(it))} },
                    label = {
                        Text(
                            text = "1. Satuan Lingkungan Setempat (SLS)",
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
                    value = state.value.noSegmen!!,
                    onValueChange = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.NoSegmenChanged(
                                    it
                                )
                            )
                        }
                    },
                    label = {
                        Text(
                            text = "2. Nomor Segmen",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    onIncrement = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.NoSegmenChanged(
                                    increment(
                                        state.value.noSegmen!!
                                    )
                                )
                            )
                        }
                    },
                    onDecrement = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.NoSegmenChanged(
                                    decrement(
                                        state.value.noSegmen!!
                                    )
                                )
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                InputNomor(
                    value = UtilFunctions.convertTo3DigitsString(state.value.noBgFisik!!),
                    onValueChange = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.NoBgFisikChanged(
                                    it.toInt()
                                )
                            )
                        }
                    },
                    label = {
                        Text(
                            text = "3. Nomor Urut Bangunan Fisik",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    onIncrement = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.NoBgFisikChanged(
                                    increment(
                                        UtilFunctions.convertTo3DigitsString(
                                            state.value.noBgFisik!!
                                        )
                                    ).toInt()
                                )
                            )
                        }
                    },
                    onDecrement = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.NoBgFisikChanged(
                                    decrement(
                                        UtilFunctions.convertTo3DigitsString(
                                            state.value.noBgFisik!!
                                        )
                                    ).toInt()
                                )
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                InputNomor(
                    value = UtilFunctions.convertTo3DigitsString(state.value.noBgSensus!!),
                    onValueChange = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.NoBgSensusChanged(
                                    it.toInt()
                                )
                            )
                        }
                    },
                    label = {
                        Text(
                            text = "4. Nomor Urut Bangunan Sensus",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    onIncrement = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.NoBgSensusChanged(
                                    increment(
                                        UtilFunctions.convertTo3DigitsString(
                                            state.value.noBgSensus!!
                                        )
                                    ).toInt()
                                )
                            )
                        }
                    },
                    onDecrement = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.NoBgSensusChanged(
                                    decrement(
                                        UtilFunctions.convertTo3DigitsString(
                                            state.value.noBgSensus!!
                                        )
                                    ).toInt()
                                )
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                InputNomor(
                    value = state.value.noUrutKlg.toString(),
                    onValueChange = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.NoUrutKlgChanged(
                                    it.toInt()
                                )
                            )
                        }
                    },
                    label = {
                        Text(
                            text = "5. Nomor Urut Keluarga",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    onIncrement = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.NoUrutKlgChanged(
                                    increment(
                                        UtilFunctions.convertTo3DigitsString(
                                            state.value.noUrutKlg!!
                                        )
                                    ).toInt()
                                )
                            )
                        }
                    },
                    onDecrement = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.NoUrutKlgChanged(
                                    decrement(
                                        UtilFunctions.convertTo3DigitsString(
                                            state.value.noUrutKlg!!
                                        )
                                    ).toInt()
                                )
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                TextField(
                    value = state.value.namaKK!!,
                    onValueChange = { coroutineScope.launch { viewModel.onEvent(IsiRutaScreenEvent.NamaKKChanged(it)) } },
                    label = {
                        Text(
                            text = "6. Nama Kepala Keluarga",
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
                    value = state.value.alamat!!,
                    onValueChange = { coroutineScope.launch { viewModel.onEvent(IsiRutaScreenEvent.AlamatChanged(it)) } },
                    label = {
                        Text(
                            text = "7. Alamat",
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

                InputNomor(
                    value = state.value.isGenzOrtu.toString(),
                    onValueChange = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.IsGenzOrtuChanged(
                                    it.toInt()
                                )
                            )
                        }
                    },
                    label = {
                        Text(
                            text = "8. Keberadaan Gen Z dan Orang Tua dalam Keluarga",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    onIncrement = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.IsGenzOrtuChanged(
                                    increment(
                                        UtilFunctions.convertTo3DigitsString(
                                            state.value.isGenzOrtu!!
                                        )
                                    ).toInt()
                                )
                            )
                        }
                    },
                    onDecrement = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.IsGenzOrtuChanged(
                                    decrement(
                                        UtilFunctions.convertTo3DigitsString(
                                            state.value.isGenzOrtu!!
                                        )
                                    ).toInt()
                                )
                            )
                        }
                    }
                )

                if (state.value.isGenzOrtu.toString() != "0") {
                    Spacer(modifier = Modifier.padding(10.dp))

                    InputNomor(
                        value = state.value.noUrutKlgEgb.toString(),
                        onValueChange = { coroutineScope.launch { viewModel.onEvent(IsiRutaScreenEvent.NoUrutKlgEgbChanged(it.toInt())) } },
                        label = {
                            Text(
                                text = "9. Nomor Urut Keluarga Eligible",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.SemiBold
                            ) },
                        onIncrement = { coroutineScope.launch{ viewModel.onEvent(IsiRutaScreenEvent.NoUrutKlgEgbChanged(increment(
                            UtilFunctions.convertTo3DigitsString(
                            state.value.noUrutKlgEgb!!
                        )).toInt())) } },
                        onDecrement = { coroutineScope.launch{ viewModel.onEvent(IsiRutaScreenEvent.NoUrutKlgEgbChanged(decrement(
                            UtilFunctions.convertTo3DigitsString(
                            state.value.noUrutKlgEgb!!
                        )).toInt())) } }
                    )
                }

                InputNomor(
                    value = state.value.penglMkn.toString(),
                    onValueChange = { coroutineScope.launch { viewModel.onEvent(IsiRutaScreenEvent.PenglMknChanged(it.toInt())) } },
                    label = {
                        Text(
                            text = "10. Jumlah Pengelolaan Makan/Minum dan Kebutuhan dalam Keluarga",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        ) },
                    onIncrement = { coroutineScope.launch{ viewModel.onEvent(IsiRutaScreenEvent.PenglMknChanged(increment(
                        UtilFunctions.convertTo3DigitsString(
                        state.value.penglMkn!!
                    )).toInt())) } },
                    onDecrement = { coroutineScope.launch{ viewModel.onEvent(IsiRutaScreenEvent.PenglMknChanged(increment(
                        UtilFunctions.convertTo3DigitsString(
                        state.value.penglMkn!!
                    )).toInt())) } }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                for (i in 1..state.value.penglMkn!!) {
                    Card(
                        border = BorderStroke(1.dp, PklSecondary),
                        colors = CardDefaults.cardColors(
                            containerColor = PklBase
                        ),
                        shape = RectangleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Keterangan Rumah Tangga ke-$i",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                        KeteranganRuta(viewModel = viewModel, index = i-1)
                    }
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.onEvent(IsiRutaScreenEvent.submit)
                        }
                        navController.navigate(Capi63Screen.ListRuta.route + "/${noBS}"){
                            popUpTo(Capi63Screen.ListRuta.route + "/${noBS}"){
                                inclusive = true
                            }
                        }
                    },
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(10.dp),
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .fillMaxWidth(),
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

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeteranganRuta(
    viewModel: IsiRutaViewModel,
    index: Int
){
    val state = viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val kkKrtOptions = listOf("Kepala Keluarga (KK) saja", "Kepala Rumah Tangga (KRT) saja", "KK Sekaligus KRT")

    InputNomor(
        value = UtilFunctions.convertTo3DigitsString(state.value.listNoUrutRuta!![index]),
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoUrutRutaChanged(
                        it.toInt()
                    ), index
                )
            }
        },
        label = {
            Text(
                text = "11. Nomor Urut Rumah Tangga",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoUrutRutaChanged(
                        increment(
                            UtilFunctions.convertTo3DigitsString(
                                state.value.listNoUrutRuta!![index]
                            )
                        ).toInt()
                    ), index
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoUrutRutaChanged(
                        decrement(
                            UtilFunctions.convertTo3DigitsString(
                                state.value.listNoUrutRuta!![index]
                            )
                        ).toInt()
                    ), index
                )
            }
        }
    )

    Spacer(modifier = Modifier.padding(10.dp))

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "12. Identifikasi KK/KRT",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold
        )

        RadioButtons(
            options = kkKrtOptions,
            selectedOption = state.value.listKkOrKrt!![index],
            onOptionSelected = { option ->
                coroutineScope.launch {
                    viewModel.onEvent(
                        IsiRutaScreenEvent.KKOrKRTChanged(option),
                        index
                    )
                }
            }
        )
    }

    Spacer(modifier = Modifier.padding(10.dp))

    TextField(
        value = state.value.listNamaKrt!![index],
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NamaKRTChanged(
                        it
                    ), index
                )
            }
        },
        label = {
            Text(
                text = "13. Nama Kepala Rumah Tangga",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
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
        value = UtilFunctions.convertTo3DigitsString(state.value.listGenzOrtu!![index]),
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.GenzOrtuChanged(
                        it.toInt()
                    ), index
                )
            }
        },
        label = {
            Text(
                text = "14. Keberadaan Gen Z dan Orang Tua dalam Rumah Tangga",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.GenzOrtuChanged(
                        increment(
                            UtilFunctions.convertTo3DigitsString(state.value.listGenzOrtu!![index])
                        ).toInt()
                    ), index
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.GenzOrtuChanged(
                        decrement(
                            UtilFunctions.convertTo3DigitsString(state.value.listGenzOrtu!![index])
                        ).toInt()
                    ), index
                )
            }
        },
    )
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
    onDecrement: () -> Unit = {},
    readOnly: Boolean = true
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
        },
        readOnly = readOnly
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IsiRumahTanggaTopBar(
    navController: NavHostController,
    noBS: String
) {
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
                    navController.navigate(Capi63Screen.ListRuta.route + "/$noBS"){
                        popUpTo(Capi63Screen.ListRuta.route + "/$noBS"){
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

@RequiresApi(Build.VERSION_CODES.S)
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
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
    }
}

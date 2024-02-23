//package org.odk.collect.pkl.ui.screen
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.selection.selectable
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Remove
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.RadioButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.RectangleShape
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import com.polstat.pkl.ui.event.SalinRutaEvent
//import com.polstat.pkl.ui.theme.PklAccent
//import com.polstat.pkl.ui.theme.PklBase
//import com.polstat.pkl.ui.theme.PklPrimary
//import com.polstat.pkl.ui.theme.PklPrimary700
//import com.polstat.pkl.ui.theme.PklPrimary900
//import com.polstat.pkl.ui.theme.PklSecondary
//import com.polstat.pkl.ui.theme.PoppinsFontFamily
//import com.polstat.pkl.utils.UtilFunctions
//import com.polstat.pkl.viewmodel.SalinRutaViewModel
//import kotlinx.coroutines.launch
//import org.odk.collect.pkl.navigation.CapiScreen
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SalinRutaScreen(
//    navController: NavHostController,
//    viewModel: SalinRutaViewModel
//) {
//    val state = viewModel.state.collectAsState()
//    val coroutineScope = rememberCoroutineScope()
//    val noBS = viewModel.noBS
//    val kodeRuta = viewModel.kodeRuta
//    val kodeKlg = viewModel.kodeKlg
//
//    Scaffold(
//        topBar = {
//            SalinRumahTanggaTopBar(
//                navController = navController,
//                noBS = noBS!!
//            )
//        },
//        modifier = Modifier.fillMaxSize()
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .padding(paddingValues)
//                .fillMaxSize()
//                .background(color = PklBase)
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(15.dp)
//                    .verticalScroll(rememberScrollState())
//            ) {
//                if (state.value.noSegmen != "S000" || state.value.noBgFisik != "0" || state.value.noBgSensus != "0") {
//                    Card(
//                        border = BorderStroke(1.dp, PklSecondary),
//                        colors = CardDefaults.cardColors(
//                            containerColor = PklBase
//                        ),
//                        shape = RectangleShape,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                    ) {
//
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(15.dp)
//                        ) {
//                            Text(
//                                text = "Isian Listing Ruta Terakhir",
//                                fontFamily = PoppinsFontFamily,
//                                fontWeight = FontWeight.Medium
//                            )
//
//                            Spacer(modifier = Modifier.padding(10.dp))
//
//                            Text(
//                                text = "No. Segmen: ${state.value.noSegmen} | No. BF: ${state.value.noBgFisik} | No. BS: ${state.value.noBgSensus}",
//                                fontFamily = PoppinsFontFamily,
//                                fontWeight = FontWeight.Light
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.padding(10.dp))
//
//                TextField(
//                    value = state.value.SLS!!,
//                    onValueChange = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.SLSChanged(
//                                    it
//                                )
//                            )
//                        }
//                    },
//                    label = {
//                        Text(
//                            text = "1. Satuan Lingkungan Setempat (SLS)",
//                            fontFamily = PoppinsFontFamily,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    },
//                    textStyle = TextStyle.Default.copy(
//                        fontFamily = PoppinsFontFamily,
//                        fontWeight = FontWeight.SemiBold
//                    ),
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color.Transparent,
//                        focusedIndicatorColor = PklPrimary700,
//                        unfocusedIndicatorColor = PklAccent
//                    ),
//                    keyboardOptions = KeyboardOptions.Default.copy(
//                        imeAction = ImeAction.Next
//                    )
//                )
//
//                Spacer(modifier = Modifier.padding(10.dp))
//
//                InputNomorSR(
//                    value = state.value.noSegmen!!,
//                    onValueChange = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.NoSegmenChanged(
//                                    it
//                                )
//                            )
//                        }
//                    },
//                    label = {
//                        Text(
//                            text = "2. Nomor Segmen",
//                            fontFamily = PoppinsFontFamily,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    },
//                    onIncrement = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.NoSegmenChanged(
//                                    incrementSR(
//                                        state.value.noSegmen!!
//                                    )
//                                )
//                            )
//                        }
//                    },
//                    onDecrement = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.NoSegmenChanged(
//                                    decrement(
//                                        state.value.noSegmen!!
//                                    )
//                                )
//                            )
//                        }
//                    }
//                )
//
//                Spacer(modifier = Modifier.padding(10.dp))
//
//                InputNomorSR(
//                    value = state.value.noBgFisik,
//                    onValueChange = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.NoBgFisikChanged(it)
//                            )
//                        }
//                    },
//                    label = {
//                        Text(
//                            text = "3. Nomor Urut Bangunan Fisik",
//                            fontFamily = PoppinsFontFamily,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    },
//                    onIncrement = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.NoBgFisikChanged(
//                                    incrementSR(state.value.noBgFisik)
//                                )
//                            )
//                        }
//                    },
//                    onDecrement = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.NoBgFisikChanged(
//                                    decrement(state.value.noBgFisik)
//                                )
//                            )
//                        }
//                    }
//                )
//
//                Spacer(modifier = Modifier.padding(10.dp))
//
//                InputNomorSR(
//                    value = state.value.noBgSensus,
//                    onValueChange = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.NoBgSensusChanged(it)
//                            )
//                        }
//                    },
//                    label = {
//                        Text(
//                            text = "4. Nomor Urut Bangunan Sensus",
//                            fontFamily = PoppinsFontFamily,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    },
//                    onIncrement = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.NoBgSensusChanged(
//                                    incrementSR(state.value.noBgSensus)
//                                )
//                            )
//                        }
//                    },
//                    onDecrement = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.NoBgSensusChanged(
//                                    decrement(state.value.noBgSensus)
//                                )
//                            )
//                        }
//                    }
//                )
//
//                Spacer(modifier = Modifier.padding(10.dp))
//
//                InputNomorSR(
//                    value = state.value.noUrutKlg.toString(),
//                    onValueChange = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.NoUrutKlgChanged(
//                                    it.toInt()
//                                )
//                            )
//                        }
//                    },
//                    label = {
//                        Text(
//                            text = "5. Nomor Urut Keluarga",
//                            fontFamily = PoppinsFontFamily,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    },
//                    onIncrement = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.NoUrutKlgChanged(
//                                    increment(
//                                        UtilFunctions.convertTo3DigitsString(
//                                            state.value.noUrutKlg!!
//                                        )
//                                    ).toInt()
//                                )
//                            )
//                        }
//                    },
//                    onDecrement = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.NoUrutKlgChanged(
//                                    decrement(
//                                        UtilFunctions.convertTo3DigitsString(
//                                            state.value.noUrutKlg!!
//                                        )
//                                    ).toInt()
//                                )
//                            )
//                        }
//                    }
//                )
//
//                Spacer(modifier = Modifier.padding(10.dp))
//
//                TextField(
//                    value = state.value.namaKK!!,
//                    onValueChange = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.NamaKKChanged(
//                                    it
//                                )
//                            )
//                        }
//                    },
//                    label = {
//                        Text(
//                            text = "6. Nama Kepala Keluarga",
//                            fontFamily = PoppinsFontFamily,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    },
//                    textStyle = TextStyle.Default.copy(
//                        fontFamily = PoppinsFontFamily,
//                        fontWeight = FontWeight.SemiBold
//                    ),
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color.Transparent,
//                        focusedIndicatorColor = PklPrimary700,
//                        unfocusedIndicatorColor = PklAccent
//                    ),
//                    keyboardOptions = KeyboardOptions.Default.copy(
//                        imeAction = ImeAction.Next
//                    )
//                )
//
//                Spacer(modifier = Modifier.padding(10.dp))
//
//                TextField(
//                    value = state.value.alamat!!,
//                    onValueChange = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.AlamatChanged(
//                                    it
//                                )
//                            )
//                        }
//                    },
//                    label = {
//                        Text(
//                            text = "7. Alamat",
//                            fontFamily = PoppinsFontFamily,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    },
//                    textStyle = TextStyle.Default.copy(
//                        fontFamily = PoppinsFontFamily,
//                        fontWeight = FontWeight.SemiBold
//                    ),
//                    singleLine = false,
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = TextFieldDefaults.textFieldColors(
//                        containerColor = Color.Transparent,
//                        focusedIndicatorColor = PklPrimary700,
//                        unfocusedIndicatorColor = PklAccent
//                    ),
//                    keyboardOptions = KeyboardOptions.Default.copy(
//                        imeAction = ImeAction.Next
//                    )
//                )
//
//                Spacer(modifier = Modifier.padding(10.dp))
//
//                InputNomorSR(
//                    value = state.value.isGenzOrtu.toString(),
//                    onValueChange = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.IsGenzOrtuChanged(
//                                    it.toInt()
//                                )
//                            )
//                        }
//                    },
//                    label = {
//                        Text(
//                            text = "8. Keberadaan Gen Z dan Orang Tua dalam Keluarga",
//                            fontFamily = PoppinsFontFamily,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    },
//                    onIncrement = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.IsGenzOrtuChanged(
//                                    incrementSR(
//                                        UtilFunctions.convertTo3DigitsString(
//                                            state.value.isGenzOrtu!!
//                                        )
//                                    ).toInt()
//                                )
//                            )
//                        }
//                    },
//                    onDecrement = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.IsGenzOrtuChanged(
//                                    decrement(
//                                        UtilFunctions.convertTo3DigitsString(
//                                            state.value.isGenzOrtu!!
//                                        )
//                                    ).toInt()
//                                )
//                            )
//                        }
//                    }
//                )
//
//                if (state.value.isGenzOrtu.toString() != "0") {
//                    Spacer(modifier = Modifier.padding(10.dp))
//
//                    InputNomor(
//                        value = state.value.noUrutKlgEgb.toString(),
//                        onValueChange = {
//                            coroutineScope.launch {
//                                viewModel.onEvent(
//                                    SalinRutaEvent.NoUrutKlgEgbChanged(it.toInt())
//                                )
//                            }
//                        },
//                        label = {
//                            Text(
//                                text = "9. Nomor Urut Keluarga Eligible",
//                                fontFamily = PoppinsFontFamily,
//                                fontWeight = FontWeight.SemiBold
//                            )
//                        },
//                        onIncrement = {
//                            coroutineScope.launch {
//                                viewModel.onEvent(
//                                    SalinRutaEvent.NoUrutKlgEgbChanged(
//                                        increment(
//                                            UtilFunctions.convertTo3DigitsString(
//                                                state.value.noUrutKlgEgb!!
//                                            )
//                                        ).toInt()
//                                    )
//                                )
//                            }
//                        },
//                        onDecrement = {
//                            coroutineScope.launch {
//                                viewModel.onEvent(
//                                    SalinRutaEvent.NoUrutKlgEgbChanged(
//                                        decrement(
//                                            UtilFunctions.convertTo3DigitsString(
//                                                state.value.noUrutKlgEgb!!
//                                            )
//                                        ).toInt()
//                                    )
//                                )
//                            }
//                        }
//                    )
//                }
//
//                InputNomorSR(
//                    value = state.value.penglMkn.toString(),
//                    onValueChange = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.PenglMknChanged(
//                                    it.toInt()
//                                )
//                            )
//                        }
//                    },
//                    label = {
//                        Text(
//                            text = "10. Jumlah Pengelolaan Makan/Minum dan Kebutuhan dalam Keluarga",
//                            fontFamily = PoppinsFontFamily,
//                            fontWeight = FontWeight.SemiBold
//                        )
//                    },
//                    onIncrement = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.PenglMknChanged(
//                                    increment(
//                                        UtilFunctions.convertTo3DigitsString(
//                                            state.value.penglMkn!!
//                                        )
//                                    ).toInt()
//                                )
//                            )
//                        }
//                    },
//                    onDecrement = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(
//                                SalinRutaEvent.PenglMknChanged(
//                                    decrement(
//                                        UtilFunctions.convertTo3DigitsString(
//                                            state.value.penglMkn!!
//                                        )
//                                    ).toInt()
//                                )
//                            )
//                        }
//                    }
//                )
//
//                Spacer(modifier = Modifier.padding(10.dp))
//
//                Card(
//                    border = BorderStroke(1.dp, PklSecondary),
//                    colors = CardDefaults.cardColors(
//                        containerColor = PklBase
//                    ),
//                    shape = RectangleShape,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                ) {
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(15.dp)
//                    ) {
//                        Text(
//                            text = "Keterangan Rumah Tangga",
//                            fontFamily = PoppinsFontFamily,
//                            fontWeight = FontWeight.Medium
//                        )
//                        KeteranganRuta(viewModel = viewModel)
//                    }
//                }
//
//                Spacer(modifier = Modifier.padding(10.dp))
//
//                Spacer(modifier = Modifier.padding(10.dp))
//
//                Button(
//                    onClick = {
//                        coroutineScope.launch {
//                            viewModel.onEvent(SalinRutaEvent.submit)
//                        }
//                        navController.navigate(CapiScreen.Listing.LIST_RUTA + "/${noBS}") {
//                            popUpTo(CapiScreen.Listing.LIST_RUTA + "/${noBS}") {
//                                inclusive = true
//                            }
//                        }
//                    },
//                    shape = MaterialTheme.shapes.small,
//                    contentPadding = PaddingValues(10.dp),
//                    modifier = Modifier
//                        .padding(horizontal = 2.dp)
//                        .fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(containerColor = PklPrimary)
//                ) {
//                    Text(
//                        text = "Kirim",
//                        fontFamily = PoppinsFontFamily,
//                        fontWeight = FontWeight.Bold
//                    )
//                }
//            }
//        }
//    }
//}
//
////@RequiresApi(Build.VERSION_CODES.S)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun KeteranganRuta(
//    viewModel: SalinRutaViewModel
//) {
//    val state = viewModel.state.collectAsState()
//    val coroutineScope = rememberCoroutineScope()
//    val kkKrtOptions =
//        listOf("Kepala Keluarga (KK) saja", "Kepala Rumah Tangga (KRT) saja", "KK Sekaligus KRT")
//
//    ViewNomor(
//        value = UtilFunctions.convertTo3DigitsString(state.value.noUrutRuta!!),
//        onValueChange = {
//            coroutineScope.launch {
//                viewModel.onEvent(
//                    SalinRutaEvent.NoUrutRutaChanged(
//                        it.toInt()
//                    )
//                )
//            }
//        },
//        label = {
//            Text(
//                text = "11. Nomor Urut Rumah Tangga",
//                fontFamily = PoppinsFontFamily,
//                fontWeight = FontWeight.SemiBold
//            )
//        }
//    )
//
//    Spacer(modifier = Modifier.padding(10.dp))
//
//    Column(
//        modifier = Modifier.padding(horizontal = 16.dp)
//    ) {
//        Text(
//            text = "12. Identifikasi KK/KRT",
//            fontFamily = PoppinsFontFamily,
//            fontWeight = FontWeight.SemiBold
//        )
//
//        ViewRadioButtonsSR(
//            options = kkKrtOptions,
//            selectedOption = state.value.kkOrKrt!!
//        )
//    }
//
//    Spacer(modifier = Modifier.padding(10.dp))
//
//    TextField(
//        value = state.value.namaKrt!!,
//        onValueChange = {
//            coroutineScope.launch {
//                viewModel.onEvent(
//                    SalinRutaEvent.NamaKRTChanged(
//                        it
//                    )
//                )
//            }
//        },
//        label = {
//            Text(
//                text = "13. Nama Kepala Rumah Tangga",
//                fontFamily = PoppinsFontFamily,
//                fontWeight = FontWeight.SemiBold
//            )
//        },
//        textStyle = TextStyle.Default.copy(
//            fontFamily = PoppinsFontFamily,
//            fontWeight = FontWeight.SemiBold
//        ),
//        singleLine = true,
//        modifier = Modifier.fillMaxWidth(),
//        colors = TextFieldDefaults.textFieldColors(
//            containerColor = Color.Transparent,
//            focusedIndicatorColor = PklPrimary700,
//            unfocusedIndicatorColor = PklAccent
//        ),
//        keyboardOptions = KeyboardOptions.Default.copy(
//            imeAction = ImeAction.Next
//        ),
//        readOnly = true
//    )
//
//    Spacer(modifier = Modifier.padding(10.dp))
//
//    ViewNomor(
//        value = UtilFunctions.convertTo3DigitsString(state.value.genzOrtu!!),
//        onValueChange = {
//            coroutineScope.launch {
//                viewModel.onEvent(
//                    SalinRutaEvent.GenzOrtuChanged(
//                        it.toInt()
//                    )
//                )
//            }
//        },
//        label = {
//            Text(
//                text = "14. Keberadaan Gen Z dan Orang Tua dalam Rumah Tangga",
//                fontFamily = PoppinsFontFamily,
//                fontWeight = FontWeight.SemiBold
//            )
//        }
//    )
//
//    Spacer(modifier = Modifier.padding(10.dp))
//}
//
//@Composable
//fun RadioButtonsSR(
//    options: List<String>,
//    selectedOption: String,
//    onOptionSelected: (String) -> Unit
//) {
//    Column {
//        options.forEach { option ->
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .selectable(
//                        selected = (option == selectedOption),
//                        onClick = {
//                            onOptionSelected(option)
//                        }
//                    ),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                RadioButton(
//                    selected = (option == selectedOption),
//                    onClick = { onOptionSelected(option) }
//                )
//                Text(
//                    text = option,
//                    style = MaterialTheme.typography.bodySmall.merge(),
//                    modifier = Modifier.padding(start = 16.dp),
//                    fontFamily = PoppinsFontFamily,
//                    fontWeight = FontWeight.SemiBold
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun ViewRadioButtonsSR(
//    options: List<String>,
//    selectedOption: String
//) {
//    Column {
//        options.forEach { option ->
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                RadioButton(
//                    selected = (option == selectedOption),
//                    onClick = {}
//                )
//                Text(
//                    text = option,
//                    style = MaterialTheme.typography.bodySmall.merge(),
//                    modifier = Modifier.padding(start = 16.dp),
//                    fontFamily = PoppinsFontFamily,
//                    fontWeight = FontWeight.SemiBold
//                )
//            }
//        }
//    }
//}
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun InputNomorSR(
//    value: String,
//    onValueChange: (String) -> Unit,
//    label: @Composable (() -> Unit),
//    modifier: Modifier = Modifier,
//    onIncrement: () -> Unit = {},
//    onDecrement: () -> Unit = {},
//    readOnly: Boolean = true
//) {
//    TextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = label,
//        singleLine = true,
//        modifier = modifier.fillMaxWidth(),
//        colors = TextFieldDefaults.textFieldColors(
//            containerColor = Color.Transparent,
//            focusedIndicatorColor = PklPrimary700,
//            unfocusedIndicatorColor = PklAccent
//        ),
//        textStyle = TextStyle.Default.copy(
//            fontFamily = PoppinsFontFamily,
//            fontWeight = FontWeight.SemiBold
//        ),
//        trailingIcon = {
//            Row {
//                IconButton(onClick = onDecrement) {
//                    Icon(
//                        imageVector = Icons.Filled.Remove,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .background(PklPrimary, RoundedCornerShape(1.dp))
//                            .padding(5.dp),
//                        tint = PklBase
//                    )
//                }
//
//                IconButton(onClick = onIncrement) {
//                    Icon(
//                        imageVector = Icons.Filled.Add,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .background(PklPrimary, RoundedCornerShape(1.dp))
//                            .padding(5.dp),
//                        tint = PklBase
//                    )
//                }
//            }
//        },
//        readOnly = readOnly
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ViewNomor(
//    value: String,
//    onValueChange: (String) -> Unit,
//    label: @Composable (() -> Unit),
//    modifier: Modifier = Modifier
//) {
//    TextField(
//        value = value,
//        onValueChange = onValueChange,
//        label = label,
//        singleLine = true,
//        modifier = modifier.fillMaxWidth(),
//        colors = TextFieldDefaults.textFieldColors(
//            containerColor = Color.Transparent,
//            focusedIndicatorColor = PklPrimary700,
//            unfocusedIndicatorColor = PklAccent
//        ),
//        textStyle = TextStyle.Default.copy(
//            fontFamily = PoppinsFontFamily,
//            fontWeight = FontWeight.SemiBold
//        ),
//        readOnly = true
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SalinRumahTanggaTopBar(
//    navController: NavHostController,
//    noBS: String
//) {
//    TopAppBar(
//        title = {
//            Text(
//                text = "ISI RUMAH TANGGA",
//                fontFamily = PoppinsFontFamily,
//                fontWeight = FontWeight.Medium,
//                fontSize = 20.sp
//            )
//        },
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = PklPrimary900,
//            titleContentColor = Color.White,
//        ),
//        navigationIcon = {
//            IconButton(
//                onClick = {
//                    navController.navigate(CapiScreen.Listing.LIST_RUTA + "/$noBS") {
//                        popUpTo(CapiScreen.Listing.LIST_RUTA + "/$noBS") {
//                            inclusive = true
//                        }
//                    }
//                }
//            ) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = "kembali",
//                    tint = Color.White,
//                    modifier = Modifier.size(25.dp)
//                )
//            }
//        }
//    )
//}
//
//fun incrementSR(input: String): String {
//    val numericPart = input.filter { it.isDigit() }
//    val number = numericPart.toInt()
//    val formattedNumber = String.format("%0${numericPart.length}d", number + 1)
//    return input.replaceFirst(numericPart, formattedNumber)
//}
//
//fun decrementSR(input: String): String {
//    val numericPart = input.filter { it.isDigit() }
//    val number = numericPart.toInt()
//    if (number < 1) {
//        return input
//    }
//    val formattedNumber = String.format("%0${numericPart.length}d", number - 1)
//    return input.replaceFirst(numericPart, formattedNumber)
//}
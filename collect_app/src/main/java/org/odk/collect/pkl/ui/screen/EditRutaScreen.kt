package org.odk.collect.pkl.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.polstat.pkl.R
import com.polstat.pkl.ui.event.IsiRutaScreenEvent
import com.polstat.pkl.ui.state.Message
import com.polstat.pkl.ui.theme.PklAccent
import com.polstat.pkl.ui.theme.PklPrimary
import com.polstat.pkl.ui.theme.PklPrimary700
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.viewmodel.IsiRutaViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.odk.collect.pkl.navigation.CapiScreen
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRutaScreen(
    navController: NavController,
    viewModel: IsiRutaViewModel
) {
    val idBS = viewModel.idBS
    val isListRuta = viewModel.isListRuta
    val isMonitoring = viewModel.isMonitoring
    var state = viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val isKlgValid = viewModel.isKlgValid.collectAsState()
    val isRutaValid = viewModel.isRutaValid.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isListRuta == true) stringResource(id = R.string.edit_ruta).uppercase() else stringResource(id = R.string.edit_klg).uppercase(),
                        style = TextStyle(
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp,
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        ),
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PklPrimary900,
                    titleContentColor = Color.White,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            val isMonitoring = false
//                            val isListRuta = true
                            navController.navigate(CapiScreen.Listing.LIST_RUTA + "/$idBS/$isMonitoring/$isListRuta") {
                                popUpTo(CapiScreen.Listing.LIST_RUTA + "/$idBS/$isMonitoring/$isListRuta") {
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
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
//                .background(color = PklBase)
                .paint(
                    painter = painterResource(id = R.drawable.pb_bg_background),
                    contentScale = ContentScale.Crop
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Timber.tag("EditRutaScreen").d("EditRutaScreen: %s", state.value)
                if (isListRuta == true && state.value.listNoUrutRuta[0][0] != "[not set]") {
                    FormEditRuta(viewModel = viewModel)
                } else if (isListRuta == false && state.value.listNoUrutKlg[0] != "[not set]") {
                    FormEditKeluarga(viewModel = viewModel)
                }
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val onEventJob = async {
                                viewModel.onEvent(IsiRutaScreenEvent.edit)
                            }
                            onEventJob.await()
                            if (isKlgValid.value || isRutaValid.value) {
                                if (isListRuta == true) {
                                    Toast.makeText(context, "Ruta berhasil diedit!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Keluarga berhasil diedit!", Toast.LENGTH_SHORT).show()
                                }
                                delay(500L)
                                navController.navigate(CapiScreen.Listing.LIST_RUTA + "/$idBS/$isMonitoring/$isListRuta") {
                                    popUpTo(CapiScreen.Listing.LIST_RUTA + "/$idBS/$isMonitoring/$isListRuta") {
                                        inclusive = true
                                    }
                                }
                            }  else {
                                if (isListRuta == true) {
                                    Toast.makeText(context, "Ruta gagal diedit!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Keluarga gagal diedit!", Toast.LENGTH_SHORT).show()
                                }
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

@Composable
fun FormEditRuta(
    viewModel: IsiRutaViewModel
) {
    val state = viewModel.state.collectAsState()
    val kkKrtOptions = listOf("Kepala Keluarga (KK) saja", "Kepala Rumah Tangga (KRT) saja", "KK Sekaligus KRT")
    val coroutineScope = rememberCoroutineScope()

    InputNomorHuruf(
        value = state.value.listNoUrutRuta[0][0],
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoUrutRutaChanged(
                        it
                    ),
                    0,
                    0
                )
            }
        },
        label = {
            Text(
                text = "1. Nomor Urut Rumah Tangga",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoUrutRutaChanged(
                        viewModel.increment(state.value.listNoUrutRuta[0][0])
                    ),
                    0,
                    0
                )
            }
        },
        onIncrementHuruf = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoUrutRutaChanged(
                        viewModel.incrementHuruf(state.value.listNoUrutRuta[0][0])
                    ),
                    0,
                    0
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoUrutRutaChanged(
                        viewModel.decrement(state.value.listNoUrutRuta[0][0])
                    ),
                    0,
                    0
                )
            }
        },
        onDecrementHuruf = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoUrutRutaChanged(
                        viewModel.decrementHuruf(state.value.listNoUrutRuta[0][0])
                    ),
                    0,
                    0
                )
            }
        },
        readOnly = true,
        isWarningOrError = state.value.listNoUrutRutaMsg[0][0].warning != null || state.value.listNoUrutRutaMsg[0][0].error != null,
        message = state.value.listNoUrutRutaMsg[0][0]
    )

    Spacer(modifier = Modifier.padding(10.dp))

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "2. Identifikasi KK/KRT",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold
        )

        RadioButtons(
            options = kkKrtOptions,
            selectedOption = state.value.listKkOrKrt[0][0],
            message = Message(),
            onOptionSelected = { option ->
                coroutineScope.launch {
                    viewModel.onEvent(
                        IsiRutaScreenEvent.KKOrKRTChanged(option),
                        0,
                        0
                    )
                }
            }
        )
    }

    Spacer(modifier = Modifier.padding(10.dp))

    CustomTextField(
        value = state.value.listNamaKrt[0][0],
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NamaKRTChanged(it),
                    0,
                    0
                )
            }
        },
        label = {
            Text(
                text = "3. Nama Kepala Rumah Tangga",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        isWarningOrError = state.value.listNamaKrtMsg[0][0].warning != null || state.value.listNamaKrtMsg[0][0].error != null,
        message = state.value.listNamaKrtMsg[0][0]
    )

    InputNomor(
        value = state.value.listJmlGenzAnak[0][0].toString(),
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.JmlGenzAnakChanged(it.toIntOrNull() ?: 0),
                    0,
                    0
                )
            }
        },
        label = {
            Text(
                text = "4. Jumlah Gen Z anak eligible (kelahiran 2007-2012)",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.JmlGenzAnakChanged(
                        viewModel.increment(state.value.listJmlGenzAnak[0][0].toString()).toInt()
                    ),
                    0,
                    0
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.JmlGenzAnakChanged(
                        viewModel.decrement(state.value.listJmlGenzAnak[0][0].toString()).toInt()
                    ),
                    0,
                    0
                )
            }
        },
    )

    InputNomor(
        value = state.value.listJmlGenzDewasa[0][0].toString(),
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.JmlGenzDewasaChanged(it.toIntOrNull() ?: 0),
                    0,
                    0
                )
            }
        },
        label = {
            Text(
                text = "5. Jumlah Gen Z dewasa eligible (kelahiran 1997-2006)",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.JmlGenzDewasaChanged(
                        viewModel.increment(state.value.listJmlGenzDewasa[0][0].toString()).toInt()
                    ),
                    0,
                    0
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.JmlGenzDewasaChanged(
                        viewModel.decrement(state.value.listJmlGenzDewasa[0][0].toString()).toInt()
                    ),
                    0,
                    0
                )
            }
        },
    )

    Spacer(modifier = Modifier.padding(10.dp))

    TextField(
        value = state.value.listCatatan[0][0],
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.CatatanChanged(it),
                    0,
                    0
                )
            }
        },
        label = {
            Text(
                text = "6. Catatan",
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
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = PklPrimary700,
            unfocusedIndicatorColor = PklAccent,
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        ),
        maxLines = 3
    )

    Spacer(modifier = Modifier.padding(10.dp))
}

@Composable
fun FormEditKeluarga(
    viewModel: IsiRutaViewModel
) {
    val state = viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    
    Spacer(modifier = Modifier.padding(10.dp))

    CustomTextField(
        value = state.value.SLS,
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.SLSChanged(it)
                )
            }
        },
        label = {
            Text(
                text = "1. Satuan Lingkungan Setempat (SLS)",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        isWarningOrError = state.value.SLSMsg.warning != null || state.value.SLSMsg.error != null,
        message = state.value.SLSMsg
    )

    Spacer(modifier = Modifier.padding(10.dp))

    InputNomor(
        value = state.value.noSegmen,
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoSegmenChanged(it)
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
                        viewModel.incrementNoSegmen(
                            state.value.noSegmen
                        )
                    )
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoSegmenChanged(
                        viewModel.decrementNoSegmen(
                            state.value.noSegmen
                        )
                    )
                )
            }
        },
        readOnly = true,
        isWarningOrError = state.value.noSegmenMsg.warning != null || state.value.noSegmenMsg.error != null,
        message = state.value.noSegmenMsg
    )

    Spacer(modifier = Modifier.padding(10.dp))

    InputNomorHuruf(
        value = state.value.noBgFisik,
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoBgFisikChanged(it)
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
                        viewModel.increment(state.value.noBgFisik)
                    )
                )
            }
        },
        onIncrementHuruf = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoBgFisikChanged(
                        viewModel.incrementHuruf(state.value.noBgFisik)
                    )
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoBgFisikChanged(
                        viewModel.decrement(state.value.noBgFisik)
                    )
                )
            }
        },
        onDecrementHuruf = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoBgFisikChanged(
                        viewModel.decrementHuruf(state.value.noBgFisik)
                    )
                )
            }
        },
        isWarningOrError = state.value.noBgFisikMsg.warning != null || state.value.noBgFisikMsg.error != null,
        message = state.value.noBgFisikMsg
    )

    Spacer(modifier = Modifier.padding(10.dp))

    InputNomorHuruf(
        value = state.value.noBgSensus,
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoBgSensusChanged(it)
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
                        viewModel.increment(state.value.noBgSensus)
                    )
                )
            }
        },
        onIncrementHuruf = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoBgSensusChanged(
                        viewModel.incrementHuruf(state.value.noBgSensus)
                    )
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoBgSensusChanged(
                        viewModel.decrement(state.value.noBgSensus)
                    )
                )
            }
        },
        onDecrementHuruf = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoBgSensusChanged(
                        viewModel.decrementHuruf(state.value.noBgSensus)
                    )
                )
            }
        },
        isWarningOrError = state.value.noBgSensusMsg.warning != null || state.value.noBgSensusMsg.error != null,
        message = state.value.noBgSensusMsg
    )

    Spacer(modifier = Modifier.padding(10.dp))

    InputNomorHuruf(
        value = state.value.listNoUrutKlg[0],
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.NoUrutKlgChanged(it),
                    index = 0
                )
            }
        },
        label = {
            Text(
                text = "6. Nomor Urut Keluarga",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.NoUrutKlgChanged(
                        viewModel.increment(state.value.listNoUrutKlg[0])
                    ),
                    index = 0
                )
            }
        },
        onIncrementHuruf = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.NoUrutKlgChanged(
                        viewModel.incrementHuruf(state.value.listNoUrutKlg[0])
                    ),
                    index = 0
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.NoUrutKlgChanged(
                        viewModel.decrement(state.value.listNoUrutKlg[0])
                    ),
                    index = 0
                )
            }
        },
        onDecrementHuruf = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.NoUrutKlgChanged(
                        viewModel.decrementHuruf(state.value.listNoUrutKlg[0])
                    ),
                    index = 0
                )
            }
        },
        readOnly = true,
        isWarningOrError = state.value.listNoUrutKlgMsg[0].warning != null || state.value.listNoUrutKlgMsg[0].error != null,
        message = state.value.listNoUrutKlgMsg[0]
    )

    Spacer(modifier = Modifier.padding(10.dp))

    CustomTextField(
        value = state.value.listNamaKK[0],
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.NamaKKChanged(it),
                    index = 0
                )
            }
        },
        label = {
            Text(
                text = "7. Nama Kepala Keluarga",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        isWarningOrError = state.value.listNamaKKMsg[0].warning != null || state.value.listNamaKKMsg[0].error != null,
        message = state.value.listNamaKKMsg[0]
    )

    Spacer(modifier = Modifier.padding(10.dp))

    CustomTextField(
        value = state.value.listAlamat[0],
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.AlamatChanged(
                        it
                    ),
                    index = 0
                )
            }
        },
        label = {
            Text(
                text = "8. Alamat",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        isWarningOrError = state.value.listAlamatMsg[0].warning != null || state.value.listAlamatMsg[0].error != null,
        message = state.value.listAlamatMsg[0]
    )

    Spacer(modifier = Modifier.padding(10.dp))

    InputNomor(
        value = state.value.listIsGenzOrtu[0].toString(),
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.IsGenzOrtuChanged(
                        it.toIntOrNull() ?: 0
                    ),
                    index = 0
                )
            }
        },
        label = {
            Text(
                text = "9. Keberadaan Gen Z dan Orang Tua dalam Keluarga",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.IsGenzOrtuChanged(
                        viewModel.increment(state.value.listIsGenzOrtu[0].toString()).toInt()
                    ),
                    index = 0
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.IsGenzOrtuChanged(
                        viewModel.decrement(state.value.listIsGenzOrtu[0].toString()).toInt()
                    ),
                    index = 0
                )
            }
        }
    )

    if (state.value.listIsGenzOrtu[0].toString() != "0") {
        Spacer(modifier = Modifier.padding(10.dp))

        InputNomor(
            value = state.value.listNoUrutKlgEgb[0].toString(),
            onValueChange = {
                coroutineScope.launch {
                    viewModel.onEvent(
                        event = IsiRutaScreenEvent.NoUrutKlgEgbChanged(
                            it.toIntOrNull() ?: 0
                        ),
                        index = 0
                    )
                }
            },
            label = {
                Text(
                    text = "10. Nomor Urut Keluarga Eligible",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold
                )
            },
            onIncrement = {
                coroutineScope.launch {
                    viewModel.onEvent(
                        event = IsiRutaScreenEvent.NoUrutKlgEgbChanged(
                            viewModel.increment(state.value.listNoUrutKlgEgb[0].toString()).toInt()
                        ),
                        index = 0
                    )
                }
            },
            onDecrement = {
                coroutineScope.launch {
                    viewModel.onEvent(
                        event = IsiRutaScreenEvent.NoUrutKlgEgbChanged(
                            viewModel.decrement(state.value.listNoUrutKlgEgb[0].toString()).toInt()
                        ),
                        index = 0
                    )
                }
            },
            readOnly = true,
            isWarningOrError = state.value.listNoUrutKlgEgbMsg[0].warning != null || state.value.listNoUrutKlgEgbMsg[0].error != null,
            message = state.value.listNoUrutKlgEgbMsg[0]
        )
    }

    InputNomor(
        value = state.value.listPenglMkn[0].toString(),
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.PenglMknChanged(
                        it.toIntOrNull() ?: 0
                    ),
                    index = 0
                )
            }
        },
        label = {
            Text(
                text = "11. Jumlah Pengelolaan Makan/Minum dan Kebutuhan dalam Keluarga",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.PenglMknChanged(
                        viewModel.increment(state.value.listPenglMkn[0].toString()).toInt()
                    ),
                    index = 0
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.PenglMknChanged(
                        viewModel.decrement(state.value.listPenglMkn[0].toString()).toInt()
                    ),
                    index = 0
                )
            }
        },
        readOnly = true
    )

    Spacer(modifier = Modifier.padding(10.dp))
}

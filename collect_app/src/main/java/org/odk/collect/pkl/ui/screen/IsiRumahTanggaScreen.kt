package org.odk.collect.pkl.ui.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.polstat.pkl.R
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
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.odk.collect.pkl.navigation.CapiScreen

@Composable
fun IsiRumahTanggaScreen(
    navController: NavHostController,
    viewModel: IsiRutaViewModel
) {
    val state = viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val idBS = viewModel.idBS
    val lastKeluarga = viewModel.lastKeluarga.collectAsState()
    val lastKeluargaEgb = viewModel.lastKeluargaEgb.collectAsState()
    val lastRuta = viewModel.lastRuta.collectAsState()
    val context = LocalContext.current

    viewModel.getRutaLocation()
    LaunchedEffect(key1 = Unit) {
        viewModel.getRutaLocation()
    }

    Scaffold(
        topBar = {
            IsiRumahTanggaTopBar(
                navController = navController,
                idBS = idBS
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = PklBase)
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (lastKeluarga.value.noBgFisik != "0") {
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
                            Row {
                                TableCellForm(
                                    label = "No. Segmen",
                                    value = ": ${lastKeluarga.value.noSegmen}",
                                    fontSize = 10.sp,
                                    weight = weight
                                )
                                TableCellForm(
                                    label = "No. Klg",
                                    value = ": ${UtilFunctions.padWithZeros(lastKeluarga.value.noUrutKlg)}",
                                    fontSize = 10.sp,
                                    weight = weight
                                )
                            }
                            Row {
                                TableCellForm(
                                    label = "No. Bg Fisik",
                                    value = ": ${UtilFunctions.padWithZeros(lastKeluarga.value.noBgFisik)}",
                                    fontSize = 10.sp,
                                    weight = weight
                                )
                                TableCellForm(
                                    label = "No. Klg Egb",
                                    value = ": ${
                                        UtilFunctions.convertTo3DigitsString(lastKeluargaEgb.value.noUrutKlgEgb)
                                    }",
                                    fontSize = 10.sp,
                                    weight = weight
                                )
                            }
                            Row {
                                TableCellForm(
                                    label = "No. Bg Sensus",
                                    value = ": ${UtilFunctions.padWithZeros(lastKeluarga.value.noBgSensus)}",
                                    fontSize = 10.sp,
                                    weight = weight
                                )
                                TableCellForm(
                                    label = "No. Ruta",
                                    value = ": ${UtilFunctions.padWithZeros(lastRuta.value.noUrutRuta)}",
                                    fontSize = 10.sp,
                                    weight = weight
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))

                TextField(
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
                    )
                )

                Spacer(modifier = Modifier.padding(10.dp))

                InputNomor(
                    value = state.value.noSegmen,
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
                    }
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
                    }
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
                    }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                InputNomor(
                    value = state.value.jmlKlg.toString(),
                    onValueChange = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.JmlKlgChanged(
                                    it.toIntOrNull() ?: 0
                                )
                            )
                        }
                    },
                    label = {
                        Text(
                            text = "5. Banyaknya keluarga dalam satu bangunan",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    onIncrement = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.JmlKlgChanged(
                                    viewModel.increment(state.value.jmlKlg.toString()).toInt()
                                )
                            )
                        }
                    },
                    onDecrement = {
                        coroutineScope.launch {
                            viewModel.onEvent(
                                IsiRutaScreenEvent.JmlKlgChanged(
                                    viewModel.decrement(state.value.jmlKlg.toString()).toInt()
                                )
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(10.dp))

                for (i in 1..state.value.jmlKlg) {
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
                                text = "Keterangan Keluarga ke-$i",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium
                            )
                            KeteranganKeluarga(viewModel = viewModel, index = i - 1)
                        }
                    }

                    Spacer(modifier = Modifier.padding(10.dp))
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.onEvent(IsiRutaScreenEvent.submit)
                        }
                        Toast.makeText(context, "Keluarga dan Ruta berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                        val isMonitoring = false
                        val isListRuta = true
                        navController.navigate(CapiScreen.Listing.LIST_RUTA + "/$idBS/$isMonitoring/$isListRuta") {
                            popUpTo(CapiScreen.Listing.LIST_RUTA + "/$idBS/$isMonitoring/$isListRuta") {
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

@Composable
fun KeteranganKeluarga(
    viewModel: IsiRutaViewModel,
    index: Int
) {
    val state = viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    InputNomorHuruf(
        value = state.value.listNoUrutKlg[index],
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.NoUrutKlgChanged(
                        it
                    ),
                    index = index
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
                        viewModel.increment(state.value.listNoUrutKlg[index])
                    ),
                    index = index
                )
            }
        },
        onIncrementHuruf = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.NoUrutKlgChanged(
                        viewModel.incrementHuruf(state.value.listNoUrutKlg[index])
                    ),
                    index = index
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.NoUrutKlgChanged(
                        viewModel.decrement(state.value.listNoUrutKlg[index])
                    ),
                    index = index
                )
            }
        },
        onDecrementHuruf = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.NoUrutKlgChanged(
                        viewModel.decrementHuruf(state.value.listNoUrutKlg[index])
                    ),
                    index = index
                )
            }
        }
    )

    Spacer(modifier = Modifier.padding(10.dp))

    TextField(
        value = state.value.listNamaKK[index],
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.NamaKKChanged(
                        it
                    ),
                    index = index
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
        )
    )

    Spacer(modifier = Modifier.padding(10.dp))

    TextField(
        value = state.value.listAlamat[index],
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.AlamatChanged(
                        it
                    ),
                    index = index
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
        textStyle = TextStyle.Default.copy(
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold
        ),
        singleLine = false,
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
        )
    )

    Spacer(modifier = Modifier.padding(10.dp))

    InputNomor(
        value = state.value.listIsGenzOrtu[index].toString(),
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.IsGenzOrtuChanged(
                        it.toIntOrNull() ?: 0
                    ),
                    index = index
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
                        viewModel.increment(state.value.listIsGenzOrtu[index].toString()).toInt()
                    ),
                    index = index
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.IsGenzOrtuChanged(
                        viewModel.decrement(state.value.listIsGenzOrtu[index].toString()).toInt()
                    ),
                    index = index
                )
            }
        }
    )

    if (state.value.listIsGenzOrtu[index].toString() != "0") {
        Spacer(modifier = Modifier.padding(10.dp))

        InputNomor(
            value = state.value.listNoUrutKlgEgb[index].toString(),
            onValueChange = {
                coroutineScope.launch {
                    viewModel.onEvent(
                        event = IsiRutaScreenEvent.NoUrutKlgEgbChanged(
                            it.toIntOrNull() ?: 0
                        ),
                        index = index
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
                            viewModel.increment(state.value.listNoUrutKlgEgb[index].toString()).toInt()
                        ),
                        index = index
                    )
                }
            },
            onDecrement = {
                coroutineScope.launch {
                    viewModel.onEvent(
                        event = IsiRutaScreenEvent.NoUrutKlgEgbChanged(
                            viewModel.decrement(state.value.listNoUrutKlgEgb[index].toString()).toInt()
                        ),
                        index = index
                    )
                }
            }
        )
    }

    InputNomor(
        value = state.value.listPenglMkn[index].toString(),
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.PenglMknChanged(
                        it.toIntOrNull() ?: 0
                    ),
                    index = index
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
                        viewModel.increment(state.value.listPenglMkn[index].toString()).toInt()
                    ),
                    index = index
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.PenglMknChanged(
                        viewModel.decrement(state.value.listPenglMkn[index].toString()).toInt()
                    ),
                    index = index
                )
            }
        }
    )

    Spacer(modifier = Modifier.padding(10.dp))

    for (i in 1..state.value.listPenglMkn[index]) {
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
                    text = "Keterangan Ruta ke-$i",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium
                )
                KeteranganRuta(
                    viewModel = viewModel,
                    indexRuta = i - 1,
                    indexKlg = index
                )
            }
        }

        Spacer(modifier = Modifier.padding(10.dp))
    }
}

@Composable
fun KeteranganRuta(
    viewModel: IsiRutaViewModel,
    indexKlg: Int,
    indexRuta: Int
) {
    val state = viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val kkKrtOptions =
        listOf("Kepala Keluarga (KK) saja", "Kepala Rumah Tangga (KRT) saja", "KK Sekaligus KRT")

    InputNomorHuruf(
        value = state.value.listNoUrutRuta[indexKlg][indexRuta],
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoUrutRutaChanged(
                        it
                    ),
                    indexKlg,
                    indexRuta
                )
            }
        },
        label = {
            Text(
                text = "12. Nomor Urut Rumah Tangga",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoUrutRutaChanged(
                        viewModel.increment(state.value.listNoUrutRuta[indexKlg][indexRuta])
                    ),
                    indexKlg,
                    indexRuta
                )
            }
        },
        onIncrementHuruf = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoUrutRutaChanged(
                        viewModel.incrementHuruf(state.value.listNoUrutRuta[indexKlg][indexRuta])
                    ),
                    indexKlg,
                    indexRuta
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoUrutRutaChanged(
                        viewModel.decrement(state.value.listNoUrutRuta[indexKlg][indexRuta])
                    ),
                    indexKlg,
                    indexRuta
                )
            }
        },
        onDecrementHuruf = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NoUrutRutaChanged(
                        viewModel.decrementHuruf(state.value.listNoUrutRuta[indexKlg][indexRuta])
                    ),
                    indexKlg,
                    indexRuta
                )
            }
        }
    )

    Spacer(modifier = Modifier.padding(10.dp))

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "13. Identifikasi KK/KRT",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold
        )

        RadioButtons(
            options = kkKrtOptions,
            selectedOption = state.value.listKkOrKrt[indexKlg][indexRuta],
            onOptionSelected = { option ->
                coroutineScope.launch {
                    val task1 = async {
                        viewModel.onEvent(
                            IsiRutaScreenEvent.KKOrKRTChanged(option),
                            indexKlg,
                            indexRuta
                        )
                    }
                    task1.await()
                    if (option == "KK Sekaligus KRT") {
                        viewModel.onEvent(
                            IsiRutaScreenEvent.NamaKRTChanged(state.value.listNamaKK[indexKlg]),
                            indexKlg,
                            indexRuta
                        )
                    } else {
                        viewModel.onEvent(
                            IsiRutaScreenEvent.NamaKRTChanged(""),
                            indexKlg,
                            indexRuta
                        )
                    }
                }
            }
        )
    }

    Spacer(modifier = Modifier.padding(10.dp))

    TextField(
        value = state.value.listNamaKrt[indexKlg][indexRuta],
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.NamaKRTChanged(it),
                    indexKlg,
                    indexRuta
                )
            }
        },
        label = {
            Text(
                text = "14. Nama Kepala Rumah Tangga",
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
        )
    )

    InputNomor(
        value = state.value.listJmlGenzAnak[indexKlg][indexRuta].toString(),
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.JmlGenzAnakChanged(it.toIntOrNull() ?: 0),
                    indexKlg,
                    indexRuta
                )
            }
        },
        label = {
            Text(
                text = "15. Jumlah Gen Z anak eligible (kelahiran 2007-2012)",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.JmlGenzAnakChanged(
                        viewModel.increment(state.value.listJmlGenzAnak[indexKlg][indexRuta].toString()).toInt()
                    ),
                    indexKlg,
                    indexRuta
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.JmlGenzAnakChanged(
                        viewModel.decrement(state.value.listJmlGenzAnak[indexKlg][indexRuta].toString()).toInt()
                    ),
                    indexKlg,
                    indexRuta
                )
            }
        },
    )

    InputNomor(
        value = state.value.listJmlGenzDewasa[indexKlg][indexRuta].toString(),
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.JmlGenzDewasaChanged(it.toIntOrNull() ?: 0),
                    indexKlg,
                    indexRuta
                )
            }
        },
        label = {
            Text(
                text = "15b. Jumlah Gen Z dewasa eligible (kelahiran 1997-2006)",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.JmlGenzDewasaChanged(
                        viewModel.increment(state.value.listJmlGenzDewasa[indexKlg][indexRuta].toString()).toInt()
                    ),
                    indexKlg,
                    indexRuta
                )
            }
        },
        onDecrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.JmlGenzDewasaChanged(
                        viewModel.decrement(state.value.listJmlGenzDewasa[indexKlg][indexRuta].toString()).toInt()
                    ),
                    indexKlg,
                    indexRuta
                )
            }
        },
    )

    Spacer(modifier = Modifier.padding(10.dp))

//    if (state.value.listGenzOrtu[indexKlg][indexRuta] > 0) {
//        Column(
//            modifier = Modifier.padding(horizontal = 14.dp)
//        ) {
//            Text(
//                text = "16. Unggah Foto",
//                fontFamily = PoppinsFontFamily,
//                fontWeight = FontWeight.SemiBold
//            )
//
//            var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }
//            if (imageUri != EMPTY_IMAGE_URI) {
//
//            } else {
//                ImagePicker(onCameraClicked = {}, onGaleryClicked = {})
//                var showGallerySelect by remember { mutableStateOf(false) }
//                if (showGallerySelect) {
//
//                } else {
//
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.padding(10.dp))
//    }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InputNomor(
    value: String?,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {},
    readOnly: Boolean = false
) {
    TextField(
        value = value.toString(),
        onValueChange = onValueChange,
        label = label,
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = PklPrimary700,
            unfocusedIndicatorColor = PklAccent,
        ),
        textStyle = TextStyle.Default.copy(
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold
        ),
        trailingIcon = {
            Row {
                Box(
                    modifier = Modifier
                        .combinedClickable(
                            onClick = { onDecrement() },
                            onLongClick = {}
                        )
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = null,
                        modifier = Modifier
                            .background(PklPrimary, RoundedCornerShape(3.dp))
                            .padding(5.dp),
                        tint = PklBase
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))

                Box(
                    modifier = Modifier
                        .combinedClickable(
                            onClick = { onIncrement() },
                            onLongClick = {}
                        )
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .background(PklPrimary, RoundedCornerShape(3.dp))
                            .padding(5.dp),
                        tint = PklBase
                    )
                }
            }
        },
        readOnly = readOnly
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InputNomorHuruf(
    value: String?,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    onIncrement: () -> Unit = {},
    onIncrementHuruf: () -> Unit = {},
    onDecrement: () -> Unit = {},
    onDecrementHuruf: () -> Unit = {},
    readOnly: Boolean = false
) {
    TextField(
        value = value.toString(),
        onValueChange = onValueChange,
        label = label,
        singleLine = true,
        modifier = modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = PklPrimary700,
            unfocusedIndicatorColor = PklAccent,
        ),
        textStyle = TextStyle.Default.copy(
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold
        ),
        trailingIcon = {
            Row {
                Box(
                    modifier = Modifier
                        .combinedClickable(
                            onClick = { onDecrement() },
                            onLongClick = { onDecrementHuruf() }
                        )
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Remove,
                        contentDescription = null,
                        modifier = Modifier
                            .background(PklPrimary, RoundedCornerShape(3.dp))
                            .padding(5.dp),
                        tint = PklBase
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))

                Box(
                    modifier = Modifier
                        .combinedClickable(
                            onClick = { onIncrement() },
                            onLongClick = { onIncrementHuruf() }
                        )
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .background(PklPrimary, RoundedCornerShape(3.dp))
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
    idBS: String?
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.isi_klg_ruta).uppercase(),
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
                    val isListRuta = true
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
}

//@Composable
//fun ImagePicker(
//    onCameraClicked: () -> Unit,
//    onGaleryClicked: () -> Unit,
//) {
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
//            .size(250.dp, 75.dp),
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
//                        onClick = { onCameraClicked() },
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
//                        onClick = { onGaleryClicked() },
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

@Preview
@Composable
fun IsiRumahTanggaPreview() {
    val navController = rememberNavController()

    Capi63Theme {
        Surface(
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

//val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")

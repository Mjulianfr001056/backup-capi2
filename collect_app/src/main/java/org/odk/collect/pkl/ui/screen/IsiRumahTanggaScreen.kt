package org.odk.collect.pkl.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.ui.event.IsiRutaScreenEvent
import com.polstat.pkl.ui.state.Message
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklAccent
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.ui.theme.PklPrimary
import com.polstat.pkl.ui.theme.PklPrimary700
import com.polstat.pkl.ui.theme.PklPrimary900
import com.polstat.pkl.ui.theme.PklSecondary
import com.polstat.pkl.ui.theme.PklTertiary300
import com.polstat.pkl.ui.theme.PoppinsFontFamily
import com.polstat.pkl.utils.UtilFunctions
import com.polstat.pkl.viewmodel.IsiRutaViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.odk.collect.pkl.navigation.CapiScreen
import timber.log.Timber

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
    val isKlgValid = viewModel.isKlgValid.collectAsState()
    val isRutaValid = viewModel.isRutaValid.collectAsState()
    val isSetInitialKlgFinished = viewModel.isSetInitialKlgValueFinished.collectAsState()
    val errorItemsPosition: MutableList<Float> = mutableListOf()
    var hasError by remember { mutableStateOf(false) }
    var hasMovedToError by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    viewModel.getRutaLocation()
    LaunchedEffect(key1 = Unit) {
        viewModel.getRutaLocation()
    }

    LaunchedEffect(key1 = state.value.noSegmen) {
        val getLastKeluargaTask = async { viewModel.getLastKeluarga(idBS, state.value.noSegmen) }
        val getLastKeluargaEgbTask = async { viewModel.getLastKeluargaEgb(idBS, state.value.noSegmen) }
        val getLastRutaTask = async { viewModel.getLastRuta(idBS, state.value.noSegmen) }
        val getAllRutaByWilayahTask = async { viewModel.getAllRutaByWilayahAndNoSegmen(idBS, state.value.noSegmen) }

        viewModel._lastKeluarga.value = getLastKeluargaTask.await()
        viewModel._lastKeluargaEgb.value = getLastKeluargaEgbTask.await()
        viewModel._lastRuta.value = getLastRutaTask.await()
        viewModel._existingRutaFromDB.value = getAllRutaByWilayahTask.await()

        viewModel.setInitialKlgValue()
        hasMovedToError = true
    }

    LaunchedEffect(key1 = isSetInitialKlgFinished.value) {
        if (isSetInitialKlgFinished.value) {
            viewModel.setAllExistingRuta()
        }
    }

    LaunchedEffect(
        key1 = state.value.jmlKlg
    ) {
        val getAllRutaByWilayahTask = async { viewModel.getAllRutaByWilayahAndNoSegmen(idBS, state.value.noSegmen) }
        viewModel._existingRutaFromDB.value = getAllRutaByWilayahTask.await()
        viewModel.setAllExistingRuta()
        Timber.tag("ISI RUTA SCREEN").d("JmlKlgChanged: setAllExistingRuta executed!")
    }

    LaunchedEffect(hasMovedToError) {
        Timber.tag("ISI RUTA SCREEN").d("IsiRumahTanggaScreen: Masuk launched!")
        Timber.tag("ISI RUTA SCREEN").d("IsiRumahTanggaScreen: hasError=$hasError && hasMovedToError=$hasMovedToError")
        if (hasError && !hasMovedToError) {
            coroutineScope.launch {
                scrollState.animateScrollTo(errorItemsPosition[0].toInt())
                delay(1000L)
//                hasError = false
                hasMovedToError = true
                Timber.tag("ISI RUTA SCREEN").d("IsiRumahTanggaScreen: Sudah pindah ke error!")
            }
        }
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
//                .background(color = PklBase)
                .paint(
                    painter = painterResource(id = R.drawable.pb_bg_background),
                    contentScale = ContentScale.Crop
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .verticalScroll(scrollState)
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
                            Text(
                                text = "(Berdasarkan No Segmen)",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.padding(10.dp))

                            val weight = 0.5f
                            Row {
                                TableCellForm(
                                    label = "No. Segmen",
                                    value = ": ${lastKeluarga.value.noSegmen}",
                                    fontSize = 12.sp,
                                    weight = weight
                                )
                                TableCellForm(
                                    label = "No. Klg",
                                    value = ": ${UtilFunctions.padWithZeros(lastKeluarga.value.noUrutKlg, 3)}",
                                    fontSize = 12.sp,
                                    weight = weight
                                )
                            }
                            Row {
                                TableCellForm(
                                    label = "No. Bg Fisik",
                                    value = ": ${UtilFunctions.padWithZeros(lastKeluarga.value.noBgFisik, 3)}",
                                    fontSize = 12.sp,
                                    weight = weight
                                )
                                TableCellForm(
                                    label = "No. Klg Egb",
                                    value = ": ${
                                        UtilFunctions.convertTo3DigitsString(lastKeluargaEgb.value.noUrutKlgEgb)
                                    }",
                                    fontSize = 12.sp,
                                    weight = weight
                                )
                            }
                            Row {
                                TableCellForm(
                                    label = "No. Bg Sensus",
                                    value = ": ${UtilFunctions.padWithZeros(lastKeluarga.value.noBgSensus, 3)}",
                                    fontSize = 12.sp,
                                    weight = weight
                                )
                                TableCellForm(
                                    label = "No. Ruta",
                                    value = ": ${UtilFunctions.padWithZeros(lastRuta.value.noUrutRuta, 3)}",
                                    fontSize = 12.sp,
                                    weight = weight
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))

                CustomTextField(
                    value = state.value.SLS.uppercase(),
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
                    message = state.value.SLSMsg,
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        if (state.value.SLSMsg.error != null) {
                            errorItemsPosition.add(coordinates.positionInParent().y)
                            hasError = true
                            hasMovedToError = false
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(5.dp))

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
                    isWarningOrError = state.value.noSegmenMsg.warning != null || state.value.noSegmenMsg.error != null,
                    message = state.value.noSegmenMsg,
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        if (state.value.noSegmenMsg.error != null) {
                            errorItemsPosition.add(coordinates.positionInParent().y)
                            hasError = true
                            hasMovedToError = false
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(5.dp))

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
                    message = state.value.noBgFisikMsg,
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        if (state.value.noBgFisikMsg.error != null) {
                            errorItemsPosition.add(coordinates.positionInParent().y)
                            hasError = true
                            hasMovedToError = false
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(5.dp))

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
                    message = state.value.noBgSensusMsg,
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        if (state.value.noBgSensusMsg.error != null) {
                            errorItemsPosition.add(coordinates.positionInParent().y)
                            hasError = true
                            hasMovedToError = false
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(5.dp))

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
                            text = "5. Banyaknya keluarga dalam satu bangunan fisik",
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
                    },
                    isWarningOrError = state.value.jmlKlgMsg.warning != null || state.value.jmlKlgMsg.error != null,
                    message = state.value.jmlKlgMsg,
                    modifier = Modifier.onGloballyPositioned { coordinates ->
                        if (state.value.jmlKlgMsg.error != null) {
                            errorItemsPosition.add(coordinates.positionInParent().y)
                            hasError = true
                            hasMovedToError = false
                        }
                    }
                )

                Spacer(modifier = Modifier.padding(5.dp))

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
                            KeteranganKeluarga(
                                viewModel = viewModel,
                                index = i - 1,
                                scrollState = scrollState,
                                errorItemsPosition = errorItemsPosition,
                                hasError = hasError,
                                hasMovedToError = hasMovedToError
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(5.dp))
                }

                Spacer(modifier = Modifier.padding(5.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            val onEventJob = async {
                                viewModel.onEvent(IsiRutaScreenEvent.submit)
                            }
                            onEventJob.await()
                            delay(1000L)
                            val isMonitoring = false
                            val isListRuta = true
                            if (isKlgValid.value && isRutaValid.value) {
                                Toast.makeText(context, "Keluarga dan Ruta berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                                navController.navigate(CapiScreen.Listing.LIST_RUTA + "/$idBS/$isMonitoring/$isListRuta") {
                                    popUpTo(CapiScreen.Listing.LIST_RUTA + "/$idBS/$isMonitoring/$isListRuta") {
                                        inclusive = true
                                    }
                                }
                            } else if(isKlgValid.value && state.value.jmlKlg == 1 && state.value.listNoUrutKlg[0] == "") {
                                Toast.makeText(context, "Keluarga berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                                navController.navigate(CapiScreen.Listing.LIST_RUTA + "/$idBS/$isMonitoring/$isListRuta") {
                                    popUpTo(CapiScreen.Listing.LIST_RUTA + "/$idBS/$isMonitoring/$isListRuta") {
                                        inclusive = true
                                    }
                                }
                            } else if(isKlgValid.value && state.value.jmlKlg == 1 && state.value.listNoUrutKlg[0] == "0") {
                                Toast.makeText(context, "Keluarga berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                                navController.navigate(CapiScreen.Listing.LIST_RUTA + "/$idBS/$isMonitoring/$isListRuta") {
                                    popUpTo(CapiScreen.Listing.LIST_RUTA + "/$idBS/$isMonitoring/$isListRuta") {
                                        inclusive = true
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Keluarga dan Ruta gagal ditambahkan!", Toast.LENGTH_SHORT).show()
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
    index: Int,
    scrollState: ScrollState,
    errorItemsPosition: MutableList<Float>,
    hasError: Boolean,
    hasMovedToError: Boolean
) {
    val idBS = viewModel.idBS
    val state = viewModel.state.collectAsState()
    val isSubmitted = viewModel.isSubmitted.collectAsState()
    var hasErrorKlg by remember { mutableStateOf(false) }
    var hasMovedToErrorKlg by remember { mutableStateOf(false) }
    val listRutaDropdown = viewModel.listRutaDropdown.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(isSubmitted.value) {
        Timber.tag("ISI RUTA SCREEN").d("IsiRumahTanggaScreen Klg: Masuk launched!")
        Timber.tag("ISI RUTA SCREEN").d("IsiRumahTanggaScreen Klg: hasErrorKlg=$hasErrorKlg && isSubmitted=${isSubmitted.value}")
        if (hasErrorKlg && isSubmitted.value) {
            coroutineScope.launch {
                Timber.tag("ISI RUTA SCREEN").d("IsiRumahTanggaScreen Klg: errorItemsPosition $errorItemsPosition!")
                scrollState.animateScrollTo(errorItemsPosition[0].toInt())
                delay(1000L)
//                hasMovedToErrorKlg = true
                Timber.tag("ISI RUTA SCREEN").d("IsiRumahTanggaScreen Klg: Sudah pindah ke error!")
            }
        }
    }

    LaunchedEffect(
        key1 = state.value.listNoUrutRuta
    ) {
        val getAllRutaByWilayahTask = async { viewModel.getAllRutaByWilayahAndNoSegmen(idBS, state.value.noSegmen) }
        viewModel._existingRutaFromDB.value = getAllRutaByWilayahTask.await()
        viewModel.setAllExistingRuta()
        Timber.tag("ISI RUTA SCREEN").d("KeteranganKeluarga: setAllExistingRuta executed!")
    }

    LaunchedEffect(
        key1 = state.value.listKkOrKrt
    ) {
        val getAllRutaByWilayahTask = async { viewModel.getAllRutaByWilayahAndNoSegmen(idBS, state.value.noSegmen) }
        viewModel._existingRutaFromDB.value = getAllRutaByWilayahTask.await()
        viewModel.setAllExistingRuta()
        Timber.tag("ISI RUTA SCREEN").d("KeteranganKeluarga: setAllExistingRuta executed!")
    }

    LaunchedEffect(
        key1 = state.value.listNamaKrt
    ) {
        val getAllRutaByWilayahTask = async { viewModel.getAllRutaByWilayahAndNoSegmen(idBS, state.value.noSegmen) }
        viewModel._existingRutaFromDB.value = getAllRutaByWilayahTask.await()
        viewModel.setAllExistingRuta()
        Timber.tag("ISI RUTA SCREEN").d("KeteranganKeluarga: setAllExistingRuta executed!")
    }

    LaunchedEffect(
        key1 = state.value.listJmlGenzAnak
    ) {
        val getAllRutaByWilayahTask = async { viewModel.getAllRutaByWilayahAndNoSegmen(idBS, state.value.noSegmen) }
        viewModel._existingRutaFromDB.value = getAllRutaByWilayahTask.await()
        viewModel.setAllExistingRuta()
        Timber.tag("ISI RUTA SCREEN").d("KeteranganKeluarga: setAllExistingRuta executed!")
    }

    LaunchedEffect(
        key1 = state.value.listJmlGenzDewasa
    ) {
        val getAllRutaByWilayahTask = async { viewModel.getAllRutaByWilayahAndNoSegmen(idBS, state.value.noSegmen) }
        viewModel._existingRutaFromDB.value = getAllRutaByWilayahTask.await()
        viewModel.setAllExistingRuta()
        Timber.tag("ISI RUTA SCREEN").d("KeteranganKeluarga: setAllExistingRuta executed!")
    }

    LaunchedEffect(
        key1 = state.value.listKatGenz
    ) {
        val getAllRutaByWilayahTask = async { viewModel.getAllRutaByWilayahAndNoSegmen(idBS, state.value.noSegmen) }
        viewModel._existingRutaFromDB.value = getAllRutaByWilayahTask.await()
        viewModel.setAllExistingRuta()
        Timber.tag("ISI RUTA SCREEN").d("KeteranganKeluarga: setAllExistingRuta executed!")
    }

    LaunchedEffect(
        key1 = state.value.listPenglMkn
    ) {
        val getAllRutaByWilayahTask = async { viewModel.getAllRutaByWilayahAndNoSegmen(idBS, state.value.noSegmen) }
        viewModel._existingRutaFromDB.value = getAllRutaByWilayahTask.await()
        viewModel.setAllExistingRuta()
        Timber.tag("ISI RUTA SCREEN").d("KeteranganKeluarga: setAllExistingRuta executed!")
    }

    InputNomorHuruf(
        value = state.value.listNoUrutKlg[index],
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.NoUrutKlgChanged(it),
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
        },
        isWarningOrError = state.value.listNoUrutKlgMsg[index].warning != null || state.value.listNoUrutKlgMsg[index].error != null,
        message = state.value.listNoUrutKlgMsg[index],
        modifier = Modifier.onGloballyPositioned { coordinates ->
            if (state.value.listNoUrutKlgMsg[index].error != null) {
                errorItemsPosition.add(coordinates.positionInParent().y + 350.0f)
                hasErrorKlg = true
                hasMovedToErrorKlg = false
            }
        }
    )

    Spacer(modifier = Modifier.padding(5.dp))

    CustomTextField(
        value = state.value.listNamaKK[index].uppercase(),
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.NamaKKChanged(it),
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
        isWarningOrError = state.value.listNamaKKMsg[index].warning != null || state.value.listNamaKKMsg[index].error != null,
        message = state.value.listNamaKKMsg[index],
        modifier = Modifier.onGloballyPositioned { coordinates ->
            if (state.value.listNamaKKMsg[index].error != null) {
                if (isSubmitted.value) {
                    errorItemsPosition.add(coordinates.positionInParent().y + 900.0f)
                    hasErrorKlg = true
                }
//                if (hasMovedToErrorKlg) {
//                    hasErrorKlg = false
//                } else {
//                    hasErrorKlg = true
//                }
//                hasMovedToErrorKlg = false
            }
        }
    )

    Spacer(modifier = Modifier.padding(5.dp))

    CustomTextField(
        value = state.value.listAlamat[index].uppercase(),
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
        isWarningOrError = state.value.listAlamatMsg[index].warning != null || state.value.listAlamatMsg[index].error != null,
        message = state.value.listAlamatMsg[index],
        modifier = Modifier.onGloballyPositioned { coordinates ->
            if (state.value.listAlamatMsg[index].error != null) {
                errorItemsPosition.add(coordinates.positionInParent().y)
                hasErrorKlg = true
                hasMovedToErrorKlg = false
            }
        }
    )

    Spacer(modifier = Modifier.padding(5.dp))

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
                text = "9. Keberadaan Gen Z dalam Keluarga yang Tinggal Bersama Ortu",
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
        },
        isWarningOrError = state.value.listIsGenzOrtuMsg[index].warning != null || state.value.listIsGenzOrtuMsg[index].error != null,
        message = state.value.listIsGenzOrtuMsg[index],
        modifier = Modifier.onGloballyPositioned { coordinates ->
            if (state.value.listIsGenzOrtuMsg[index].error != null) {
                errorItemsPosition.add(coordinates.positionInParent().y)
                hasErrorKlg = true
                hasMovedToErrorKlg = false
            }
        }
    )

    if (state.value.listIsGenzOrtu[index].toString() != "0") {
        Spacer(modifier = Modifier.padding(5.dp))

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
            },
            isWarningOrError = state.value.listNoUrutKlgEgbMsg[index].warning != null || state.value.listNoUrutKlgEgbMsg[index].error != null,
            message = state.value.listNoUrutKlgEgbMsg[index],
            modifier = Modifier.onGloballyPositioned { coordinates ->
                if (state.value.listNoUrutKlgEgbMsg[index].error != null) {
                    errorItemsPosition.add(coordinates.positionInParent().y)
                    hasErrorKlg = true
                    hasMovedToErrorKlg = false
                }
            }
        )
    }

    Spacer(modifier = Modifier.padding(5.dp))

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

    Spacer(modifier = Modifier.padding(5.dp))

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
                    indexKlg = index,
                    scrollState = scrollState,
                    errorItemsPosition = errorItemsPosition,
                    hasErrorKlg = hasErrorKlg,
                    hasMovedToErrorKlg = hasMovedToErrorKlg
                )
            }
        }

        Spacer(modifier = Modifier.padding(5.dp))
    }

    if (state.value.jmlKlg != 0 && state.value.listPenglMkn[index] == 0) {
        Spacer(modifier = Modifier.padding(5.dp))

        Text(
            text = "11a. Isikan jika keluarga tidak memiliki pengelolaan makan/minum dan kebutuhan dalam keluarga",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = if (state.value.listAnotherRutaMsg[index].warning != null || state.value.listAnotherRutaMsg[index].error != null) PklPrimary900 else Color.Black,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        DropDownRuta(
            listRuta = listRutaDropdown.value,
            viewModel = viewModel,
            indexKlg = index
        )

        Spacer(modifier = Modifier.padding(5.dp))

        if (state.value.listAnotherRuta[index].kodeRuta != "[not set]") {
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
                        text = "Keterangan Ruta",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium
                    )
                    KeteranganRutaReadOnly(state.value.listAnotherRuta[index])
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(5.dp))
}

@Composable
fun KeteranganRuta(
    viewModel: IsiRutaViewModel,
    indexKlg: Int,
    indexRuta: Int,
    scrollState: ScrollState,
    errorItemsPosition: MutableList<Float>,
    hasErrorKlg: Boolean,
    hasMovedToErrorKlg: Boolean
) {
    val state = viewModel.state.collectAsState()
    var hasErrorRuta by remember { mutableStateOf(hasErrorKlg) }
    var hasMovedToErrorRuta by remember { mutableStateOf(hasMovedToErrorKlg) }
    val coroutineScope = rememberCoroutineScope()
    val kkKrtOptions =
        listOf("Kepala Keluarga (KK) saja", "Kepala Rumah Tangga (KRT) saja", "KK Sekaligus KRT")
    val isEnableOptions = listOf("Ya", "Tidak")

    LaunchedEffect(hasMovedToErrorRuta) {
        Timber.tag("ISI RUTA SCREEN").d("IsiRumahTanggaScreen Ruta: Masuk launched!")
        Timber.tag("ISI RUTA SCREEN").d("IsiRumahTanggaScreen Ruta: hasErrorRuta=$hasErrorRuta && hasMovedToErrorRuta=$hasMovedToErrorRuta")
        if (hasErrorRuta && !hasMovedToErrorRuta) {
            coroutineScope.launch {
                scrollState.animateScrollTo(errorItemsPosition[0].toInt())
                delay(1000L)
                hasMovedToErrorRuta = true
                Timber.tag("ISI RUTA SCREEN").d("IsiRumahTanggaScreen Ruta: Sudah pindah ke error!")
            }
        }
    }

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
        },
        isWarningOrError = state.value.listNoUrutRutaMsg[indexKlg][indexRuta].warning != null || state.value.listNoUrutRutaMsg[indexKlg][indexRuta].error != null,
        message = state.value.listNoUrutRutaMsg[indexKlg][indexRuta],
        modifier = Modifier.onGloballyPositioned { coordinates ->
            if (state.value.listNoUrutRutaMsg[indexKlg][indexRuta].error != null) {
                errorItemsPosition.add(coordinates.positionInParent().y)
                hasErrorRuta = true
                hasMovedToErrorRuta = false
            }
        }
    )

    Spacer(modifier = Modifier.padding(5.dp))

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
            message = state.value.listKkOrKrtMsg[indexKlg][indexRuta],
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

    Spacer(modifier = Modifier.padding(5.dp))

    CustomTextField(
        value = state.value.listNamaKrt[indexKlg][indexRuta].uppercase(),
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
        isWarningOrError = state.value.listNamaKrtMsg[indexKlg][indexRuta].warning != null || state.value.listNamaKrtMsg[indexKlg][indexRuta].error != null,
        message = state.value.listNamaKrtMsg[indexKlg][indexRuta],
        modifier = Modifier.onGloballyPositioned { coordinates ->
            if (state.value.listNamaKrtMsg[indexKlg][indexRuta].error != null) {
                errorItemsPosition.add(coordinates.positionInParent().y)
                hasErrorRuta = true
                hasMovedToErrorRuta = false
            }
        }
    )

    Spacer(modifier = Modifier.padding(5.dp))

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
                text = "15a. Jumlah Gen Z anak eligible (kelahiran 2007-2012)",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.JmlGenzAnakChanged(
                        viewModel.increment(state.value.listJmlGenzAnak[indexKlg][indexRuta].toString())
                            .toInt()
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
                        viewModel.decrement(state.value.listJmlGenzAnak[indexKlg][indexRuta].toString())
                            .toInt()
                    ),
                    indexKlg,
                    indexRuta
                )
            }
        },
    )

    Spacer(modifier = Modifier.padding(5.dp))

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
                        viewModel.increment(state.value.listJmlGenzDewasa[indexKlg][indexRuta].toString())
                            .toInt()
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
                        viewModel.decrement(state.value.listJmlGenzDewasa[indexKlg][indexRuta].toString())
                            .toInt()
                    ),
                    indexKlg,
                    indexRuta
                )
            }
        },
    )

    Spacer(modifier = Modifier.padding(5.dp))

    if (state.value.listIsGenzOrtu[indexKlg] > 0) {
        Text(
            text = "15c. Apakah dapat dikunjungi ketika periode pencacahan?",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = if (state.value.listIsEnableMsg[indexKlg][indexRuta].warning != null || state.value.listIsEnableMsg[indexKlg][indexRuta].error != null) PklPrimary900 else Color.Black,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        RadioButtons(
            options = isEnableOptions,
            selectedOption = state.value.listIsEnable[indexKlg][indexRuta],
            message = state.value.listIsEnableMsg[indexKlg][indexRuta],
            onOptionSelected = { option ->
                coroutineScope.launch {
                    viewModel.onEvent(
                        IsiRutaScreenEvent.IsEnableChanged(option),
                        indexKlg,
                        indexRuta
                    )
                }
            }
        )
    }

    Spacer(modifier = Modifier.padding(5.dp))

    TextField(
        value = state.value.listCatatan[indexKlg][indexRuta],
        onValueChange = {
            coroutineScope.launch {
                viewModel.onEvent(
                    IsiRutaScreenEvent.CatatanChanged(it),
                    indexKlg,
                    indexRuta
                )
            }
        },
        label = {
            Text(
                text = "16. Catatan",
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

    Spacer(modifier = Modifier.padding(5.dp))
}

@Composable
fun KeteranganRutaReadOnly(
    ruta: Ruta
) {
    val kkKrtOptions = listOf("Kepala Keluarga (KK) saja", "Kepala Rumah Tangga (KRT) saja", "KK Sekaligus KRT")

    InputNomorHuruf(
        value = ruta.noUrutRuta,
        onValueChange = {},
        label = {
            Text(
                text = "12. Nomor Urut Rumah Tangga",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {},
        onIncrementHuruf = {},
        onDecrement = {},
        onDecrementHuruf = {},
        readOnly = true
    )

    Spacer(modifier = Modifier.padding(5.dp))

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
            selectedOption = when (ruta.kkOrKrt) {
                "1" -> "Kepala Keluarga (KK) saja"
                "2" -> "Kepala Rumah Tangga (KRT) saja"
                "3" -> "KK Sekaligus KRT"
                else -> "N/A"
            },
            onOptionSelected = {}
        )
    }

    Spacer(modifier = Modifier.padding(5.dp))

    CustomTextField(
        value = ruta.namaKrt,
        onValueChange = {},
        label = {
            Text(
                text = "14. Nama Kepala Rumah Tangga",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        }
    )

    Spacer(modifier = Modifier.padding(5.dp))

    InputNomor(
        value = ruta.jmlGenzAnak.toString(),
        onValueChange = {},
        label = {
            Text(
                text = "15. Jumlah Gen Z anak eligible (kelahiran 2007-2012)",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {},
        onDecrement = {},
        readOnly = true
    )

    Spacer(modifier = Modifier.padding(5.dp))

    InputNomor(
        value = ruta.jmlGenzDewasa.toString(),
        onValueChange = {},
        label = {
            Text(
                text = "15b. Jumlah Gen Z dewasa eligible (kelahiran 1997-2006)",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold
            )
        },
        onIncrement = {},
        onDecrement = {},
        readOnly = true
    )

    Spacer(modifier = Modifier.padding(5.dp))

    TextField(
        value = ruta.catatan,
        onValueChange = {},
        label = {
            Text(
                text = "16. Catatan",
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
        maxLines = 3,
        readOnly = true
    )

    Spacer(modifier = Modifier.padding(5.dp))
}


@Composable
fun RadioButtons(
    options: List<String>,
    selectedOption: String,
    message: Message = Message(),
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (message.warning != null){
                Text(
                    text = message.warning ?: "",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.W600,
                    fontSize = 12.sp,
                    color = PklTertiary300
                )
            } else {
                Text(
                    text = message.error ?: "",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.W600,
                    fontSize = 12.sp,
                    color = PklPrimary900
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
    readOnly: Boolean = false,
    isWarningOrError: Boolean = false,
    message: Message = Message()
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
            errorContainerColor = Color.Transparent
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
                            onClick = {
                                if (!readOnly) {
                                    onDecrement()
                                }
                            },
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
                            onClick = {
                                if (!readOnly) {
                                    onIncrement()
                                }
                            },
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
        readOnly = readOnly,
        isError = isWarningOrError,
        supportingText = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (message.warning != null){
                    Text(
                        text = message.warning ?: "",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.W600,
                        fontSize = 12.sp,
                        color = PklTertiary300
                    )
                } else {
                    Text(
                        text = message.error ?: "",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.W600,
                        fontSize = 12.sp,
                        color = PklPrimary900
                    )
                }
            }
        }
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
    readOnly: Boolean = false,
    isWarningOrError: Boolean = false,
    message: Message = Message()
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
            errorContainerColor = Color.Transparent
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
                            onClick = {
                                if (!readOnly) {
                                    onDecrement()
                                }
                            },
                            onLongClick = {
                                if (!readOnly) {
                                    onDecrementHuruf()
                                }
                            }
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
                            onClick = {
                                if (!readOnly) {
                                    onIncrement()
                                }
                            },
                            onLongClick = {
                                if (!readOnly) {
                                    onIncrementHuruf()
                                }
                            }
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
        readOnly = readOnly,
        isError = isWarningOrError,
        supportingText = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (message.warning != null){
                    Text(
                        text = message.warning ?: "",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.W600,
                        fontSize = 12.sp,
                        color = PklTertiary300
                    )
                } else {
                    Text(
                        text = message.error ?: "",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.W600,
                        fontSize = 12.sp,
                        color = PklPrimary900
                    )
                }
            }
        }
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

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    isWarningOrError: Boolean = false,
    message: Message = Message()
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier.fillMaxWidth(),
        textStyle = TextStyle.Default.copy(
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold
        ),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = PklPrimary700,
            unfocusedIndicatorColor = PklAccent,
            errorContainerColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Next
        ),
        isError = isWarningOrError,
        supportingText = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (message.warning != null){
                    Text(
                        text = message.warning ?: "",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.W600,
                        fontSize = 12.sp,
                        color = PklTertiary300
                    )
                } else {
                    Text(
                        text = message.error ?: "",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.W600,
                        fontSize = 12.sp,
                        color = PklPrimary900
                    )
                }
            }
        },
        readOnly = readOnly
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownRuta(
    viewModel: IsiRutaViewModel,
    listRuta: List<Ruta>,
    indexKlg: Int
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItemIndex by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val state = viewModel.state.collectAsState()
    val listDropDownMenu = listRuta.toMutableList()
    val ruta = Ruta()
    listDropDownMenu.add(0, ruta.copy(namaKrt = "Pilih salah satu ruta"))

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        /**
         * Ketika sudah menginputkan bbrp ruta dalam suatu klg dan di klg lain yg tdk punya ruta sdh milih dropdown ruta, kemudian jml ruta dalam suatu klg tadi dikurangi terjadi fc.
         (SOLVED)
         */

        if (selectedItemIndex > listDropDownMenu.size - 1) {
            selectedItemIndex = 0
            coroutineScope.launch {
                viewModel.onEvent(
                    event = IsiRutaScreenEvent.AnotherRutaChanged(listDropDownMenu[selectedItemIndex]),
                    index = indexKlg
                )
            }
        }

        TextField(
            value = if (selectedItemIndex == 0) listDropDownMenu[selectedItemIndex].namaKrt else "${listDropDownMenu[selectedItemIndex].noUrutRuta} - ${listDropDownMenu[selectedItemIndex].namaKrt}",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = PklSecondary,
                unfocusedIndicatorColor = PklAccent,
                errorContainerColor = Color.Transparent
            ),
            textStyle = TextStyle(
                fontFamily = PoppinsFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.W600
            ),
            isError = state.value.listAnotherRutaMsg[indexKlg].warning != null || state.value.listAnotherRutaMsg[indexKlg].error != null,
            supportingText = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (state.value.listAnotherRutaMsg[indexKlg].warning != null){
                        Text(
                            text = state.value.listAnotherRutaMsg[indexKlg].warning ?: "",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.W600,
                            fontSize = 12.sp,
                            color = PklTertiary300
                        )
                    } else {
                        Text(
                            text = state.value.listAnotherRutaMsg[indexKlg].error ?: "",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.W600,
                            fontSize = 12.sp,
                            color = PklPrimary900
                        )
                    }
                }
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(PklBase)
        ) {
            listDropDownMenu.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = if (index == 0) item.namaKrt else "${item.noUrutRuta} - ${item.namaKrt}",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 14.sp,
                            fontWeight = if (index == selectedItemIndex) FontWeight.Bold else null,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        selectedItemIndex = index
                        expanded = false
                        coroutineScope.launch {
                            viewModel.onEvent(
                                event = IsiRutaScreenEvent.AnotherRutaChanged(item),
                                index = indexKlg
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PklBase)
                )
            }
        }
    }
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

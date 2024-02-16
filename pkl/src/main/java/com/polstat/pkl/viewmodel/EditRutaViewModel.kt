package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.database.entity.KeluargaEntity
import com.polstat.pkl.database.entity.RutaEntity
import com.polstat.pkl.repository.KeluargaRepository
import com.polstat.pkl.repository.LocalRutaRepository
import com.polstat.pkl.ui.state.IsiRutaScreenState
import com.polstat.pkl.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRutaViewModel @Inject constructor(
    private val localRutaRepository: LocalRutaRepository,
    private val keluargaRepository: KeluargaRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val TAG = "CAPI63_EDIT_RUTA_VM"
    }

    private val _state = MutableStateFlow(IsiRutaScreenState())

    val state = _state.asStateFlow()

    val isListRuta = savedStateHandle.get<Boolean>("isListRuta")

    private val _editRuta = MutableStateFlow(RutaEntity())

    val editRuta = _editRuta.asStateFlow()

    private val _editKlg = MutableStateFlow(KeluargaEntity())

    val editKlg = _editKlg.asStateFlow()

    private val _isKlgValid = MutableStateFlow(false)

    val isKlgValid = _isKlgValid.asStateFlow()

    private val _isRutaValid = MutableStateFlow(false)

    val isRutaValid = _isRutaValid.asStateFlow()

    init {

    }

    private fun setRutaValueEdit() {
        _state.value = state.value.copy(
            listNoUrutRuta = listOf(listOf(editRuta.value.noUrutRuta)),
            listKkOrKrt = listOf(listOf(editRuta.value.kkOrKrt)),
            listNamaKrt = listOf(listOf(editRuta.value.namaKrt)),
            listJmlGenzAnak = listOf(listOf(editRuta.value.jmlGenzAnak)),
            listJmlGenzDewasa = listOf(listOf(editRuta.value.jmlGenzDewasa)),
            listKatGenz = listOf(listOf(editRuta.value.katGenz)),
            listCatatan = listOf(listOf(editRuta.value.catatan))
        )
    }

    private fun setKeluargaValueEdit() {
        _state.value = state.value.copy(
            SLS = editKlg.value.banjar,
            noBgSensus = editKlg.value.noBgSensus,
            noBgFisik = editKlg.value.noBgFisik,
            noSegmen = editKlg.value.noSegmen,
            listNoUrutKlg = listOf(editKlg.value.noUrutKlg),
            listNoUrutKlgEgb = listOf(editKlg.value.noUrutKlgEgb),
            listNamaKK = listOf(editKlg.value.namaKK),
            listAlamat = listOf(editKlg.value.alamat),
            listIsGenzOrtu = listOf(editKlg.value.isGenzOrtu),
            listPenglMkn = listOf(editKlg.value.penglMkn)
        )
    }

    private fun getKeluarga(
        kodeKlg: String
    ) {
        viewModelScope.launch {
            keluargaRepository.getKeluarga(kodeKlg).collectLatest { result ->
                when(result) {
                    is Result.Error -> {
                        result.message?.let { error ->
                            Log.d(TAG, "Error getKeluarga: $error")
                        }
                    }
                    is Result.Loading -> Log.d(TAG, "getKeluarga: Loading...")
                    is Result.Success -> {
                        result.data?.let { klg ->
                            _editKlg.value = klg
                            Log.d(TAG, "getKeluarga: Berhasil mendapatkan keluarga ${editKlg.value}")
                        }
                    }
                }
            }
        }
    }

    fun getRuta(
        kodeRuta: String
    ) {
        viewModelScope.launch {
            localRutaRepository.getRuta(kodeRuta).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let {
                            _editRuta.value = it
                        }
                    }
                    is Result.Error -> {
                        result.message?.let { error ->
                            Log.d(TAG, "Error getRuta: $error")
                        }
                    }
                    is Result.Loading -> {
                        Log.d(TAG, "getRuta: Loading...")
                    }
                }
            }
        }
    }

}
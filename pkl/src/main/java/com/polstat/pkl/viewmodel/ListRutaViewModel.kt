package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.repository.RutaRepository
import kotlinx.coroutines.launch

class ListRutaViewModel(
    private val rutaRepository: RutaRepository
): ViewModel() {

    companion object {
        private const val TAG = "ListRutaViewModel"
    }

    private lateinit var ruta: List<Ruta>
    var rutaUiState: RutaUiState by mutableStateOf(RutaUiState.Loading)
        private set

    init {
        viewModelScope.launch {
            try {
                getAllRuta()
            } catch (e: Exception) {
                Log.e(
                    TAG, e.stackTraceToString()
                )
            }
        }
    }

    fun searchRuta(search: String) {
        if (!::ruta.isInitialized)
            return

        if (rutaUiState is RutaUiState.Success) {
            val filteredRuta = ruta.filter { ruta ->
                ruta.kodeRuta.lowercase().contains(search.lowercase(), false)
                ruta.nama_krt.lowercase().contains(search.lowercase(), false)
            }
            rutaUiState = RutaUiState.Success(filteredRuta)
        }
    }

    suspend fun getAllRuta() {
        try {
            ruta = rutaRepository.getAllRuta()
        } catch (e: Exception) {
            Log.e(
                TAG, "Error: ${e.message}"
            )
            return
        }
        Log.d(
            TAG, "ruta: $ruta"
        )
        rutaUiState = RutaUiState.Success(ruta)
    }
}

sealed interface RutaUiState {
    data class Success(val ruta: List<Ruta>) : RutaUiState
    object Error : RutaUiState
    object Loading : RutaUiState
}
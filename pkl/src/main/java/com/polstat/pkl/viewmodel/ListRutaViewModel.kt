package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.repository.RutaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListRutaViewModel @Inject constructor(
    private val rutaRepository: RutaRepository
): ViewModel() {

    companion object {
        private const val TAG = "ListRutaViewModel"
    }

    private lateinit var listRuta: List<Ruta>
    private lateinit var ruta: Ruta
    var rutaUiState: RutaUiState by mutableStateOf(RutaUiState.Loading)
        private set
    var selectedRuta: String by mutableStateOf("")

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
        if (!::listRuta.isInitialized)
            return

        if (rutaUiState is RutaUiState.Success) {
            val filteredRuta = listRuta.filter { listRuta ->
                listRuta.kodeRuta.lowercase().contains(search.lowercase(), false)
                listRuta.nama_krt.lowercase().contains(search.lowercase(), false)
            }
            rutaUiState = RutaUiState.Success(filteredRuta)
        }
    }

    suspend fun getAllRuta() {
        try {
            listRuta = rutaRepository.getAllRuta()
        } catch (e: Exception) {
            Log.e(
                TAG, "Error: ${e.message}"
            )
            return
        }
        Log.d(
            TAG, "List Ruta: $listRuta"
        )
        rutaUiState = RutaUiState.Success(listRuta)
    }

    suspend fun editRuta() {
        try {
           ruta = rutaRepository.editRuta("update", selectedRuta)
        } catch (e: Exception) {
            Log.e(TAG, "Error in update ruta: ${e.message}")
            return
        }
        Log.d(TAG, "Updated ruta: $ruta")
    }

    suspend fun deleteRuta() {
        try {
            rutaRepository.deleteRuta("delete", selectedRuta)
        } catch (e: Exception) {
            Log.e(TAG, "Error in delete ruta: ${e.message}")
            return
        }
        Log.d(TAG, "Ruta is deleted")
    }
}

sealed interface RutaUiState {
    data class Success(val ruta: List<Ruta>) : RutaUiState
    object Error : RutaUiState
    object Loading : RutaUiState
}
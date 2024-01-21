package com.polstat.pkl.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.polstat.pkl.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KuesionerViewModel @Inject constructor(
    sessionRepository: SessionRepository
): ViewModel() {

    companion object {
        private const val TAG = "CAPI63_KUESIONER_VM"
    }

    private val _session = sessionRepository.getActiveSession()
    val session = _session

    fun getToken(): String {
        return try {
            _session!!.token!!
        } catch (e: Exception) {
            Log.d(TAG, "getToken: error getting token")
            "fKaPJ9kmrHUJsZ0mv2jx3dQir4SygjeDHgfByQIjyidK12HALTvTrrzek5VlC\$qq"
        }
    }
}
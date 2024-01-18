package com.polstat.pkl.viewmodel

import androidx.lifecycle.ViewModel
import com.polstat.pkl.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SamplingViewModel @Inject constructor (
    private val sessionRepository: SessionRepository
) : ViewModel() {

    companion object {
        private const val TAG = "CAPI63_SAMPLING_VM"
    }

    private val _session = sessionRepository.getActiveSession()

    val session = _session
}
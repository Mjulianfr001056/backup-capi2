package com.polstat.pkl.ui.event

sealed class SalinRutaEvent {
    data class SLSChanged(val sls: String) : SalinRutaEvent()
    data class NoSegmenChanged(val noSegmen: String) : SalinRutaEvent()
    data class NoBgFisikChanged(val noBgFisik: Int) : SalinRutaEvent()
    data class NoBgSensusChanged(val noBgSensus: Int) : SalinRutaEvent()
    data class NoUrutKlgChanged(val noUrutKlg: Int) : SalinRutaEvent()
    data class NamaKKChanged(val namaKK: String) : SalinRutaEvent()
    data class AlamatChanged(val alamat: String) : SalinRutaEvent()
    data class IsGenzOrtuChanged(val isGenzOrtu: Int) : SalinRutaEvent()
    data class NoUrutKlgEgbChanged(val noUrutKlgEgb: Int) : SalinRutaEvent()
    data class PenglMknChanged(val penglMkn: Int) : SalinRutaEvent()
    data class NoUrutRutaChanged(val noUrutRuta: Int) : SalinRutaEvent()
    data class KKOrKRTChanged(val kkOrKRT: String) : SalinRutaEvent()
    data class NamaKRTChanged(val namaKRT: String) : SalinRutaEvent()
    data class GenzOrtuChanged(val genzOrtu: Int) : SalinRutaEvent()
    data class KatGenzChanged(val katGenz: Int) : SalinRutaEvent()
    object submit : SalinRutaEvent()
}
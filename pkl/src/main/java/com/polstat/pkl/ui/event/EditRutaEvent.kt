package com.polstat.pkl.ui.event

sealed class EditRutaEvent {
    data class SLSChanged(val sls: String) : EditRutaEvent()
    data class NoSegmenChanged(val noSegmen: String) : EditRutaEvent()
    data class NoBgFisikChanged(val noBgFisik: String) : EditRutaEvent()
    data class NoBgSensusChanged(val noBgSensus: String) : EditRutaEvent()
    data class NoUrutKlgChanged(val noUrutKlg: Int) : EditRutaEvent()
    data class NamaKKChanged(val namaKK: String) : EditRutaEvent()
    data class AlamatChanged(val alamat: String) : EditRutaEvent()
    data class IsGenzOrtuChanged(val isGenzOrtu: Int) : EditRutaEvent()
    data class NoUrutKlgEgbChanged(val noUrutKlgEgb: Int) : EditRutaEvent()
    data class PenglMknChanged(val penglMkn: Int) : EditRutaEvent()
    data class NoUrutRutaChanged(val noUrutRuta: Int) : EditRutaEvent()
    data class KKOrKRTChanged(val kkOrKRT: String) : EditRutaEvent()
    data class NamaKRTChanged(val namaKRT: String) : EditRutaEvent()
    data class GenzOrtuChanged(val genzOrtu: Int) : EditRutaEvent()
    data class KatGenzChanged(val katGenz: Int) : EditRutaEvent()
    object submit : EditRutaEvent()
}
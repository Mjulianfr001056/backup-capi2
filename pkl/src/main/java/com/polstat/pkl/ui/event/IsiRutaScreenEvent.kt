package com.polstat.pkl.ui.event

sealed class IsiRutaScreenEvent {
    data class SLSChanged(val sls: String) : IsiRutaScreenEvent()
    data class NoSegmenChanged(val noSegmen: String) : IsiRutaScreenEvent()
    data class NoBgFisikChanged(val noBgFisik: String) : IsiRutaScreenEvent()
    data class NoBgSensusChanged(val noBgSensus: String) : IsiRutaScreenEvent()
    data class NoUrutKlgChanged(val noUrutKlg: Int) : IsiRutaScreenEvent()
    data class NamaKKChanged(val namaKK: String) : IsiRutaScreenEvent()
    data class AlamatChanged(val alamat: String) : IsiRutaScreenEvent()
    data class IsGenzOrtuChanged(val isGenzOrtu: Int) : IsiRutaScreenEvent()
    data class NoUrutKlgEgbChanged(val noUrutKlgEgb: Int) : IsiRutaScreenEvent()
    data class PenglMknChanged(val penglMkn: Int) : IsiRutaScreenEvent()
    data class NoUrutRutaChanged(val noUrutRuta: Int) : IsiRutaScreenEvent()
    data class KKOrKRTChanged(val kkOrKRT: String) : IsiRutaScreenEvent()
    data class NamaKRTChanged(val namaKRT: String) : IsiRutaScreenEvent()
    data class GenzOrtuChanged(val genzOrtu: String) : IsiRutaScreenEvent()
    data class KatGenzChanged(val katGenz: String) : IsiRutaScreenEvent()
    object submit : IsiRutaScreenEvent()
}
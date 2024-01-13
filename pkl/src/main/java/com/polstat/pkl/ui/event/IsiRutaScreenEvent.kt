package com.polstat.pkl.ui.event

sealed class IsiRutaScreenEvent {
    data class NoSegmenChanged(val noSegmen: String) : IsiRutaScreenEvent()
    data class NoBgFisikChanged(val noBgFisik: String) : IsiRutaScreenEvent()
    data class NoBgSensusChanged(val noBgSensus: String) : IsiRutaScreenEvent()
    data class NoUrutRutaChanged(val noUrutRuta: String) : IsiRutaScreenEvent()
    data class NamaKrtChanged(val namaKrt: String) : IsiRutaScreenEvent()
    data class AlamatChanged(val alamat: String) : IsiRutaScreenEvent()
    data class IsGenzOrtuChanged(val isGenzOrtu: String) : IsiRutaScreenEvent()
    data class JmlGenzChanged(val jmlGenz: String) : IsiRutaScreenEvent()
    data class NoUrutRtEgbChanged(val noUrutRtEgb: String) : IsiRutaScreenEvent()
    data class CatatanChanged(val catatan: String) : IsiRutaScreenEvent()

    object submit : IsiRutaScreenEvent()
}
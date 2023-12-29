package com.polstat.pkl.entity

data class RumahTangga(
    var noSegmen : String, // String 4 digit, contoh pengisian : S001, S012, dll
    var noBgF: String, // Bangunan fisik
    var noBgS: String, // Bangunan sensus
    var noUrutRuta: String, // String 3 digit
    var namaKepalaRuta: String, // String
    var alamat: String, // String
    var noBS: String // Nomor blok sensus
) {
    class Builder {
        var noSegmen : String = ""
        var noBgF: String = ""
        var noBgS: String = ""
        var noUrutRuta: String = ""
        var namaKepalaRuta: String = ""
        var alamat: String = ""
        var noBS: String = ""

        fun noSegmen(noSegmen: String) = apply { this.noSegmen = noSegmen }
        fun noBgF(noBgF: String) = apply { this.noBgF = noBgF }
        fun noBgS(noBgS: String) = apply { this.noBgS = noBgS }
        fun noUrutRuta(noUrutRuta: String) = apply { this.noUrutRuta = noUrutRuta }
        fun namaKepalaRuta(namaKepalaRuta: String) = apply { this.namaKepalaRuta = namaKepalaRuta }
        fun alamat(alamat: String) = apply { this.alamat = alamat }
        fun noBS(noBS: String) = apply { this.noBS = noBS }
        fun build() = RumahTangga(noSegmen, noBgF, noBgS, noUrutRuta, namaKepalaRuta, alamat, noBS)
    }
}
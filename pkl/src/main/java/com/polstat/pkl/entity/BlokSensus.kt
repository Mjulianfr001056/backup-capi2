package com.polstat.pkl.entity

import java.util.Date

/**
 * @author Julian Firdaus
 * @since 09/12/2023
 */
data class BlokSensus(
    var kodeKabupaten : String,
    var kodeKecamatan : String,
    var kodeDesa : String,
    var noBS : String,

    var namaKabupaten : String,
    var namaKecamatan : String,
    var namaDesa : String,
    var namaSLS : String, // "bakalkaya" atau "sukomiskin" gitu contoh isinya

    var jmlArt : Int, // Jumlah anggota rumah tangga
    var jmlArtGenZ : Int, // Jumlah anggota rumah tangga yang memiliki generasi Z
    var jmlGenZAnak : Int, // Jumlah gen Z anak
    var jmlGenZDewasa : Int, // Jumlah gen Z dewasa

    var nim : String, // NIM PCL yang melakukan mencacahan karena satu blok sensus dicacah satu PCL
    var nimPML : String, // NIM PML yang melakukan pengawasan
    var tglCacah : Date,
    var tglPeriksa : Date,

    var catatan : String // Catatan
) {
    companion object {
        const val KODE_PROVINSI = "51"
        const val NAMA_PROVINSI = "Bali"
        const val JENIS_BS = "B" // Status blok sensus, B = Biasa, P = Persiapan, K = Khusus

        const val FLAG_LISTING = "LISTING"
        const val FLAG_SIAP = "SIAP"
        const val FLAG_SAMPLED = "SAMPLED"
        const val FLAG_DIUNGGAH = "DIUNGGAH"
    }

    fun getBlokSensus(): String {
        return KODE_PROVINSI +
                kodeKabupaten +
                kodeKecamatan +
                kodeDesa +
                noBS +
                JENIS_BS
    }
}
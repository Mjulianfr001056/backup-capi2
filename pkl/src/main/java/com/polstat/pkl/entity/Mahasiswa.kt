package com.polstat.pkl.entity

/**
 * @author Julian Firdaus
 * @since 09/12/2023
 *
 */
sealed class Mahasiswa(
    var nim: String,
    var nama: String,
    var noHp: String,
    var email: String,
    var idTim: String
) {
    class Pcl(
        nim: String,
        nama: String,
        noHp: String,
        email: String,
        idTim: String
    ) : Mahasiswa(nim, nama, noHp, email, idTim)

    class Pml(
        nim: String,
        nama: String,
        noHp: String,
        email: String,
        idTim: String
    ) : Mahasiswa(nim, nama, noHp, email, idTim)
}


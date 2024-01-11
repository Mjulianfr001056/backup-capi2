package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.DataTimEntity
import com.polstat.pkl.model.domain.DataTim
import com.polstat.pkl.model.domain.Mahasiswa

fun DataTim.toDataTimEntity() : DataTimEntity {
    return DataTimEntity(
        idTim = idTim,
        namaTim = namaTim,
        passPML = passPML,
        namaPML = namaPML,
        nimPML = nimPML,
        teleponPML = teleponPML
    )
}

fun DataTimEntity.toDataTim(
    anggota: List<Mahasiswa>
) : DataTim {
    return DataTim(
        anggota = anggota,
        idTim = idTim,
        namaTim = namaTim,
        passPML = passPML,
        namaPML = namaPML,
        nimPML = nimPML,
        teleponPML = teleponPML
    )
}
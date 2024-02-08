package com.polstat.pkl.mapper

import com.polstat.pkl.database.entity.AnggotaTimEntity
import com.polstat.pkl.model.domain.AnggotaTim

fun AnggotaTim.toEntity() : AnggotaTimEntity {
    return AnggotaTimEntity(
        nim = nim,
        nama = nama,
        noTlp = noTlp
    )
}
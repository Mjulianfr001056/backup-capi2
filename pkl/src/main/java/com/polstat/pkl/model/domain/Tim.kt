package com.polstat.pkl.model.domain

import com.polstat.pkl.database.entity.DataTimEntity

sealed class Tim(
    open val idTim: String = "[not set]",
    open val namaTim: String = "[not set]",
    open val namaPML: String = "[not set]",
    open val nimPML: String = "[not set]",
    open val teleponPML: String = "[not set]",
) {
    data class Ppl(
        override val idTim: String = "[not set]",
        override val namaTim: String = "[not set]",
        override val namaPML: String = "[not set]",
        override val nimPML: String = "[not set]",
        override val teleponPML: String = "[not set]",
    ) : Tim(idTim, namaTim, namaPML, nimPML) {
        override fun toEntity(): DataTimEntity {
            return DataTimEntity(
                idTim = idTim,
                namaTim = namaTim,
                namaPML = namaPML,
                nimPML = nimPML,
                teleponPML = teleponPML,
                passPML = ""
            )
        }
    }

    data class Pml(
        override val idTim: String = "[not set]",
        override val namaTim: String = "[not set]",
        override val namaPML: String = "[not set]",
        override val nimPML: String = "[not set]",
        val anggota: List<Mahasiswa> = emptyList(),
        val passPML: String = "[not set]",
        override val teleponPML: String = "[not set]",
    ) : Tim(idTim, namaTim, namaPML, nimPML) {
        override fun toEntity(): DataTimEntity {
            return DataTimEntity(
                idTim = idTim,
                namaTim = namaTim,
                namaPML = namaPML,
                nimPML = nimPML,
                teleponPML = teleponPML,
                passPML = passPML
            )
        }
    }

    abstract fun toEntity(): DataTimEntity
}
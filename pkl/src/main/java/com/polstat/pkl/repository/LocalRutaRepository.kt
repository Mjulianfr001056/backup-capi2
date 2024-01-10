package com.polstat.pkl.repository

import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.model.domain.Session
import com.polstat.pkl.model.domain.Wilayah

interface LocalRutaRepository {

    fun createRuta(session: Session, wilayah: Wilayah, ruta: Ruta) : String

    fun updateRuta(session: Session, wilayah: Wilayah, updatedRuta: Ruta) : String

    fun deleteRuta(session: Session, wilayah: Wilayah, deletedRuta: Ruta) : String

}
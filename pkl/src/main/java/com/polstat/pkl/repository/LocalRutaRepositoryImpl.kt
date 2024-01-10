package com.polstat.pkl.repository

import com.polstat.pkl.model.domain.Ruta
import com.polstat.pkl.model.domain.Session
import com.polstat.pkl.model.domain.Wilayah
import javax.inject.Inject

class LocalRutaRepositoryImpl @Inject constructor(
    private val sessionRepository: SessionRepository
) : LocalRutaRepository {

    companion object {
        private const val TAG = "LocalRutaRepoImpl"
    }

    private fun saveSession(session: Session) {
        sessionRepository.saveSession(session)
    }

    override fun createRuta(session: Session, wilayah: Wilayah, ruta: Ruta): String {

        val updatedWilayahList = session.wilayah.map {
            if (it == wilayah) {
                val updatedRutaList = it.ruta.toMutableList()
                updatedRutaList.add(ruta)

                it.copy(ruta = updatedRutaList)
            } else {
                it
            }
        }

        val updatedSession = session.copy(wilayah = updatedWilayahList)

        saveSession(updatedSession)

        return "Ruta berhasil disimpan!"
    }

    override fun updateRuta(session: Session, wilayah: Wilayah, updatedRuta: Ruta): String {

        val updatedWilayahList = session.wilayah.map {
            // Mencari wilayah yang rutanya akan diupdate
            if (it == wilayah) {
                val updatedRutaList = it.ruta.map { existingRuta ->
                    // Mencari ruta yang akan diupdate
                    if (existingRuta.kodeRuta == updatedRuta.kodeRuta) {
                        // Mengupdate nilai atribut-atribut Ruta
                        existingRuta.copy(
                            alamat = updatedRuta.alamat,
                            catatan = updatedRuta.catatan,
                            isGenzOrtu = updatedRuta.isGenzOrtu,
                            jmlGenz = updatedRuta.jmlGenz,
                            lat = updatedRuta.lat,
                            long = updatedRuta.long,
                            namaKrt = updatedRuta.namaKrt,
                            noBS = updatedRuta.noBS,
                            noBgFisik = updatedRuta.noBgFisik,
                            noBgSensus = updatedRuta.noBgSensus,
                            noSegmen = updatedRuta.noSegmen,
                            noUrutRtEgb = updatedRuta.noUrutRtEgb,
                            noUrutRuta = updatedRuta.noUrutRuta,
                            status = "update"
                        )

                    } else {
                        // Membiarkan ruta yang tidak diupdate
                        existingRuta
                    }
                }

                // Memperbarui list ruta dari wilayah
                it.copy(ruta = updatedRutaList)
            } else {
                // Membiarkan wilayah yang rutanya tidak diupdate
                it
            }
        }

        // Memperbarui session
        val updatedSession = session.copy(wilayah = updatedWilayahList)

        saveSession(updatedSession)

        return "Ruta berhasil diupdate!"
    }

    override fun deleteRuta(session: Session, wilayah: Wilayah, deletedRuta: Ruta): String {

        val updatedWilayahList = session.wilayah.map {
            // Mencari wilayah yang rutanya akan diupdate
            if (it == wilayah) {
                val updatedRutaList = it.ruta.map { existingRuta ->
                    // Mencari ruta yang akan diupdate
                    if (existingRuta.kodeRuta == deletedRuta.kodeRuta) {
                        // Mengupdate nilai atribut-atribut Ruta
                        existingRuta.copy(
                            status = "delete"
                        )

                    } else {
                        // Membiarkan ruta yang tidak diupdate
                        existingRuta
                    }
                }

                // Memperbarui list ruta dari wilayah
                it.copy(ruta = updatedRutaList)
            } else {
                // Membiarkan wilayah yang rutanya tidak diupdate
                it
            }
        }

        // Memperbarui session
        val updatedSession = session.copy(wilayah = updatedWilayahList)

        saveSession(updatedSession)

        return "Ruta berhasil dihapus!"

    }

}
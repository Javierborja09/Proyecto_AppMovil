package com.program.diefit

import android.content.Context
import com.program.diefit.data.AppDatabase
import com.program.diefit.data.RutinaDao
import com.program.diefit.entities.Rutina

object RutinaRepository {

    private lateinit var rutinaDao: RutinaDao

    fun init(context: Context) {
        rutinaDao = AppDatabase.getDatabase(context).rutinaDao()
    }

    private fun uid(): Int = UserRepository.usuarioActual?.id ?: 0

    fun cargarDesdeStorage() {
        val uid = uid()
        val rutinas = rutinaDao.getByUsuario(uid)
        if (rutinas.isEmpty()) {
            val iniciales = listOf(
                Rutina(uid, "Fuerza — Pecho y tríceps", "4", "45", "Fuerza"),
                Rutina(uid, "Cardio — Cinta y bicicleta", "2", "30", "Cardio")
            )
            rutinaDao.insertAll(iniciales)
        }
    }

    fun obtenerTodos(): List<Rutina> = rutinaDao.getByUsuario(uid())

    fun agregar(rutina: Rutina) {
        rutinaDao.insert(rutina.copy(usuarioId = uid()))
    }

    fun eliminar(rutina: Rutina) {
        rutinaDao.delete(rutina)
    }

    fun actualizar() {}
}
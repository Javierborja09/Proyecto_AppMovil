package com.program.diefit

import android.content.Context
import com.program.diefit.data.AppDatabase
import com.program.diefit.data.RegistroComidaDao
import com.program.diefit.entities.RegistroComida

object RegistroComidaRepository {

    private lateinit var registroComidaDao: RegistroComidaDao

    fun init(context: Context) {
        registroComidaDao = AppDatabase.getDatabase(context).registroComidaDao()
    }

    private fun uid(): Int = UserRepository.usuarioActual?.id ?: 0

    fun obtenerTodos(): List<RegistroComida> = registroComidaDao.getByUsuario(uid())

    fun obtenerPorFecha(fecha: String): List<RegistroComida> {
        return registroComidaDao.getByFecha(uid(), fecha)
    }

    fun agregar(registro: RegistroComida) {
        registroComidaDao.insert(registro.copy(usuarioId = uid()))
    }

    fun eliminar(registro: RegistroComida) {
        registroComidaDao.delete(registro)
    }

    fun totalCaloriasPorFecha(fecha: String): Int {
        return registroComidaDao.totalCalorias(uid(), fecha) ?: 0
    }
}
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

    fun cargarDesdeStorage() {
        // Ya no es estrictamente necesario cargar listas en memoria con Room
    }

    fun obtenerTodos(): List<RegistroComida> = registroComidaDao.getAll()

    fun obtenerPorFecha(fecha: String): List<RegistroComida> {
        return registroComidaDao.getAll().filter { it.fecha == fecha }
    }

    fun agregar(registro: RegistroComida) {
        registroComidaDao.insert(registro)
    }

    fun eliminar(registro: RegistroComida) {
        registroComidaDao.delete(registro)
    }

    fun totalCaloriasPorFecha(fecha: String): Int {
        return obtenerPorFecha(fecha).sumOf { it.calorias.toDoubleOrNull()?.toInt() ?: 0 }
    }
}
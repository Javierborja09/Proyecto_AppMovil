package com.program.diefit

import android.content.Context
import com.program.diefit.data.AppDatabase
import com.program.diefit.data.RegistroRutinaDao
import com.program.diefit.entities.RegistroRutina
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object RegistroRutinaRepository {

    private lateinit var registroRutinaDao: RegistroRutinaDao
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun init(context: Context) {
        registroRutinaDao = AppDatabase.getDatabase(context).registroRutinaDao()
    }

    fun cargarDesdeStorage() {
        // Ya no es necesario con Room
    }

    fun obtenerTodos(): List<RegistroRutina> {
        return registroRutinaDao.getAll().sortedByDescending {
            formatoFecha.parse(it.fecha)?.time ?: 0L
        }
    }

    fun obtenerPorFecha(fecha: String): List<RegistroRutina> {
        return registroRutinaDao.getAll().filter { it.fecha == fecha }
    }

    fun estaMarcada(fecha: String, rutinaNombre: String): Boolean {
        return registroRutinaDao.getAll().any { it.fecha == fecha && it.rutinaNombre == rutinaNombre }
    }

    fun marcarCumplida(fecha: String, rutinaNombre: String) {
        if (!estaMarcada(fecha, rutinaNombre)) {
            registroRutinaDao.insert(
                RegistroRutina(fecha = fecha, rutinaNombre = rutinaNombre, cumplido = true)
            )
        }
    }

    fun desmarcar(fecha: String, rutinaNombre: String) {
        registroRutinaDao.deleteByFechaAndNombre(fecha, rutinaNombre)
    }

    fun calcularRacha(): Int {
        var racha = 0
        val cal = Calendar.getInstance()
        val todos = registroRutinaDao.getAll()

        while (true) {
            val fechaStr = formatoFecha.format(cal.time)
            val hayCumplida = todos.any { it.fecha == fechaStr }
            if (hayCumplida) {
                racha++
                cal.add(Calendar.DAY_OF_MONTH, -1)
            } else {
                break
            }
        }
        return racha
    }

    fun fechasConRegistro(): List<String> {
        return registroRutinaDao.getAll().map { it.fecha }.distinct().sortedByDescending {
            formatoFecha.parse(it)?.time ?: 0L
        }
    }
}
package com.program.diefit

import com.program.diefit.entities.RegistroRutina
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object RegistroRutinaRepository {

    private val registros = mutableListOf<RegistroRutina>()
    private val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun cargarDesdeStorage() {
        registros.clear()
        registros.addAll(RegistroRutinaStorage.cargarRegistros())
    }

    fun obtenerTodos(): List<RegistroRutina> = registros.sortedByDescending {
        formatoFecha.parse(it.fecha)?.time ?: 0L
    }

    fun obtenerPorFecha(fecha: String): List<RegistroRutina> {
        return registros.filter { it.fecha == fecha }
    }

    fun estaMarcada(fecha: String, rutinaNombre: String): Boolean {
        return registros.any { it.fecha == fecha && it.rutinaNombre == rutinaNombre }
    }

    fun marcarCumplida(fecha: String, rutinaNombre: String) {
        if (!estaMarcada(fecha, rutinaNombre)) {
            registros.add(RegistroRutina(fecha = fecha, rutinaNombre = rutinaNombre, cumplido = true))
            RegistroRutinaStorage.guardarRegistros(registros)
        }
    }

    fun desmarcar(fecha: String, rutinaNombre: String) {
        registros.removeAll { it.fecha == fecha && it.rutinaNombre == rutinaNombre }
        RegistroRutinaStorage.guardarRegistros(registros)
    }

    // Racha: días consecutivos hacia atrás desde hoy con al menos 1 rutina cumplida
    fun calcularRacha(): Int {
        var racha = 0
        val cal = Calendar.getInstance()

        while (true) {
            val fechaStr = formatoFecha.format(cal.time)
            val hayCumplida = registros.any { it.fecha == fechaStr }
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
        return registros.map { it.fecha }.distinct().sortedByDescending {
            formatoFecha.parse(it)?.time ?: 0L
        }
    }
}
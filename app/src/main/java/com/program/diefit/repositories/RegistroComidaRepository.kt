package com.program.diefit

import com.program.diefit.entities.RegistroComida

object RegistroComidaRepository {

    private val registros = mutableListOf<RegistroComida>()

    fun cargarDesdeStorage() {
        registros.clear()
        registros.addAll(RegistroComidaStorage.cargarRegistros())
    }

    fun obtenerTodos(): List<RegistroComida> = registros

    fun obtenerPorFecha(fecha: String): List<RegistroComida> {
        return registros.filter { it.fecha == fecha }
    }

    fun agregar(registro: RegistroComida) {
        registros.add(registro)
        RegistroComidaStorage.guardarRegistros(registros)
    }

    fun eliminar(registro: RegistroComida) {
        registros.remove(registro)
        RegistroComidaStorage.guardarRegistros(registros)
    }

    fun totalCaloriasPorFecha(fecha: String): Int {
        return obtenerPorFecha(fecha).sumOf { it.calorias.toDoubleOrNull()?.toInt() ?: 0 }
    }
}
package com.program.diefit

import com.program.diefit.entities.Rutina

object RutinaRepository {

    private val rutinas = mutableListOf<Rutina>()

    fun cargarDesdeStorage() {
        rutinas.clear()
        rutinas.addAll(RutinaStorage.cargarRutinas())

        if (rutinas.isEmpty()) {
            rutinas.add(Rutina("Fuerza — Pecho y tríceps", "4", "45", "Fuerza"))
            rutinas.add(Rutina("Cardio — Cinta y bicicleta", "2", "30", "Cardio"))
            RutinaStorage.guardarRutinas(rutinas)
        }
    }

    fun obtenerTodos(): List<Rutina> = rutinas

    fun agregar(rutina: Rutina) {
        rutinas.add(rutina)
        RutinaStorage.guardarRutinas(rutinas)
    }

    fun eliminar(rutina: Rutina) {
        rutinas.remove(rutina)
        RutinaStorage.guardarRutinas(rutinas)
    }

    fun actualizar() {
        RutinaStorage.guardarRutinas(rutinas)
    }
}
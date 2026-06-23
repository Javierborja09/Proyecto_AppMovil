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

    fun cargarDesdeStorage() {
        val rutinas = rutinaDao.getAll()
        if (rutinas.isEmpty()) {
            val iniciales = listOf(
                Rutina("Fuerza — Pecho y tríceps", "4", "45", "Fuerza"),
                Rutina("Cardio — Cinta y bicicleta", "2", "30", "Cardio")
            )
            rutinaDao.insertAll(iniciales)
        }
    }

    fun obtenerTodos(): List<Rutina> = rutinaDao.getAll()

    fun agregar(rutina: Rutina) {
        rutinaDao.insert(rutina)
    }

    fun eliminar(rutina: Rutina) {
        rutinaDao.delete(rutina)
    }

    fun actualizar() {
        // En Room, las actualizaciones se manejan re-insertando el objeto con el mismo ID,
        // por lo que este método puede quedar vacío si no se usa directamente para otra lógica.
    }
}
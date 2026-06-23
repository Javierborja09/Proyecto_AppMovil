package com.program.diefit.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registro_rutina")
data class RegistroRutina(
    val usuarioId: Int,
    val fecha: String,
    val rutinaNombre: String,
    val cumplido: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
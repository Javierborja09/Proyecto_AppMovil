package com.program.diefit.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rutinas")
data class Rutina(
    val nombre: String,
    val dias: String,
    val duracion: String,
    val tipo: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
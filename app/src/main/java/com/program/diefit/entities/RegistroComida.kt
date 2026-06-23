package com.program.diefit.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registro_comida")
data class RegistroComida(
    @PrimaryKey
    var id: String = System.currentTimeMillis().toString(),
    var fecha: String,
    var nombreProducto: String,
    var cantidad: String,
    var unidad: String,
    var calorias: String,
    var proteinas: String,
    var carbohidratos: String,
    var grasas: String
)
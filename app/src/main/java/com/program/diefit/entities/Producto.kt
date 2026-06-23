package com.program.diefit.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    val usuarioId: Int,
    val nombre: String,
    val cantidad: String,
    val unidad: String,
    val calorias: String,
    val proteinas: String,
    val carbohidratos: String,
    val grasas: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
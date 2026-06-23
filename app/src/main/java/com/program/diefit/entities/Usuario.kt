package com.program.diefit.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    var nombre: String,
    var email: String,
    var password: String,
    var edad: String = "",
    var peso: String = "",
    var talla: String = "",
    var meta: String = "",
    var genero: String = "",
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
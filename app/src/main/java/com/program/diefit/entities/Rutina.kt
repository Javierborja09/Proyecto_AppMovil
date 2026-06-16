package com.program.diefit.entities

data class Rutina(
    var nombre: String,
    var ejercicios: String = "0",
    var duracion: String = "0",
    var tipo: String = "Fuerza"
)
package com.program.diefit.entities

data class RegistroRutina(
    var id: String = System.currentTimeMillis().toString(),
    var fecha: String,
    var rutinaNombre: String,
    var cumplido: Boolean = true
)
package com.program.diefit.entities

data class RegistroComida(
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
package com.program.diefit.entities

data class Producto(
    var nombre: String,
    var cantidad: String = "100",
    var unidad: String = "g",
    var calorias: String = "",
    var proteinas: String = "",
    var carbohidratos: String = "",
    var grasas: String = ""
)
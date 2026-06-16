package com.program.diefit.entities

data class Usuario(
    var nombre: String,
    var email: String,
    var password: String,
    var edad: String = "",
    var peso: String = "",
    var talla: String = "",
    var meta: String = "",
    var genero: String = ""
)
package com.program.diefit

import com.program.diefit.entities.Usuario


object UserRepository {

    private val usuarios = mutableListOf<Usuario>()
    var usuarioActual: Usuario? = null

    fun cargarDesdeStorage() {
        usuarios.clear()
        usuarios.addAll(UserStorage.cargarUsuarios())

        if (usuarios.none { it.email == "admin@diefit.com" }) {
            usuarios.add(Usuario("Admin", "admin@diefit.com", "123456"))
            UserStorage.guardarUsuarios(usuarios)
        }

        val emailSesion = UserStorage.obtenerSesion()
        if (emailSesion != null) {
            usuarioActual = usuarios.find { it.email == emailSesion }
        }
    }

    fun registrar(nombre: String, email: String, password: String): Result {
        if (usuarios.any { it.email == email }) {
            return Result.ERROR_EMAIL_EXISTE
        }
        val nuevo = Usuario(nombre, email, password)
        usuarios.add(nuevo)
        usuarioActual = nuevo
        UserStorage.guardarUsuarios(usuarios)
        UserStorage.guardarSesion(email)
        return Result.EXITO
    }

    fun login(email: String, password: String): Boolean {
        val user = usuarios.find { it.email == email && it.password == password }
        return if (user != null) {
            usuarioActual = user
            UserStorage.guardarSesion(email)
            true
        } else {
            false
        }
    }

    fun buscarPorEmail(email: String): Usuario? {
        return usuarios.find { it.email == email }
    }

    fun actualizarUsuarioActual() {
        UserStorage.guardarUsuarios(usuarios)
    }

    fun cerrarSesion() {
        usuarioActual = null
        UserStorage.cerrarSesion()
    }

    enum class Result {
        EXITO,
        ERROR_EMAIL_EXISTE
    }
}
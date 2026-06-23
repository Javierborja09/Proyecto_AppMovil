package com.program.diefit

import android.content.Context
import com.program.diefit.data.AppDatabase
import com.program.diefit.data.UsuarioDao
import com.program.diefit.data.SessionStorage
import com.program.diefit.entities.Usuario

object UserRepository {

    private lateinit var usuarioDao: UsuarioDao
    var usuarioActual: Usuario? = null

    fun init(context: Context) {
        usuarioDao = AppDatabase.getDatabase(context).usuarioDao()
    }

    fun loginOCrearConGoogle(email: String, nombre: String): Boolean {
        var usuario = usuarioDao.getUsuarioByEmail(email)
        if (usuario == null) {
            usuario = Usuario(
                nombre = nombre,
                email = email,
                password = ""
            )
            usuarioDao.insert(usuario)
            usuario = usuarioDao.getUsuarioByEmail(email)
        }
        usuarioActual = usuario
        usuario?.let { SessionStorage.guardarSesion(it.email) }
        return usuario != null
    }

    fun cargarDesdeStorage() {
        if (usuarioDao.getUsuarioByEmail("admin@diefit.com") == null) {
            usuarioDao.insert(Usuario("Admin", "admin@diefit.com", "123456"))
        }

        val emailSesion = SessionStorage.obtenerSesion()
        if (emailSesion != null) {
            usuarioActual = usuarioDao.getUsuarioByEmail(emailSesion)
        }
    }

    fun registrar(nombre: String, email: String, password: String): Result {
        if (usuarioDao.getUsuarioByEmail(email) != null) {
            return Result.ERROR_EMAIL_EXISTE
        }
        val nuevo = Usuario(nombre, email, password)
        usuarioDao.insert(nuevo)
        usuarioActual = nuevo
        SessionStorage.guardarSesion(email)
        return Result.EXITO
    }

    fun login(email: String, password: String): Boolean {
        val user = usuarioDao.getUsuarioByEmail(email)
        return if (user != null && user.password == password) {
            usuarioActual = user
            SessionStorage.guardarSesion(email)
            true
        } else {
            false
        }
    }

    fun buscarPorEmail(email: String): Usuario? {
        return usuarioDao.getUsuarioByEmail(email)
    }

    fun actualizarUsuarioActual() {
        usuarioActual?.let {
            usuarioDao.insert(it)
        }
    }

    fun cerrarSesion() {
        usuarioActual = null
        SessionStorage.cerrarSesion()
    }

    enum class Result {
        EXITO,
        ERROR_EMAIL_EXISTE
    }
}
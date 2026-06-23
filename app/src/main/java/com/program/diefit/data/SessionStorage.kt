package com.program.diefit.data

import android.content.Context
import android.content.SharedPreferences

object SessionStorage {

    private const val PREFS_NAME = "diefit_sesion_prefs"
    private const val KEY_SESION_EMAIL = "sesion_email"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun guardarSesion(email: String) {
        prefs.edit().putString(KEY_SESION_EMAIL, email).apply()
    }

    fun obtenerSesion(): String? {
        return prefs.getString(KEY_SESION_EMAIL, null)
    }

    fun cerrarSesion() {
        prefs.edit().remove(KEY_SESION_EMAIL).apply()
    }
}
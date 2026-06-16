package com.program.diefit

import android.content.Context
import android.content.SharedPreferences
import com.program.diefit.entities.Usuario
import org.json.JSONArray
import org.json.JSONObject

object UserStorage {

    private const val PREFS_NAME = "diefit_prefs"
    private const val KEY_USUARIOS = "usuarios"
    private const val KEY_SESION_EMAIL = "sesion_email"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun guardarUsuarios(usuarios: List<Usuario>) {
        val array = JSONArray()
        usuarios.forEach { u ->
            val obj = JSONObject()
            obj.put("nombre", u.nombre)
            obj.put("email", u.email)
            obj.put("password", u.password)
            obj.put("edad", u.edad)
            obj.put("peso", u.peso)
            obj.put("talla", u.talla)
            obj.put("meta", u.meta)
            obj.put("genero", u.genero)
            array.put(obj)
        }
        prefs.edit().putString(KEY_USUARIOS, array.toString()).apply()
    }

    fun cargarUsuarios(): MutableList<Usuario> {
        val json = prefs.getString(KEY_USUARIOS, null) ?: return mutableListOf()
        val array = JSONArray(json)
        val lista = mutableListOf<Usuario>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            lista.add(
                Usuario(
                    nombre = obj.getString("nombre"),
                    email = obj.getString("email"),
                    password = obj.getString("password"),
                    edad = obj.optString("edad", ""),
                    peso = obj.optString("peso", ""),
                    talla = obj.optString("talla", ""),
                    meta = obj.optString("meta", ""),
                    genero = obj.optString("genero", "")
                )
            )
        }
        return lista
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
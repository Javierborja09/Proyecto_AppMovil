package com.program.diefit

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import com.program.diefit.entities.Rutina

object RutinaStorage {

    private const val PREFS_NAME = "diefit_rutinas_prefs"
    private const val KEY_RUTINAS = "rutinas"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun guardarRutinas(rutinas: List<Rutina>) {
        val array = JSONArray()
        rutinas.forEach { r ->
            val obj = JSONObject()
            obj.put("nombre", r.nombre)
            obj.put("ejercicios", r.ejercicios)
            obj.put("duracion", r.duracion)
            obj.put("tipo", r.tipo)
            array.put(obj)
        }
        prefs.edit().putString(KEY_RUTINAS, array.toString()).apply()
    }

    fun cargarRutinas(): MutableList<Rutina> {
        val json = prefs.getString(KEY_RUTINAS, null) ?: return mutableListOf()
        val array = JSONArray(json)
        val lista = mutableListOf<Rutina>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            lista.add(
                Rutina(
                    nombre = obj.getString("nombre"),
                    ejercicios = obj.optString("ejercicios", "0"),
                    duracion = obj.optString("duracion", "0"),
                    tipo = obj.optString("tipo", "Fuerza")
                )
            )
        }
        return lista
    }
}
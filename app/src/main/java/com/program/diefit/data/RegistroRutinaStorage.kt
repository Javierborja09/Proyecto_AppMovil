package com.program.diefit

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import com.program.diefit.entities.RegistroRutina

object RegistroRutinaStorage {

    private const val PREFS_NAME = "diefit_registros_rutina_prefs"
    private const val KEY_REGISTROS = "registros_rutina"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun guardarRegistros(registros: List<RegistroRutina>) {
        val array = JSONArray()
        registros.forEach { r ->
            val obj = JSONObject()
            obj.put("id", r.id)
            obj.put("fecha", r.fecha)
            obj.put("rutinaNombre", r.rutinaNombre)
            obj.put("cumplido", r.cumplido)
            array.put(obj)
        }
        prefs.edit().putString(KEY_REGISTROS, array.toString()).apply()
    }

    fun cargarRegistros(): MutableList<RegistroRutina> {
        val json = prefs.getString(KEY_REGISTROS, null) ?: return mutableListOf()
        val array = JSONArray(json)
        val lista = mutableListOf<RegistroRutina>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            lista.add(
                RegistroRutina(
                    id = obj.optString("id", System.currentTimeMillis().toString()),
                    fecha = obj.getString("fecha"),
                    rutinaNombre = obj.getString("rutinaNombre"),
                    cumplido = obj.optBoolean("cumplido", true)
                )
            )
        }
        return lista
    }
}
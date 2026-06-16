package com.program.diefit

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import com.program.diefit.entities.RegistroComida

object RegistroComidaStorage {

    private const val PREFS_NAME = "diefit_registros_prefs"
    private const val KEY_REGISTROS = "registros"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun guardarRegistros(registros: List<RegistroComida>) {
        val array = JSONArray()
        registros.forEach { r ->
            val obj = JSONObject()
            obj.put("id", r.id)
            obj.put("fecha", r.fecha)
            obj.put("nombreProducto", r.nombreProducto)
            obj.put("cantidad", r.cantidad)
            obj.put("unidad", r.unidad)
            obj.put("calorias", r.calorias)
            obj.put("proteinas", r.proteinas)
            obj.put("carbohidratos", r.carbohidratos)
            obj.put("grasas", r.grasas)
            array.put(obj)
        }
        prefs.edit().putString(KEY_REGISTROS, array.toString()).apply()
    }

    fun cargarRegistros(): MutableList<RegistroComida> {
        val json = prefs.getString(KEY_REGISTROS, null) ?: return mutableListOf()
        val array = JSONArray(json)
        val lista = mutableListOf<RegistroComida>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            lista.add(
                RegistroComida(
                    id = obj.optString("id", System.currentTimeMillis().toString()),
                    fecha = obj.getString("fecha"),
                    nombreProducto = obj.getString("nombreProducto"),
                    cantidad = obj.getString("cantidad"),
                    unidad = obj.getString("unidad"),
                    calorias = obj.getString("calorias"),
                    proteinas = obj.getString("proteinas"),
                    carbohidratos = obj.getString("carbohidratos"),
                    grasas = obj.getString("grasas")
                )
            )
        }
        return lista
    }
}
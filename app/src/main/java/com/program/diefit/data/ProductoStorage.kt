package com.program.diefit.data

import android.content.Context
import android.content.SharedPreferences
import com.program.diefit.entities.Producto
import org.json.JSONArray
import org.json.JSONObject

object ProductoStorage {

    private const val PREFS_NAME = "diefit_productos_prefs"
    private const val KEY_PRODUCTOS = "productos"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun guardarProductos(productos: List<Producto>) {
        val array = JSONArray()
        productos.forEach { p ->
            val obj = JSONObject()
            obj.put("nombre", p.nombre)
            obj.put("cantidad", p.cantidad)
            obj.put("unidad", p.unidad)
            obj.put("calorias", p.calorias)
            obj.put("proteinas", p.proteinas)
            obj.put("carbohidratos", p.carbohidratos)
            obj.put("grasas", p.grasas)
            array.put(obj)
        }
        prefs.edit().putString(KEY_PRODUCTOS, array.toString()).apply()
    }

    fun cargarProductos(): MutableList<Producto> {
        val json = prefs.getString(KEY_PRODUCTOS, null) ?: return mutableListOf()
        val array = JSONArray(json)
        val lista = mutableListOf<Producto>()
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            lista.add(
                Producto(
                    nombre = obj.getString("nombre"),
                    cantidad = obj.optString("cantidad", "100"),
                    unidad = obj.optString("unidad", "g"),
                    calorias = obj.optString("calorias", ""),
                    proteinas = obj.optString("proteinas", ""),
                    carbohidratos = obj.optString("carbohidratos", ""),
                    grasas = obj.optString("grasas", "")
                )
            )
        }
        return lista
    }
}
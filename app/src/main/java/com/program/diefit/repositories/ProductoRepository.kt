package com.program.diefit

import android.content.Context
import com.program.diefit.data.AppDatabase
import com.program.diefit.data.ProductoDao
import com.program.diefit.entities.Producto

object ProductoRepository {

    private lateinit var productoDao: ProductoDao

    fun init(context: Context) {
        productoDao = AppDatabase.getDatabase(context).productoDao()
    }

    fun cargarDesdeStorage() {
        val productos = productoDao.getAll()
        if (productos.isEmpty()) {
            val iniciales = listOf(
                Producto("Pechuga de pollo", "100", "g", "165", "31", "0", "3.6"),
                Producto("Arroz blanco", "100", "g", "130", "2.7", "28", "0.3")
            )
            productoDao.insertAll(iniciales)
        }
    }

    fun obtenerTodos(): List<Producto> {
        return productoDao.getAll()
    }

    fun buscar(query: String): List<Producto> {
        val productos = productoDao.getAll()
        if (query.isBlank()) return productos
        return productos.filter { it.nombre.contains(query, ignoreCase = true) }
    }

    fun agregar(producto: Producto) {
        productoDao.insert(producto)
    }

    fun eliminar(producto: Producto) {
        productoDao.delete(producto)
    }

    fun actualizar() {
    }
}
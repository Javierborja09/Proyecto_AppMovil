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

    private fun uid(): Int = UserRepository.usuarioActual?.id ?: 0

    fun cargarDesdeStorage() {
        val uid = uid()
        val productos = productoDao.getByUsuario(uid)
        if (productos.isEmpty()) {
            val iniciales = listOf(
                Producto(uid, "Pechuga de pollo", "100", "g", "165", "31", "0", "3.6"),
                Producto(uid, "Arroz blanco", "100", "g", "130", "2.7", "28", "0.3")
            )
            productoDao.insertAll(iniciales)
        }
    }

    fun obtenerTodos(): List<Producto> = productoDao.getByUsuario(uid())

    fun buscar(query: String): List<Producto> {
        val productos = productoDao.getByUsuario(uid())
        if (query.isBlank()) return productos
        return productos.filter { it.nombre.contains(query, ignoreCase = true) }
    }

    fun agregar(producto: Producto) {
        productoDao.insert(producto.copy(usuarioId = uid()))
    }

    fun eliminar(producto: Producto) {
        productoDao.delete(producto)
    }

    fun actualizar() {}
}
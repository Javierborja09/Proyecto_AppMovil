package com.program.diefit

import com.program.diefit.data.ProductoStorage
import com.program.diefit.entities.Producto

object ProductoRepository {

    private val productos = mutableListOf<Producto>()

    fun cargarDesdeStorage() {
        productos.clear()
        productos.addAll(ProductoStorage.cargarProductos())

        if (productos.isEmpty()) {
            productos.add(Producto("Pechuga de pollo", "100", "g", "165", "31", "0", "3.6"))
            productos.add(Producto("Arroz blanco", "100", "g", "130", "2.7", "28", "0.3"))
            ProductoStorage.guardarProductos(productos)
        }
    }

    fun obtenerTodos(): List<Producto> = productos

    fun buscar(query: String): List<Producto> {
        if (query.isBlank()) return productos
        return productos.filter { it.nombre.contains(query, ignoreCase = true) }
    }

    fun agregar(producto: Producto) {
        productos.add(producto)
        ProductoStorage.guardarProductos(productos)
    }

    fun eliminar(producto: Producto) {
        productos.remove(producto)
        ProductoStorage.guardarProductos(productos)
    }

    fun actualizar() {
        ProductoStorage.guardarProductos(productos)
    }
}
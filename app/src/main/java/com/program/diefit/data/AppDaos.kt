package com.program.diefit.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.program.diefit.entities.*
@Dao
interface ProductoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(productos: List<Producto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(producto: Producto)

    @Delete
    fun delete(producto: Producto)

    @Query("SELECT * FROM productos WHERE usuarioId = :usuarioId")
    fun getByUsuario(usuarioId: Int): List<Producto>
}

@Dao
interface RegistroComidaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(registro: RegistroComida)

    @Delete
    fun delete(registro: RegistroComida)

    @Query("SELECT * FROM registro_comida WHERE usuarioId = :usuarioId AND fecha = :fecha")
    fun getByFecha(usuarioId: Int, fecha: String): List<RegistroComida>

    @Query("SELECT * FROM registro_comida WHERE usuarioId = :usuarioId")
    fun getByUsuario(usuarioId: Int): List<RegistroComida>

    @Query("SELECT SUM(CAST(calorias AS INTEGER)) FROM registro_comida WHERE usuarioId = :usuarioId AND fecha = :fecha")
    fun totalCalorias(usuarioId: Int, fecha: String): Int?
}

@Dao
interface RegistroRutinaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(registro: RegistroRutina)

    @Query("DELETE FROM registro_rutina WHERE usuarioId = :usuarioId AND fecha = :fecha AND rutinaNombre = :rutinaNombre")
    fun deleteByFechaAndNombre(usuarioId: Int, fecha: String, rutinaNombre: String)

    @Query("SELECT * FROM registro_rutina WHERE usuarioId = :usuarioId")
    fun getAll(usuarioId: Int): List<RegistroRutina>

    @Query("SELECT COUNT(*) > 0 FROM registro_rutina WHERE usuarioId = :usuarioId AND fecha = :fecha AND rutinaNombre = :rutinaNombre AND cumplido = 1")
    fun estaMarcada(usuarioId: Int, fecha: String, rutinaNombre: String): Boolean
}

@Dao
interface RutinaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(rutinas: List<Rutina>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rutina: Rutina)

    @Delete
    fun delete(rutina: Rutina)

    @Query("SELECT * FROM rutinas WHERE usuarioId = :usuarioId")
    fun getByUsuario(usuarioId: Int): List<Rutina>
}

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(usuario: Usuario)

    @Query("SELECT * FROM usuarios")
    fun getAll(): List<Usuario>

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    fun getUsuarioByEmail(email: String): Usuario?
}
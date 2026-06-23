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

    @Query("SELECT * FROM productos")
    fun getAll(): List<Producto>
}

@Dao
interface RegistroComidaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(registros: List<RegistroComida>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(registro: RegistroComida)

    @Delete
    fun delete(registro: RegistroComida)

    @Query("SELECT * FROM registro_comida")
    fun getAll(): List<RegistroComida>
}

@Dao
interface RegistroRutinaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(registros: List<RegistroRutina>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(registro: RegistroRutina)

    @Query("DELETE FROM registro_rutina WHERE fecha = :fecha AND rutinaNombre = :rutinaNombre")
    fun deleteByFechaAndNombre(fecha: String, rutinaNombre: String)

    @Query("SELECT * FROM registro_rutina")
    fun getAll(): List<RegistroRutina>
}

@Dao
interface RutinaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(rutinas: List<Rutina>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(rutina: Rutina)

    @Delete
    fun delete(rutina: Rutina)

    @Query("SELECT * FROM rutinas")
    fun getAll(): List<Rutina>
}

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(usuarios: List<Usuario>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(usuario: Usuario)

    @Query("SELECT * FROM usuarios")
    fun getAll(): List<Usuario>

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    fun getUsuarioByEmail(email: String): Usuario?
}
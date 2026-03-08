package com.example.techaudit.data

import androidx.contentpager.content.Query
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.example.techaudit.model.AuditItem

@Dao
interface AuditDao {

    //Traer todos los equipos ordenados por fecha

    @Query("SELECT * FROM equipos ORDER BY fechaRegistro DESC")
    suspend fun getAllItems() : List<AuditItem>

    //Buscar uno solo por ID
    @Query("SELECT * FROM equipos WHERE id = :id")
    suspend fun getById(id: String) : AuditItem

    //Insertar un nuevo equipo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AuditItem)

    //Actualizar un equipo
    @Update
    suspend fun update(item: AuditItem)

    //Borrar
    @Delete
    suspend fun delete(item: AuditItem)

}
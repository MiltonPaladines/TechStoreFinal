package com.example.techaudit20.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.example.techaudit20.model.AuditItem
import com.example.techaudit20.model.Laboratorio
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditDao {

    @Query("SELECT * FROM laboratorios ORDER BY nombre ASC")
    fun getAllLaboratorios(): kotlinx.coroutines.flow.Flow<List<Laboratorio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaboratorio(lab: Laboratorio)

    @Delete
    suspend fun deleteLaboratorio(lab: Laboratorio)


    @Query("SELECT * FROM equipos WHERE laboratorioId = :labId ORDER BY fechaRegistro DESC")
    fun getEquiposByLaboratorio(labId: String): kotlinx.coroutines.flow.Flow<List<AuditItem>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AuditItem)

    @Update
    suspend fun update(item: AuditItem)

    @Delete
    suspend fun delete(item: AuditItem)

    // Para la sincronización (obtener todos sin filtro)
    @Query("SELECT * FROM equipos")
    suspend fun getAllEquiposStatic(): List<AuditItem>
}

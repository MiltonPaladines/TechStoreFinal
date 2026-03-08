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

    // --- SECCIÓN DE LABORATORIOS ---

    @Query("SELECT * FROM laboratorios ORDER BY nombre ASC")
    fun getAllLaboratorios(): Flow<List<Laboratorio>>


    @Query("SELECT * FROM laboratorios")
    suspend fun getAllLaboratoriosStatic(): List<Laboratorio>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaboratorio(lab: Laboratorio)

    @Delete
    suspend fun deleteLaboratorio(lab: Laboratorio)


    // --- SECCIÓN DE EQUIPOS ---

    // 1. Consulta para la UI: Solo muestra los que NO están marcados para borrar
    @Query("SELECT * FROM equipos WHERE laboratorioId = :labId AND eliminado = 0 ORDER BY fechaRegistro DESC")
    fun getEquiposByLaboratorio(labId: String): Flow<List<AuditItem>>

    // 2. Inserción y Actualización (OnConflict REPLACE es vital para la sincronización)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AuditItem)

    @Update
    suspend fun update(item: AuditItem)

    // 3. Borrado Lógico: Lo usamos en el Swipe para ocultarlo sin borrarlo de la DB aún
    @Update
    suspend fun marcarComoEliminado(equipo: AuditItem)

    // 4. Borrado Físico: Lo usamos en sincronizarDatos() tras confirmar con MockAPI
    @Delete
    suspend fun borradoFisico(equipo: AuditItem)

    // 5. Para Sincronización: Obtiene todos los registros (incluyendo eliminados) sin ser Flow
    @Query("SELECT * FROM equipos")
    suspend fun getAllEquiposStatic(): List<AuditItem>

}

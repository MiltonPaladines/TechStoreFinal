package com.example.techaudit20.data

import com.example.techaudit20.model.AuditItem
import com.example.techaudit20.model.Laboratorio
import kotlinx.coroutines.flow.Flow

class AuditRepository(private val auditDao: AuditDao) {

    val allLaboratorios: Flow<List<Laboratorio>> = auditDao.getAllLaboratorios()

    suspend fun insertLaboratorio(lab: Laboratorio) = auditDao.insertLaboratorio(lab)

    fun getEquiposByLab(labId: String): Flow<List<AuditItem>> = auditDao.getEquiposByLaboratorio(labId)

    suspend fun insert(item: AuditItem) = auditDao.insert(item)

    suspend fun update(item: AuditItem) = auditDao.update(item)

    suspend fun marcarComoEliminado(item: AuditItem) = auditDao.marcarComoEliminado(item)

  }
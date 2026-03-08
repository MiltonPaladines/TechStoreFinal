package com.example.techaudit20.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.techaudit20.model.AuditItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.techaudit20.TechAuditApp
import com.example.techaudit20.data.AuditRepository
import com.example.techaudit20.model.Laboratorio
import kotlinx.coroutines.launch

class AuditViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AuditRepository


    val allLaboratorios: LiveData<List<Laboratorio>>

    init {
        val dao = (application as TechAuditApp).database.auditDao()
        repository = AuditRepository(auditDao = dao)
        allLaboratorios = repository.allLaboratorios.asLiveData()
    }


    fun insertLaboratorio(lab: Laboratorio) = viewModelScope.launch {
        repository.insertLaboratorio(lab)
    }


    fun getEquiposByLab(labId: String): LiveData<List<AuditItem>> {
        return repository.getEquiposByLab(labId).asLiveData()
    }

    fun insert(item: AuditItem) = viewModelScope.launch {
        repository.insert(item)
    }

    fun update(item: AuditItem) = viewModelScope.launch {
        repository.update(item)
    }

    fun delete(item: AuditItem) = viewModelScope.launch {
        repository.delete(item)
    }
}
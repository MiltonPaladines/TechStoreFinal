package com.example.techaudit20.network

import com.example.techaudit20.model.AuditItem
import com.example.techaudit20.model.Laboratorio
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("laboratorios")
    suspend fun sincronizarLabs(@Body labs: List<Laboratorio>): Response<Unit>

    @POST("equipos")
    suspend fun sincronizarEquipos(@Body equipos: List<AuditItem>): Response<Unit>
}
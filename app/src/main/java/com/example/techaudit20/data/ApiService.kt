package com.example.techaudit20.network

import com.example.techaudit20.model.AuditItem
import com.example.techaudit20.model.Laboratorio
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("laboratorios")
    suspend fun crearLaboratorio(@Body lab: Laboratorio): Response<Laboratorio>

    @PUT("laboratorios/{id}")
    suspend fun actualizarLaboratorio(@Path("id") id: String, @Body lab: Laboratorio): Response<Laboratorio>

    @POST("equipos")
    suspend fun crearEquipo(@Body equipo: AuditItem): Response<AuditItem>

    @GET("laboratorios")
    suspend fun obtenerLaboratorios(): Response<List<Laboratorio>>

    @GET("equipos")
    suspend fun obtenerEquipos(): Response<List<AuditItem>>

    @PUT("equipos/{id}")
    suspend fun actualizarEquipo(@Path("id") id: String, @Body item: AuditItem): Response<AuditItem>

    @DELETE("equipos/{id}")
    suspend fun eliminarEquipo(@Path("id") id: String): Response<Unit>
}
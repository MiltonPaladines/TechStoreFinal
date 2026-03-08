package com.example.techaudit.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class AuditStatus {
    PENDIENTE,
    OPERATIVO,
    DANIADO,
    NO_ENCONTRADO
}

@Entity(tableName = "equipos")
@Parcelize
data class AuditItem (

    @PrimaryKey
    val id: String,

    val nombre:String,
    val ubicacion: String,
    val fechaRegistro: String,
    var estado: AuditStatus = AuditStatus.PENDIENTE,
    var notas: String = "",
    var fotoUri: String ?= null

):Parcelable
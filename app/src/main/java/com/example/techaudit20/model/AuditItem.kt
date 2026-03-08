package com.example.techaudit20.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "equipos",
    foreignKeys = [
        ForeignKey(
            entity = Laboratorio::class,
            parentColumns = ["id"],
            childColumns = ["laboratorioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["laboratorioId"])]
)
@Parcelize
data class AuditItem(
    @PrimaryKey val id: String,
    val laboratorioId: String,
    val nombre: String,
    val ubicacion: String,
    val fechaRegistro: String,
    var estado: AuditStatus = AuditStatus.PENDIENTE,
    var notas: String = "",
    var fotoUri: String? = null,
    val eliminado: Boolean = false
) : Parcelable
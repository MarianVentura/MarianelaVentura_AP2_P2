package edu.ucne.marianelaventura_ap2_p2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Gastos")
data class GastosEntity (
    @PrimaryKey(autoGenerate = true)
    val gastoId: Int? = null,
    val fecha: String = "",
    val suplidor: String = "",
    val ncf: String = "",
    val itbis: Double = 0.0,
    val monto: Double = 0.0
)
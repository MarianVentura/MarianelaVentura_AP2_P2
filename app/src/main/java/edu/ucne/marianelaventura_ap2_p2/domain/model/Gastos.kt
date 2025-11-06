package edu.ucne.marianelaventura_ap2_p2.domain.model

data class Gasto(
    val gastoId: Int? = null,
    val fecha: String = "",
    val suplidor: String = "",
    val ncf: String = "",
    val itbis: Double = 0.0,
    val monto: Double = 0.0
)
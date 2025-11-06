package edu.ucne.marianelaventura_ap2_p2.presentation

import edu.ucne.marianelaventura_ap2_p2.domain.model.Gasto

data class GastoUiState(
    val gastoId: Int? = null,
    val fecha: String = "",
    val suplidor: String = "",
    val ncf: String = "",
    val itbis: String = "",
    val monto: String = "",
    val gastos: List<Gasto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val fechaError: String? = null,
    val suplidorError: String? = null,
    val ncfError: String? = null,
    val itbisError: String? = null,
    val montoError: String? = null
)


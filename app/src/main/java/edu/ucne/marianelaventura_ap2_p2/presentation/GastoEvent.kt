package edu.ucne.marianelaventura_ap2_p2.presentation

import edu.ucne.marianelaventura_ap2_p2.domain.model.Gasto

sealed class GastoEvent {
    data class FechaChanged(val fecha: String) : GastoEvent()
    data class SuplidorChanged(val suplidor: String) : GastoEvent()
    data class NcfChanged(val ncf: String) : GastoEvent()
    data class ItbisChanged(val itbis: String) : GastoEvent()
    data class MontoChanged(val monto: String) : GastoEvent()
    data object SaveGasto : GastoEvent()
    data object ClearForm : GastoEvent()
    data class SelectGasto(val gasto: Gasto) : GastoEvent()
}

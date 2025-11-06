package edu.ucne.marianelaventura_ap2_p2.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.marianelaventura_ap2_p2.data.remote.Resource
import edu.ucne.marianelaventura_ap2_p2.domain.model.Gasto
import edu.ucne.marianelaventura_ap2_p2.domain.usecase.GetGastosUseCase
import edu.ucne.marianelaventura_ap2_p2.domain.usecase.SaveGastoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GastoViewModel @Inject constructor(
    private val getGastosUseCase: GetGastosUseCase,
    private val saveGastoUseCase: SaveGastoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GastoUiState())
    val uiState: StateFlow<GastoUiState> = _uiState.asStateFlow()

    init {
        loadGastos()
    }

    fun onEvent(event: GastoEvent) {
        when (event) {
            is GastoEvent.FechaChanged -> {
                _uiState.update { it.copy(fecha = event.fecha, fechaError = null) }
            }
            is GastoEvent.SuplidorChanged -> {
                _uiState.update { it.copy(suplidor = event.suplidor, suplidorError = null) }
            }
            is GastoEvent.NcfChanged -> {
                _uiState.update { it.copy(ncf = event.ncf, ncfError = null) }
            }
            is GastoEvent.ItbisChanged -> {
                _uiState.update { it.copy(itbis = event.itbis, itbisError = null) }
            }
            is GastoEvent.MontoChanged -> {
                _uiState.update { it.copy(monto = event.monto, montoError = null) }
            }
            GastoEvent.SaveGasto -> saveGasto()
            GastoEvent.ClearForm -> clearForm()
            is GastoEvent.SelectGasto -> selectGasto(event.gasto)
        }
    }

    private fun loadGastos() {
        viewModelScope.launch {
            getGastosUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                gastos = resource.data ?: emptyList(),
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message
                            )
                        }
                    }
                }
            }
        }
    }

    private fun saveGasto() {
        viewModelScope.launch {
            val state = _uiState.value

            var hasError = false

            if (state.fecha.isBlank()) {
                _uiState.update { it.copy(fechaError = "La fecha es obligatoria") }
                hasError = true
            }

            if (state.suplidor.isBlank()) {
                _uiState.update { it.copy(suplidorError = "El suplidor es obligatorio") }
                hasError = true
            }

            if (state.ncf.isBlank()) {
                _uiState.update { it.copy(ncfError = "El NCF es obligatorio") }
                hasError = true
            }

            val itbis = state.itbis.toDoubleOrNull()
            if (itbis == null || itbis < 0) {
                _uiState.update { it.copy(itbisError = "ITBIS inválido") }
                hasError = true
            }

            val monto = state.monto.toDoubleOrNull()
            if (monto == null || monto <= 0) {
                _uiState.update { it.copy(montoError = "Monto inválido") }
                hasError = true
            }

            if (hasError) return@launch

            _uiState.update { it.copy(isLoading = true) }

            val gasto = Gasto(
                gastoId = state.gastoId,
                fecha = state.fecha,
                suplidor = state.suplidor,
                ncf = state.ncf,
                itbis = itbis!!,
                monto = monto!!
            )

            when (val result = saveGastoUseCase(gasto)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = if (state.gastoId == null)
                                "Gasto creado exitosamente"
                            else
                                "Gasto actualizado exitosamente"
                        )
                    }
                    clearForm()
                    loadGastos()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                else -> {}
            }
        }
    }

    private fun selectGasto(gasto: Gasto) {
        _uiState.update {
            it.copy(
                gastoId = gasto.gastoId,
                fecha = gasto.fecha,
                suplidor = gasto.suplidor,
                ncf = gasto.ncf,
                itbis = gasto.itbis.toString(),
                monto = gasto.monto.toString()
            )
        }
    }

    private fun clearForm() {
        _uiState.update {
            GastoUiState(gastos = it.gastos)
        }
    }
}
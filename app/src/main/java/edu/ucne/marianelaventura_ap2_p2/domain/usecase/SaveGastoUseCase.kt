package edu.ucne.marianelaventura_ap2_p2.domain.usecase

import edu.ucne.marianelaventura_ap2_p2.data.remote.Resource
import edu.ucne.marianelaventura_ap2_p2.domain.model.Gasto
import edu.ucne.marianelaventura_ap2_p2.domain.repository.GastoRepository
import javax.inject.Inject

class SaveGastoUseCase @Inject constructor(
    private val repository: GastoRepository
) {
    suspend operator fun invoke(gasto: Gasto): Resource<Gasto> {
        return if (gasto.gastoId == null) {
            repository.createGasto(gasto)
        } else {
            repository.updateGasto(gasto.gastoId, gasto)
        }
    }
}
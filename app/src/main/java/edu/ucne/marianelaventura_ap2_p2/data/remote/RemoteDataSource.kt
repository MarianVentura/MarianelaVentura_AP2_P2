package edu.ucne.marianelaventura_ap2_p2.data.remote

import edu.ucne.marianelaventura_ap2_p2.data.remote.dto.GastoDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val gastoApi: GastoApi
) {
    suspend fun getGastos(): List<GastoDto> = gastoApi.getGastos()

    suspend fun getGasto(id: Int): GastoDto = gastoApi.getGasto(id)

    suspend fun createGasto(gastoDto: GastoDto): GastoDto = gastoApi.createGasto(gastoDto)

    suspend fun updateGasto(id: Int, gastoDto: GastoDto): GastoDto =
        gastoApi.updateGasto(id, gastoDto)
}







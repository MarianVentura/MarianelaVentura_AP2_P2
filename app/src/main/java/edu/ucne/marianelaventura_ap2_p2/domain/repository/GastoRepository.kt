package edu.ucne.marianelaventura_ap2_p2.domain.repository

import edu.ucne.marianelaventura_ap2_p2.data.remote.Resource
import edu.ucne.marianelaventura_ap2_p2.domain.model.Gasto
import kotlinx.coroutines.flow.Flow

interface GastoRepository {
    fun getGastos(): Flow<Resource<List<Gasto>>>
    fun getGasto(id: Int): Flow<Resource<Gasto>>
    suspend fun createGasto(gasto: Gasto): Resource<Gasto>
    suspend fun updateGasto(id: Int, gasto: Gasto): Resource<Gasto>
}


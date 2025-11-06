package edu.ucne.marianelaventura_ap2_p2.data.repository

import edu.ucne.marianelaventura_ap2_p2.data.local.dao.GastoDao
import edu.ucne.marianelaventura_ap2_p2.data.mappers.toDomain
import edu.ucne.marianelaventura_ap2_p2.data.mappers.toDto
import edu.ucne.marianelaventura_ap2_p2.data.mappers.toEntity
import edu.ucne.marianelaventura_ap2_p2.data.remote.RemoteDataSource
import edu.ucne.marianelaventura_ap2_p2.data.remote.Resource
import edu.ucne.marianelaventura_ap2_p2.domain.model.Gasto
import edu.ucne.marianelaventura_ap2_p2.domain.repository.GastoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GastoRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val gastoDao: GastoDao
) : GastoRepository {

    override fun getGastos(): Flow<Resource<List<Gasto>>> = flow {
        try {
            emit(Resource.Loading())

            val gastos = remoteDataSource.getGastos().map { it.toDomain() }

            gastos.forEach { gasto ->
                gastoDao.save(gasto.toEntity())
            }

            emit(Resource.Success(gastos))
        } catch (e: HttpException) {
            emitLocalData()
            emit(Resource.Error("Error HTTP: ${e.message()}"))
        } catch (e: IOException) {
            emitLocalData()
            emit(Resource.Error("Error de conexión. Mostrando datos guardados."))
        } catch (e: Exception) {
            emitLocalData()
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }

    private suspend fun emitLocalData() {
        gastoDao.getAll().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getGasto(id: Int): Flow<Resource<Gasto>> = flow {
        try {
            emit(Resource.Loading())
            val gasto = remoteDataSource.getGasto(id).toDomain()

            gastoDao.save(gasto.toEntity())

            emit(Resource.Success(gasto))
        } catch (e: HttpException) {
            val localGasto = gastoDao.find(id)
            if (localGasto != null) {
                emit(Resource.Success(localGasto.toDomain()))
            }
            emit(Resource.Error("Error HTTP: ${e.message()}"))
        } catch (e: IOException) {
            val localGasto = gastoDao.find(id)
            if (localGasto != null) {
                emit(Resource.Success(localGasto.toDomain()))
            }
            emit(Resource.Error("Error de conexión. Mostrando datos guardados."))
        } catch (e: Exception) {
            emit(Resource.Error("Error desconocido: ${e.message}"))
        }
    }

    override suspend fun createGasto(gasto: Gasto): Resource<Gasto> {
        return try {
            val gastoDto = remoteDataSource.createGasto(gasto.toDto())
            val nuevoGasto = gastoDto.toDomain()

            gastoDao.save(nuevoGasto.toEntity())

            Resource.Success(nuevoGasto)
        } catch (e: HttpException) {
            Resource.Error("Error HTTP: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Error de conexión. Verifica tu internet.")
        } catch (e: Exception) {
            Resource.Error("Error desconocido: ${e.message}")
        }
    }

    override suspend fun updateGasto(id: Int, gasto: Gasto): Resource<Gasto> {
        return try {
            val gastoDto = remoteDataSource.updateGasto(id, gasto.toDto())
            val gastoActualizado = gastoDto.toDomain()

            gastoDao.save(gastoActualizado.toEntity())

            Resource.Success(gastoActualizado)
        } catch (e: HttpException) {
            Resource.Error("Error HTTP: ${e.message()}")
        } catch (e: IOException) {
            Resource.Error("Error de conexión. Verifica tu internet.")
        } catch (e: Exception) {
            Resource.Error("Error desconocido: ${e.message}")
        }
    }
}
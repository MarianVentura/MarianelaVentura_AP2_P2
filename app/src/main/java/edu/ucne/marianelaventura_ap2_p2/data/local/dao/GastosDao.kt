package edu.ucne.marianelaventura_ap2_p2.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.marianelaventura_ap2_p2.data.local.entities.GastosEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {
    @Upsert
    suspend fun save(gasto: GastosEntity)

    @Query("SELECT * FROM Gastos WHERE gastoId = :id LIMIT 1")
    suspend fun find(id: Int): GastosEntity?

    @Query("SELECT * FROM Gastos")
    fun getAll(): Flow<List<GastosEntity>>

    @Delete
    suspend fun delete(gasto: GastosEntity)

    @Query("DELETE FROM Gastos WHERE gastoId = :id")
    suspend fun deleteById(id: Int)
}
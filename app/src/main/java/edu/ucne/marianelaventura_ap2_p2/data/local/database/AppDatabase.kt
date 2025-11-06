package edu.ucne.marianelaventura_ap2_p2.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.marianelaventura_ap2_p2.data.local.dao.GastoDao
import edu.ucne.marianelaventura_ap2_p2.data.local.entities.GastosEntity

@Database(
    entities = [GastosEntity::class],
    version = 1,
    exportSchema = false
)
abstract class GastoDatabase : RoomDatabase() {
    abstract fun gastoDao(): GastoDao
}
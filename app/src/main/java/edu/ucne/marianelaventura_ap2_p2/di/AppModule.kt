package edu.ucne.marianelaventura_ap2_p2.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.marianelaventura_ap2_p2.data.local.database.GastoDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGastoDatabase(@ApplicationContext appContext: Context): GastoDatabase {
        return Room.databaseBuilder(
            appContext,
            GastoDatabase::class.java,
            "SegundoParcial.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideGastoDao(database: GastoDatabase) = database.gastoDao()
}

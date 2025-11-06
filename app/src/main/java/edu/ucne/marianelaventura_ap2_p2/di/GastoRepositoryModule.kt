package edu.ucne.marianelaventura_ap2_p2.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.marianelaventura_ap2_p2.data.repository.GastoRepositoryImpl
import edu.ucne.marianelaventura_ap2_p2.domain.repository.GastoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GastoRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindGastoRepository(
        gastoRepositoryImpl: GastoRepositoryImpl
    ): GastoRepository
}


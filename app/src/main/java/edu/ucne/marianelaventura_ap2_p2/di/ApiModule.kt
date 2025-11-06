package edu.ucne.marianelaventura_ap2_p2.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }
    @Provides
    @Singleton
    fun provideGastoApi(moshi: Moshi, okHttpClient: OkHttpClient): edu.ucne.marianelaventura_ap2_p2.data.remote.GastoApi {
        return retrofit2.Retrofit.Builder()
            .baseUrl("https://gestionhuacalesapi.azurewebsites.net/")
            .addConverterFactory(retrofit2.converter.moshi.MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(edu.ucne.marianelaventura_ap2_p2.data.remote.GastoApi::class.java)
    }
}




package edu.ucne.marianelaventura_ap2_p2.data.remote

import edu.ucne.marianelaventura_ap2_p2.data.remote.dto.GastoDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GastoApi {
    @GET("api/Gastos")
    suspend fun getGastos(): List<GastoDto>

    @GET("api/Gastos/{id}")
    suspend fun getGasto(@Path("id") id: Int): GastoDto

    @POST("api/Gastos")
    suspend fun createGasto(@Body gastoDto: GastoDto): GastoDto

    @PUT("api/Gastos/{id}")
    suspend fun updateGasto(@Path("id") id: Int, @Body gastoDto: GastoDto): GastoDto
}





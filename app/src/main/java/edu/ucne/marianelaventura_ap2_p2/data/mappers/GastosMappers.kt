package edu.ucne.marianelaventura_ap2_p2.data.mappers

import edu.ucne.marianelaventura_ap2_p2.data.local.entities.GastosEntity
import edu.ucne.marianelaventura_ap2_p2.data.remote.dto.GastoDto
import edu.ucne.marianelaventura_ap2_p2.domain.model.Gasto

fun GastoDto.toDomain(): Gasto {
    return Gasto(
        gastoId = gastoId,
        fecha = fecha,
        suplidor = suplidor,
        ncf = ncf,
        itbis = itbis,
        monto = monto
    )
}

fun Gasto.toDto(): GastoDto {
    return GastoDto(
        gastoId = gastoId,
        fecha = fecha,
        suplidor = suplidor,
        ncf = ncf,
        itbis = itbis,
        monto = monto
    )
}

fun GastosEntity.toDomain(): Gasto {
    return Gasto(
        gastoId = gastoId,
        fecha = fecha,
        suplidor = suplidor,
        ncf = ncf,
        itbis = itbis,
        monto = monto
    )
}

fun Gasto.toEntity(): GastosEntity {
    return GastosEntity(
        gastoId = gastoId,
        fecha = fecha,
        suplidor = suplidor,
        ncf = ncf,
        itbis = itbis,
        monto = monto
    )
}
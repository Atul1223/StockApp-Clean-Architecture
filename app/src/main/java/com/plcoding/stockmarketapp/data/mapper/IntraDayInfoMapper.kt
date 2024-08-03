package com.plcoding.stockmarketapp.data.mapper

import com.plcoding.stockmarketapp.data.remote.dto.IntraDayInfoDto
import com.plcoding.stockmarketapp.domain.model.IntraDayInfoModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun IntraDayInfoDto.toIntraDayInfoModel(): IntraDayInfoModel {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timeStamp, formatter)
    return IntraDayInfoModel(
        date = localDateTime,
        close = close
    )
}
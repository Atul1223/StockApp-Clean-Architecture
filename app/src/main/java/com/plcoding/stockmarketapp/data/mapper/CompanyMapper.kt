package com.plcoding.stockmarketapp.data.mapper

import com.plcoding.stockmarketapp.data.local.CompanyListingEntity
import com.plcoding.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.plcoding.stockmarketapp.domain.model.CompanyInfoModel
import com.plcoding.stockmarketapp.domain.model.CompanyListingModel

fun CompanyListingEntity.toCompanyListingModel(): CompanyListingModel {
    return CompanyListingModel(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListingModel.toCompanyListingEntity(): CompanyListingEntity {
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyInfoDto.toCompanyInfoModel(): CompanyInfoModel {
    return CompanyInfoModel(
        name = name ?: "",
        symbol = symbol ?: "",
        description = description ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}
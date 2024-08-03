package com.plcoding.stockmarketapp.domain.model

data class CompanyInfoModel(
    val name: String,
    val symbol: String,
    val description: String,
    val country: String,
    val industry: String,
)

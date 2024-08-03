package com.plcoding.stockmarketapp.presentation.company_info

import com.plcoding.stockmarketapp.domain.model.CompanyInfoModel
import com.plcoding.stockmarketapp.domain.model.IntraDayInfoModel

data class CompanyInfoState(
    val stockIntraDayInfo: List<IntraDayInfoModel> = emptyList(),
    val companyInfoModel: CompanyInfoModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

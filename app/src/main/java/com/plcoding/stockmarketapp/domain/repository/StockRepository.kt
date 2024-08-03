package com.plcoding.stockmarketapp.domain.repository

import androidx.room.Query
import com.plcoding.stockmarketapp.data.remote.dto.CompanyInfoDto
import com.plcoding.stockmarketapp.domain.model.CompanyInfoModel
import com.plcoding.stockmarketapp.domain.model.CompanyListingModel
import com.plcoding.stockmarketapp.domain.model.IntraDayInfoModel
import com.plcoding.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListingModel>>>

    suspend fun getIntraDayInfo(
        symbol: String
    ): Resource<List<IntraDayInfoModel>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfoModel>
}
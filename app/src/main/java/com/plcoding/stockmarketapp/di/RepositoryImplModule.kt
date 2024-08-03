package com.plcoding.stockmarketapp.di

import com.plcoding.stockmarketapp.data.csv.CSVParser
import com.plcoding.stockmarketapp.data.csv.CompanyListingParser
import com.plcoding.stockmarketapp.data.csv.IntraDayInfoParser
import com.plcoding.stockmarketapp.data.repository.StockRepositoryImpl
import com.plcoding.stockmarketapp.domain.model.CompanyListingModel
import com.plcoding.stockmarketapp.domain.model.IntraDayInfoModel
import com.plcoding.stockmarketapp.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryImplModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(
        companyListingParser: CompanyListingParser
    ): CSVParser<CompanyListingModel>

    @Binds
    @Singleton
    abstract fun bindIntraDayInfoParser(
        intraDayInfoParser: IntraDayInfoParser
    ): CSVParser<IntraDayInfoModel>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository
}
package com.plcoding.stockmarketapp.data.repository

import com.plcoding.stockmarketapp.data.csv.CSVParser
import com.plcoding.stockmarketapp.data.csv.CompanyListingParser
import com.plcoding.stockmarketapp.data.csv.IntraDayInfoParser
import com.plcoding.stockmarketapp.data.local.StockDatabase
import com.plcoding.stockmarketapp.data.mapper.toCompanyInfoModel
import com.plcoding.stockmarketapp.data.mapper.toCompanyListingEntity
import com.plcoding.stockmarketapp.data.mapper.toCompanyListingModel
import com.plcoding.stockmarketapp.data.remote.StockApi
import com.plcoding.stockmarketapp.domain.model.CompanyInfoModel
import com.plcoding.stockmarketapp.domain.model.CompanyListingModel
import com.plcoding.stockmarketapp.domain.model.IntraDayInfoModel
import com.plcoding.stockmarketapp.domain.repository.StockRepository
import com.plcoding.stockmarketapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingParser: CSVParser<CompanyListingModel>,
    private val intraDayInfoParser: CSVParser<IntraDayInfoModel>
): StockRepository {

    private val dao = db.dao

    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListingModel>>> {
        return flow {
            emit(Resource.Loading(true))

            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListingModel() }
            ))

            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteListings = try {
                val response = api.getListings()
                companyListingParser.parse(response.byteStream())
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("No able to load data", data = null))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Not able to load data", data = null))
                null
            }

            if(remoteListings != null) {
                dao.clearCompanyListing()
                dao.insertCompanyListing(remoteListings.map { it.toCompanyListingEntity() })
                emit(Resource.Success(
                    data = dao.searchCompanyListing("")
                    .map {
                        it.toCompanyListingModel()
                    }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntraDayInfo(symbol: String): Resource<List<IntraDayInfoModel>> {
        return try {
            val response = api.getIntraDayInfo(symbol = symbol)
            val result = intraDayInfoParser.parse(response.byteStream())
            Resource.Success(data = result)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Not able to load Intra-day data", data = null)
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Not able to load Intra-day data", data = null)
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfoModel> {
        return try {
            val response = api.getCompanyInfo(symbol = symbol)
            Resource.Success(data = response.toCompanyInfoModel())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(message = "Not able to load company data", data = null)
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(message = "Not able to load Intra-company data", data = null)
        }
    }
}
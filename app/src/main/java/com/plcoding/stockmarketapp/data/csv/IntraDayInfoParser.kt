package com.plcoding.stockmarketapp.data.csv

import android.util.Log
import com.opencsv.CSVReader
import com.plcoding.stockmarketapp.data.mapper.toIntraDayInfoModel
import com.plcoding.stockmarketapp.data.remote.dto.IntraDayInfoDto
import com.plcoding.stockmarketapp.domain.model.CompanyListingModel
import com.plcoding.stockmarketapp.domain.model.IntraDayInfoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IntraDayInfoParser @Inject constructor(): CSVParser<IntraDayInfoModel> {

    override suspend fun parse(stream: InputStream): List<IntraDayInfoModel> {
        val csvReader = CSVReader(InputStreamReader(stream))
        return withContext(Dispatchers.IO) {
            csvReader.readAll()
                .drop(1)
                .mapNotNull { row ->
                    val timestamp = row.getOrNull(0) ?: return@mapNotNull null
                    val close = row.getOrNull(4) ?: return@mapNotNull null
                    val dto = IntraDayInfoDto(
                        timeStamp = timestamp,
                        close = close.toDouble()
                    )
                    dto.toIntraDayInfoModel()
                }
                .filter {
                    val day = LocalDate.now().dayOfWeek.name
                    if(day.lowercase() == "sunday") {
                        it.date.dayOfMonth == LocalDate.now().minusDays(2).dayOfMonth
                    } else if( day.lowercase() == "monday") {
                        it.date.dayOfMonth == LocalDate.now().minusDays(3).dayOfMonth
                    } else {
                        it.date.dayOfMonth == LocalDate.now().minusDays(1).dayOfMonth //yesterday
                    }
                }
                .sortedBy {
                    it.date.hour
                }
                .also {
                    csvReader.close()
                }
        }
    }
}
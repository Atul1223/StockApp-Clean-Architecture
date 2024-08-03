package com.plcoding.stockmarketapp.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.stockmarketapp.domain.repository.StockRepository
import com.plcoding.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val stockRepository: StockRepository
): ViewModel() {

    var state by mutableStateOf(CompanyInfoState())
        private set

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val companyInfoResult = async { stockRepository.getCompanyInfo(symbol) }
            val intraDayInfoResult = async { stockRepository.getIntraDayInfo(symbol) }

            when(val result = companyInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        companyInfoModel = result.data,
                        isLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        companyInfoModel = null,
                        isLoading = false,
                        error = result.message
                    )
                }

                is Resource.Loading -> {
                    state = state.copy(
                        companyInfoModel = null,
                        isLoading = true,
                        error = null
                    )
                }

                else -> Unit
            }

            when(val result = intraDayInfoResult.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        stockIntraDayInfo = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }

                is Resource.Error -> {
                    state = state.copy(
                        isLoading = false,
                        error = result.message
                    )
                }

                is Resource.Loading -> {
                    state = state.copy(
                        isLoading = true,
                        error = null
                    )
                }

                else -> Unit
            }
        }
    }
}
package com.plcoding.stockmarketapp.presentation.company_listing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.stockmarketapp.domain.repository.StockRepository
import com.plcoding.stockmarketapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingViewModel @Inject constructor(
    private val stockRepository: StockRepository
): ViewModel() {

    var state by mutableStateOf(CompanyListingsState())
        private set

    private var searchJob: Job? = null

    init {
        getCompanyListings()
    }

    fun onEvent(event: CompanyListingEvents) {
        listOf(
            when (event) {
                is CompanyListingEvents.Refresh -> {
                    getCompanyListings(fetchFromRemote = true)
                }

                is CompanyListingEvents.OnSearchQueryChange -> {
                    state = state.copy(
                        searchQuery = event.query
                    )
                    searchJob?.cancel()
                    searchJob = viewModelScope.launch {
                        delay(500)
                        getCompanyListings()
                    }
                }
            }
        )
    }

    private fun getCompanyListings(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            stockRepository.getCompanyListings(fetchFromRemote, query)
                .collect() {result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let {listings ->
                                state = state.copy(
                                    companies = listings
                                )
                            }
                        }

                        is Resource.Error -> {
                            Unit
                        }

                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }
}
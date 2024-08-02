package com.plcoding.stockmarketapp.presentation.company_listing

sealed class CompanyListingEvents {
    object Refresh: CompanyListingEvents()
    class OnSearchQueryChange(val query: String) : CompanyListingEvents()
}
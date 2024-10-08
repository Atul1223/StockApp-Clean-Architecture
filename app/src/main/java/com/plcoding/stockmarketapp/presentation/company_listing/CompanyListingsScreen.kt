package com.plcoding.stockmarketapp.presentation.company_listing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.plcoding.stockmarketapp.presentation.destinations.CompanyInfoScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination(start = true)
fun CompanyListingsScreen(
    navigator: DestinationsNavigator,
    companyListingViewModel: CompanyListingViewModel = hiltViewModel()
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = companyListingViewModel.state.isRefreshing)

    val state = companyListingViewModel.state

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = {
                companyListingViewModel.onEvent(
                    CompanyListingEvents.OnSearchQueryChange(it)
                )
            },
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth(),

            placeholder = {
                Text(text = "Search...")
            },
            maxLines = 1,
            singleLine = true
        )

        AnimatedVisibility(visible = state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 50.dp))
        }

        AnimatedVisibility(visible = !state.isLoading, enter = fadeIn(), exit = fadeOut()) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {
                    companyListingViewModel.onEvent(CompanyListingEvents.Refresh)
                })
            {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(state.companies) { index, item ->
                        CompanyItem(
                            companyListingModel = item,
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth()
                                .clickable {
                                    navigator.navigate(
                                        CompanyInfoScreenDestination(item.symbol)
                                    )
                                }
                        )
                        if (index < state.companies.size) {
                            Divider(Modifier.padding(horizontal = 15.dp))
                        }
                    }
                }
            }
        }

    }
}
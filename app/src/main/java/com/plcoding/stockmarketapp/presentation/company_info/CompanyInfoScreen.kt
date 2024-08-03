package com.plcoding.stockmarketapp.presentation.company_info

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
@Destination
fun CompanyInfoScreen(
    symbol: String,
    companyInfoViewModel: CompanyInfoViewModel = hiltViewModel()
) {
    val state = companyInfoViewModel.state
    if(state.error == null) {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxSize()
        ) {
            state.companyInfoModel?.let { companyInfoModel ->  
                Text(
                    text = companyInfoModel.name, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 18.sp, 
                    overflow = TextOverflow.Ellipsis, 
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = companyInfoModel.symbol,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                Divider(Modifier
                    .fillMaxWidth())
                Spacer(modifier = Modifier.height(10.dp))


                Text(
                    text = "Industry: ${companyInfoModel.industry}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))


                Text(
                    text = "Country: ${companyInfoModel.country}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))


                Text(
                    text = "Country: ${companyInfoModel.description}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                
                if(state.stockIntraDayInfo.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(text = "Market Summary")
                    Spacer(modifier = Modifier.height(25.dp))
                    StockChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .align(Alignment.CenterHorizontally),
                        graphicColor = Color.Green,
                        intraDayInfo = state.stockIntraDayInfo
                    )
                }
            }
        }
    }
    Box(
        modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(state.isLoading) {
            CircularProgressIndicator()
        } else if(state.error != null) {
            Text(
                text = state.error,
                color = MaterialTheme.colors.error
            )
        }
    }
}
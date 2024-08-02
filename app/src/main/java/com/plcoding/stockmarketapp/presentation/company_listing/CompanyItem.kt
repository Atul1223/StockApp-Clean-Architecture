package com.plcoding.stockmarketapp.presentation.company_listing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import com.plcoding.stockmarketapp.domain.model.CompanyListingModel

@Composable
fun CompanyItem(
    companyListingModel: CompanyListingModel,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = companyListingModel.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(5.dp))

                Text(
                    text = companyListingModel.exchange,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    color = Color.White,
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = companyListingModel.symbol,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
                color = Color.White,
            )
        }
    }
}
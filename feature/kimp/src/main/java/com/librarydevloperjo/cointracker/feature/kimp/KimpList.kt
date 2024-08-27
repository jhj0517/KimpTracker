package com.librarydevloperjo.cointracker.feature.kimp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.librarydevloperjo.cointracker.data.room.KPremiumEntity
import com.librarydevloperjo.cointracker.util.PreferenceManager
import com.librarydevloperjo.cointracker.viewmodels.KPremiumAdapterViewModel

@Composable
fun KPremiumItem(
    viewModel: KPremiumAdapterViewModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            BasicText(
                text = "Name",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 90.dp)
            ) {
                BasicText(
                    text = viewModel.upbitPriceText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                BasicText(
                    text = viewModel.binancePriceText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            BasicText(
                text = "Kpremium",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(viewModel.kpTextColor)
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 70.dp)
            )

            AsyncImage(
                model = viewModel.starImage,
                contentDescription = null,
                modifier = Modifier
                    .size(15.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )

            BasicText(
                text = viewModel.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(start = 20.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun KPremiumItemPreview() {
    val fakeItem = KPremiumEntity(
        id = 1,
        koreanName = "가상화폐",
        englishName = "Cryptocurrency",
        ticker = "BTC",
        upbitPrice = "₩50,000",
        exchangeRate = "1,200 KRW/USD",
        binancePrice = "$40.00",
        kPremium = "10%",
        isBookmark = false
    )
    val mockContext = LocalContext.current
    val dummyViewModel = KPremiumAdapterViewModel(
        items = fakeItem,
        pref = PreferenceManager(mockContext)
    )

    KPremiumItem(
        viewModel = dummyViewModel,
        onClick = {}
    )
}
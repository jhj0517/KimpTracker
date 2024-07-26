package com.librarydevloperjo.cointracker.data.gson

import com.google.gson.annotations.SerializedName
import com.librarydevloperjo.cointracker.data.room.KPremiumData
import java.math.BigDecimal

data class KimchiPremiumResponse(
    val items: List<KimchiPremiumData>
)

data class KimchiPremiumData(
    @field:SerializedName("base_symbol") val baseSymbol: String,
    @field:SerializedName("binance_data") val binanceData: ExchangeData,
    @field:SerializedName("english_name") val englishName: String,
    @field:SerializedName("exchange_rate") val exchangeRate: ExchangeRateData,
    @field:SerializedName("kimchi_premium") val kimchiPremium: BigDecimal,
    @field:SerializedName("korean_name") val koreanName: String,
    @field:SerializedName("timestamp") val timestamp: Long,
    @field:SerializedName("upbit_data") val upbitData: ExchangeData
) {
    fun getPercentage(): BigDecimal = kimchiPremium.multiply(BigDecimal(100))

    fun toLocal(): KPremiumData = KPremiumData(
        koreanName = koreanName,
        englishName = englishName,
        ticker = baseSymbol,
        upbitPrice = upbitData.price.toDouble(),
        binancePrice = binanceData.price.toDouble(),
        kPremium = getPercentage().toDouble()
    )
}

data class ExchangeData(
    @field:SerializedName("symbol") val symbol: String,
    @field:SerializedName("price") val price: BigDecimal,
    @field:SerializedName("timestamp") val timestamp: Long
)

data class ExchangeRateData(
    @field:SerializedName("usd_krw") val usdKrw: BigDecimal,
    @field:SerializedName("timestamp") val timestamp: Long
)
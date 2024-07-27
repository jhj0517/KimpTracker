package com.librarydevloperjo.cointracker.data.gson

import com.google.gson.annotations.SerializedName
import com.librarydevloperjo.cointracker.data.room.KPremiumEntity
import java.math.BigDecimal

data class KimchiPremiumItem(
    @field:SerializedName("base_symbol") val baseSymbol: String,
    @field:SerializedName("binance_data") val binanceData: BinanceExchangeData,
    @field:SerializedName("english_name") val englishName: String,
    @field:SerializedName("exchange_rate") val exchangeRate: ExchangeRateData,
    @field:SerializedName("kimchi_premium") val kimchiPremium: BigDecimal,
    @field:SerializedName("korean_name") val koreanName: String,
    @field:SerializedName("timestamp") val timestamp: Long,
    @field:SerializedName("upbit_data") val upbitData: UpbitExchangeData
) {
    fun toEntity(): KPremiumEntity = KPremiumEntity(
        koreanName = koreanName,
        englishName = englishName,
        ticker = baseSymbol,
        upbitPrice = upbitData.price.toString(),
        binancePrice = binanceData.price.toString(),
        exchangeRate = exchangeRate.usdKrw.toString(),
        kPremium = kimchiPremium.toString()
    )

}

data class BinanceExchangeData(
    @field:SerializedName("symbol") val symbol: String,
    @field:SerializedName("price") val price: BigDecimal,
    @field:SerializedName("timestamp") val timestamp: Long
)

data class UpbitExchangeData(
    @field:SerializedName("symbol") val symbol: String,
    @field:SerializedName("price") val price: BigDecimal,
    @field:SerializedName("timestamp") val timestamp: Long,
    @field:SerializedName("change") val change: String,
    @field:SerializedName("change_rate") val changeRate: BigDecimal,
    @field:SerializedName("change_price") val changePrice: BigDecimal
)

data class ExchangeRateData(
    @field:SerializedName("usd_krw") val usdKrw: BigDecimal,
    @field:SerializedName("timestamp") val timestamp: Long
)
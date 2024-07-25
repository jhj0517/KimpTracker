package com.librarydevloperjo.cointracker.data.gson

import com.google.gson.annotations.SerializedName

data class UpbitResponse(
    val items: List<UpbitItem>
)
data class UpbitItem(
    @field:SerializedName("market") val market: String,
    @field:SerializedName("acc_trade_price") val accTradePrice: Double,
    @field:SerializedName("acc_trade_price_24h") val accTradePrice24h: Double,
    @field:SerializedName("acc_trade_volume") val accTradeVolume: Double,
    @field:SerializedName("acc_trade_volume_24h") val accTradeVolume24h: Double,
    @field:SerializedName("change") val change: String,
    @field:SerializedName("change_price") val changePrice: Long,
    @field:SerializedName("change_rate") val changeRate: Double,
    @field:SerializedName("english_name") val englishName: String,
    @field:SerializedName("high_price") val highPrice: Long,
    @field:SerializedName("highest_52_week_date") val highest52WeekDate: String,
    @field:SerializedName("highest_52_week_price") val highest52WeekPrice: Long,
    @field:SerializedName("korean_name") val koreanName: String,
    @field:SerializedName("low_price") val lowPrice: Long,
    @field:SerializedName("lowest_52_week_date") val lowest52WeekDate: String,
    @field:SerializedName("lowest_52_week_price") val lowest52WeekPrice: Long,
    @field:SerializedName("opening_price") val openingPrice: Long,
    @field:SerializedName("prev_closing_price") val prevClosingPrice: Long,
    @field:SerializedName("signed_change_price") val signedChangePrice: Long,
    @field:SerializedName("signed_change_rate") val signedChangeRate: Double,
    @field:SerializedName("timestamp") val timestamp: Long,
    @field:SerializedName("trade_date") val tradeDate: String,
    @field:SerializedName("trade_date_kst") val tradeDateKst: String,
    @field:SerializedName("trade_price") val tradePrice: Long,
    @field:SerializedName("trade_time") val tradeTime: String,
    @field:SerializedName("trade_time_kst") val tradeTimeKst: String,
    @field:SerializedName("trade_timestamp") val tradeTimestamp: Long,
    @field:SerializedName("trade_volume") val tradeVolume: Double
)

data class UpbitCoin(
    @field:SerializedName("coin") val ticker: String,
    @field:SerializedName("trade_price") val tradePrice: Double,
    @field:SerializedName("change") val change: String,
    @field:SerializedName("change_rate") val changeRate: Double,
    @field:SerializedName("korean_name") val koreanName: String,
    @field:SerializedName("english_name") val englishName: String,
)
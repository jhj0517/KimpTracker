package com.librarydevloperjo.cointracker.data.gson

import com.google.gson.annotations.SerializedName

data class UpbitResponse(
    @field:SerializedName("result") val result: List<UpbitCoin>,
)

data class UpbitCoin(
    @field:SerializedName("coin") val ticker: String,
    @field:SerializedName("trade_price") val tradePrice: Double,
    @field:SerializedName("change") val change: String,
    @field:SerializedName("change_rate") val changeRate: Double,
    @field:SerializedName("korean_name") val koreanName: String,
    @field:SerializedName("english_name") val englishName: String,
)
package com.librarydevloperjo.cointracker.data.gson

import com.google.gson.annotations.SerializedName

data class BinanceResponse(
    val items: List<BinanceItem>,
)

data class BinanceItem(
    @field:SerializedName("symbol") val symbol: String,
    @field:SerializedName("price") val price: String,
    @field:SerializedName("timestamp") val timestamp: Long
)

data class BinanceCoin(
    @field:SerializedName("coin") val ticker: String,
    @field:SerializedName("price") val price: Float,
    @field:SerializedName("lastPrice") val lastPrice: Double,
    @field:SerializedName("priceChangePercent") val priceChangePercent: Double,
)
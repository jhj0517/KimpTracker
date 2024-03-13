package com.librarydevloperjo.cointracker.data.gson

import com.google.gson.annotations.SerializedName

data class BinanceResponse(
    @field:SerializedName("result") val result: List<BinanceCoin>,
)

data class BinanceCoin(
    @field:SerializedName("coin") val ticker: String,
    @field:SerializedName("price") val price: Float,
    @field:SerializedName("lastPrice") val lastPrice: Double,
    @field:SerializedName("priceChangePercent") val priceChangePercent: Double,
)
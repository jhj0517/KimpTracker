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
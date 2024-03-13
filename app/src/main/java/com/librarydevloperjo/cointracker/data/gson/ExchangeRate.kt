package com.librarydevloperjo.cointracker.data.gson

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @field:SerializedName("result") val result: List<ExchangeRate>,
)

data class ExchangeRate(
    @field:SerializedName("openingPrice") val openingPrice: Double,
    @field:SerializedName("change") val change: String,
)

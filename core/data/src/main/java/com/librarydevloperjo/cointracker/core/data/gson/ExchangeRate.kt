package com.librarydevloperjo.cointracker.data.gson

import java.math.BigDecimal

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
   val items: List<ExchangeRateItem>,
)

data class ExchangeRateItem(
    @field:SerializedName("base") val base: String,
    @field:SerializedName("date") val date: String,
    @field:SerializedName("platform") val platform: String,
    @field:SerializedName("privacy") val privacy: String,
    @field:SerializedName("rates") val rates: Map<String, BigDecimal>,
    @field:SerializedName("success") val success: Boolean,
    @field:SerializedName("terms") val terms: String,
    @field:SerializedName("timestamp") val timestamp: Long
){
    fun getRate(currency: String): BigDecimal? {
        return rates[currency]
    }
}


//data class ExchangeRate(
//    @field:SerializedName("openingPrice") val openingPrice: Double,
//    @field:SerializedName("change") val change: String,
//)

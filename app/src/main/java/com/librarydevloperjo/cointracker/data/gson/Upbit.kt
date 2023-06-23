package com.librarydevloperjo.cointracker.data.gson

import com.google.gson.annotations.SerializedName

data class UpbitResponse(
    @field:SerializedName("result") val result: List<UpbitCoin>,
)

data class UpbitCoin(
    var textview_name:String?,
    @field:SerializedName("coin") val coin: String,
    @field:SerializedName("trade_price") val trade_price: Double,
    @field:SerializedName("change") val change: String,
    @field:SerializedName("change_rate") val change_rate: Double,
    @field:SerializedName("korean_name") val korean_name: String,
    @field:SerializedName("english_name") val english_name: String,
)
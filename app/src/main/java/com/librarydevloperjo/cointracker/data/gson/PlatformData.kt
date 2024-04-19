package com.librarydevloperjo.cointracker.data.gson

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @field:SerializedName("statusCode") val statusCode: Int,
    @field:SerializedName("body") val body: PlatformData,
)

data class PlatformData(
    @field:SerializedName("upbit") val upbit:  ArrayList<UpbitCoin>,
    @field:SerializedName("binance") val binance:  ArrayList<BinanceCoin>,
    @field:SerializedName("exc") val exc:  List<ExchangeRate>,
)

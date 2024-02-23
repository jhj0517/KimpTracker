package com.librarydevloperjo.cointracker.data.gson

import com.google.gson.annotations.SerializedName

data class CoinResponse(
    @field:SerializedName("result") val result: Coins,
)

data class Coins(
    @field:SerializedName("upbit") val upbit:  ArrayList<UpbitCoin>,
    @field:SerializedName("binance") val binance:  ArrayList<BinanceCoin>,
    @field:SerializedName("exc") val exc:  List<ExchangeRate>,
)

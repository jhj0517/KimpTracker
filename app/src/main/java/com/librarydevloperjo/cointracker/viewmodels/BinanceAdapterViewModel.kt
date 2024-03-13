package com.librarydevloperjo.cointracker.viewmodels

import com.librarydevloperjo.cointracker.data.gson.BinanceCoin
import java.text.NumberFormat
import java.util.Locale

class BinanceAdapterViewModel(
    private val items: BinanceCoin
) {
    val name:String
        get() = items.ticker

    val price:String
        get() = getPrice(items.price)

    private fun getPrice(price: Float): String {
        // This is done because `.format()` rounds the value unintentionally.
        nFormat.maximumFractionDigits = if (price < 1e-3) 9 else 3
        return nFormat.format(price)
    }

    companion object{
        val nFormat = NumberFormat.getNumberInstance(Locale.US)
    }
}
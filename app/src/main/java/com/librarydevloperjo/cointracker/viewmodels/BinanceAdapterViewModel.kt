package com.librarydevloperjo.cointracker.viewmodels

import com.librarydevloperjo.cointracker.data.gson.BinanceItem
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

class BinanceAdapterViewModel(
    private val item: BinanceItem
) {
    val name:String
        get() = item.symbol

    val price:String
        get() = formatPrice(item.price)

    private fun formatPrice(price: String): String {
        val bigDecimal = BigDecimal(price)
        val stripped = bigDecimal.stripTrailingZeros()
        val formatted = nFormat.format(stripped)
        return if (stripped.scale() <= 0) "$formatted.00" else formatted
    }

    companion object{
        val nFormat: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
    }
}
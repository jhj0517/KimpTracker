package com.librarydevloperjo.cointracker.viewmodels

import com.librarydevloperjo.cointracker.data.gson.KimchiPremiumItem
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

class BinanceAdapterViewModel(
    private val item: KimchiPremiumItem
) {
    val name:String
        get() = item.binanceData.symbol

    val priceText:String
        get() = formatPrice(item.binanceData.price)

    private fun formatPrice(price: Double): String {
        val bigDecimal = BigDecimal(price)
        val stripped = bigDecimal.stripTrailingZeros()
        val formatted = nFormat.format(stripped)
        return if (stripped.scale() <= 0) "$formatted.00" else formatted
    }

    companion object{
        val nFormat: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
    }
}
package com.librarydevloperjo.cointracker.viewmodels

import android.graphics.Color
import com.librarydevloperjo.cointracker.data.gson.UpbitCoin
import com.librarydevloperjo.cointracker.util.LOCALE_KEY
import com.librarydevloperjo.cointracker.util.LOCALE_KOREAN
import com.librarydevloperjo.cointracker.util.PreferenceManager
import java.text.NumberFormat
import java.util.Locale

class UpbitAdapterViewModel(
    private val items:UpbitCoin,
    private val pref: PreferenceManager
) {

    val name: String
        get() = setLocalizedText()

    val priceText: String
        get() = nFormat.format(items.tradePrice)

    val changeRate:String
        get() = setChangeRateText()

    val changeRateTextColor: Int
        get() = setTextColor()

    private fun setTextColor(): Int {
        return when {
            items.changeRate < 0 -> Color.parseColor("#416DD8")
            items.changeRate > 0 -> Color.parseColor("#E8B53333")
            else -> Color.BLACK // Default color
        }
    }

    private fun setChangeRateText(): String{
        return when {
            items.changeRate < 0 -> "-${nFormat.format(items.changeRate)} %"
            items.changeRate > 0 -> "+${nFormat.format(items.changeRate)} %"
            else -> "${nFormat.format(items.changeRate)} %"
        }
    }

    private fun setLocalizedText(): String {
        return when (pref.getInt(LOCALE_KEY)) {
            LOCALE_KOREAN -> items.koreanName ?: items.ticker
            else -> items.englishName ?: items.ticker
        }
    }

    init {
        nFormat.maximumFractionDigits = 3
    }

    companion object{
        val nFormat = NumberFormat.getNumberInstance(Locale.US)
    }
}
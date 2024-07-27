package com.librarydevloperjo.cointracker.viewmodels

import android.graphics.Color
import com.librarydevloperjo.cointracker.data.gson.KimchiPremiumItem
import com.librarydevloperjo.cointracker.util.LOCALE_KEY
import com.librarydevloperjo.cointracker.util.LOCALE_KOREAN
import com.librarydevloperjo.cointracker.util.PreferenceManager
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

class UpbitAdapterViewModel(
    private val item:KimchiPremiumItem,
    private val pref: PreferenceManager
) {

    val name: String
        get() = setLocalizedText()

    val priceText: String
        get() = nFormat.format(item.upbitData.price)

    val changeRate:String
        get() = setChangeRateText()

    val changeRateTextColor: Int
        get() = setTextColor()

    private fun setTextColor(): Int {
        val changeRate = item.upbitData.changeRate
        return when {
            changeRate < BigDecimal.ZERO -> Color.parseColor("#416DD8")
            changeRate > BigDecimal.ZERO -> Color.parseColor("#E8B53333")
            else -> Color.BLACK // Default color
        }
    }

    private fun setChangeRateText(): String{
        val changeRate = item.upbitData.changeRate
        return when {
            changeRate < BigDecimal.ZERO -> "-${nFormat.format(changeRate)} %"
            changeRate > BigDecimal.ZERO -> "+${nFormat.format(changeRate)} %"
            else -> "${nFormat.format(changeRate)} %"
        }
    }

    private fun setLocalizedText(): String {
        return when (pref.getInt(LOCALE_KEY)) {
            LOCALE_KOREAN -> item.koreanName
            else -> item.englishName
        }
    }

    init {
        nFormat.maximumFractionDigits = 3
    }

    companion object{
        val nFormat: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
    }
}
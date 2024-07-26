package com.librarydevloperjo.cointracker.viewmodels

import android.graphics.Color
import com.librarydevloperjo.cointracker.data.gson.UpbitItem
import com.librarydevloperjo.cointracker.util.LOCALE_KEY
import com.librarydevloperjo.cointracker.util.LOCALE_KOREAN
import com.librarydevloperjo.cointracker.util.PreferenceManager
import java.text.NumberFormat
import java.util.Locale

class UpbitAdapterViewModel(
    private val item:UpbitItem,
    private val pref: PreferenceManager
) {

    val name: String
        get() = setLocalizedText()

    val priceText: String
        get() = nFormat.format(item.tradePrice)

    val changeRate:String
        get() = setChangeRateText()

    val changeRateTextColor: Int
        get() = setTextColor()

    private fun setTextColor(): Int {
        return when {
            item.changeRate < 0 -> Color.parseColor("#416DD8")
            item.changeRate > 0 -> Color.parseColor("#E8B53333")
            else -> Color.BLACK // Default color
        }
    }

    private fun setChangeRateText(): String{
        return when {
            item.changeRate < 0 -> "-${nFormat.format(item.changeRate)} %"
            item.changeRate > 0 -> "+${nFormat.format(item.changeRate)} %"
            else -> "${nFormat.format(item.changeRate)} %"
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
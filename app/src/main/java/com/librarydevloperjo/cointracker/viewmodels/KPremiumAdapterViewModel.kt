package com.librarydevloperjo.cointracker.viewmodels

import android.graphics.Color
import com.librarydevloperjo.cointracker.R
import com.librarydevloperjo.cointracker.data.room.KPremiumEntity
import com.librarydevloperjo.cointracker.util.LOCALE_KEY
import com.librarydevloperjo.cointracker.util.LOCALE_KOREAN
import com.librarydevloperjo.cointracker.util.PreferenceManager
import java.text.NumberFormat
import java.util.Locale

class KPremiumAdapterViewModel(
    private val items: KPremiumEntity,
    private val pref:PreferenceManager
) {
    val name: String
        get() = setLocalizedText()

    val upbitPriceText: String
        get() = nFormat.format(items.upbitPrice)

    val binancePriceText: String
        get() = nFormat.format(items.binancePrice)

    val kPremiumText: String
        get() = nFormat.format(items.kPremium) + " %"

    val kpTextColor: Int
        get() = setTextColor()

    val starImage: Int
        get() = setStarImage()

    private fun setTextColor(): Int {
        return when {
            items.kPremium < 0 -> Color.parseColor("#416DD8") // Blue
            items.kPremium > 0 -> Color.parseColor("#E8B53333") // Red with transparency
            else -> Color.BLACK // Default color
        }
    }

    private fun setStarImage(): Int {
        return when (items.isBookmark) {
            true -> R.drawable.ratingstar_filled
            else -> R.drawable.ratingstar_empty
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
    companion object {
        val nFormat: NumberFormat = NumberFormat.getInstance(Locale.US)
    }
}
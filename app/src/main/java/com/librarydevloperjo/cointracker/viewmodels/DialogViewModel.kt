package com.librarydevloperjo.cointracker.viewmodels

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.librarydevloperjo.cointracker.data.room.KPremiumData
import com.librarydevloperjo.cointracker.util.LOCALE_KEY
import com.librarydevloperjo.cointracker.util.LOCALE_KOREAN
import com.librarydevloperjo.cointracker.util.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DialogViewModel @Inject constructor(
    private val pref:PreferenceManager
): ViewModel()  {
    private val _coin = MutableLiveData<KPremiumData>()
    val coin get() = _coin

    val name: String
        get() = setLocalizedText()

    val upbitPrice: String
        get() = nFormat.format(coin.value?.upbitPrice ?: 0)

    val binancePrice: String
        get() = nFormat.format(coin.value?.binancePrice ?: 0)
    val textColor: Int
        get() = setTextColor()
    val kPremium: String
        get() = "${nFormat.format(coin.value?.kPremium ?: 0)} %"

    fun setCoinData(data: KPremiumData) {
        _coin.value = data
    }

    private fun setLocalizedText(): String {
        return when (pref.getInt(LOCALE_KEY)) {
            LOCALE_KOREAN -> coin.value?.koreanName ?: coin.value!!.ticker
            else -> coin.value?.englishName ?: coin.value!!.ticker
        }
    }

    private fun setTextColor(): Int {
        val kPremium = coin.value?.kPremium ?: 0.0
        return when {
            kPremium < 0 -> Color.parseColor("#416DD8")
            kPremium > 0 -> Color.parseColor("#E8B53333")
            else -> Color.BLACK
        }
    }

    companion object{
        val nFormat = NumberFormat.getNumberInstance(Locale.US)
    }
}
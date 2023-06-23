package com.librarydevloperjo.cointracker.util

import com.librarydevloperjo.cointracker.data.gson.BinanceCoin
import com.librarydevloperjo.cointracker.data.room.KPremiumData
import com.librarydevloperjo.cointracker.data.gson.UpbitCoin
import javax.inject.Inject

class MatchCoins @Inject constructor(
    private val preference: PreferenceManager
) {
    // This class is a util class for calculating KPremium and is used in KimpFragment.

    fun match(upbit:List<UpbitCoin>, binance:List<BinanceCoin>, exc:Double) : ArrayList<KPremiumData>{
        val list = arrayListOf<KPremiumData>()
        upbit.forEach { upbitcoin ->
            val upbitTicker = upbitcoin.coin.replace("KRW-","")
            binance.forEach { binancecoin ->
                val binanceTicker = binancecoin.coin.replace("BUSD","")
                if(binanceTicker.equals(upbitTicker)){
                    val upbitKRW = upbitcoin.trade_price
                    val binanceKRW = binancecoin.price * exc
                    val kPremium =  (upbitKRW/binanceKRW)*100-100
                    var name = ""
                    name = if(preference.getInt(LOCALE_KEY)== LOCALE_KOREAN) upbitcoin.korean_name else upbitcoin.english_name
                    if(name.isNullOrEmpty()) name = upbitcoin.coin.replace("KRW-","")

                    val data = KPremiumData(
                        textview_name=name,
                        coinName = binanceTicker,
                        korean_name = upbitcoin.korean_name,
                        english_name = upbitcoin.english_name,
                        upbitPrice = upbitKRW,
                        binancePrice = binanceKRW,
                        kPremium = kPremium,
                    )
                    list.add(data)
                }
            }
        }
        return list
    }

}
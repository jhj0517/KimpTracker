package com.librarydevloperjo.cointracker.util

import com.librarydevloperjo.cointracker.data.gson.BinanceCoin
import com.librarydevloperjo.cointracker.data.room.KPremiumData
import com.librarydevloperjo.cointracker.data.gson.UpbitCoin

object PremiumCalculator {
    fun calculate(upbits:List<UpbitCoin>, binances:List<BinanceCoin>, exc:Double) : ArrayList<KPremiumData>{
        val list = arrayListOf<KPremiumData>()

        val upbitMap = upbits.associateBy { it.coin.replace("KRW-","") }
        val binanceMap = binances.associateBy { it.coin.replace("BUSD","") }

        val sameKeys = upbitMap.keys.intersect(binanceMap.keys)

        val combinedMap = sameKeys.associateWith { ticker ->
            Pair(upbitMap[ticker], binanceMap[ticker])
        }

        combinedMap.forEach { (key, pair) ->
            val upbit = pair.first!!
            val binance = pair.second!!
            val premium = (upbit.trade_price)/(binance.price*exc)*100-100
            val data = KPremiumData(
                textview_name = upbit.korean_name,
                coinName = key,
                korean_name = upbit.korean_name,
                english_name = upbit.english_name,
                upbitPrice = upbit.trade_price,
                binancePrice = binance.price * exc,
                kPremium = premium
            )
            list.add(data)
        }
        return list
    }

}
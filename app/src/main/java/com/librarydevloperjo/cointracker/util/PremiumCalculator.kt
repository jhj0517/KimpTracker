package com.librarydevloperjo.cointracker.util

import com.librarydevloperjo.cointracker.data.gson.BinanceCoin
import com.librarydevloperjo.cointracker.data.room.KPremiumData
import com.librarydevloperjo.cointracker.data.gson.UpbitCoin

object PremiumCalculator {
    fun calculate(upbits:List<UpbitCoin>, binances:List<BinanceCoin>, exc:Double) : ArrayList<KPremiumData>{
        val list = arrayListOf<KPremiumData>()

        val upbitMap = upbits.associateBy { it.ticker.replace("KRW-","") }
        val binanceMap = binances.associateBy { it.ticker.replace("BUSD","") }

        val sameKeys = upbitMap.keys.intersect(binanceMap.keys)

        val combinedMap = sameKeys.associateWith { ticker ->
            Pair(upbitMap[ticker], binanceMap[ticker])
        }

        combinedMap.forEach { (key, pair) ->
            val upbit = pair.first!!
            val binance = pair.second!!
            val premium = (upbit.tradePrice)/(binance.price*exc)*100-100
            val data = KPremiumData(
                ticker = key,
                koreanName = upbit.koreanName,
                englishName = upbit.englishName,
                upbitPrice = upbit.tradePrice,
                binancePrice = binance.price * exc,
                kPremium = premium
            )
            list.add(data)
        }
        return list
    }

}
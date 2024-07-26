package com.librarydevloperjo.cointracker.util

import com.librarydevloperjo.cointracker.data.gson.BinanceItem
import com.librarydevloperjo.cointracker.data.room.KPremiumEntity
import com.librarydevloperjo.cointracker.data.gson.UpbitCoin

object PremiumCalculator {
    fun calculate(upbits:List<UpbitCoin>, binances:List<BinanceItem>, exc:Double) : ArrayList<KPremiumEntity>{
        val list = arrayListOf<KPremiumEntity>()

        val upbitMap = upbits.associateBy { it.ticker.replace("KRW-","") }
        val binanceMap = binances.associateBy { it.symbol.replace("USDT","") }

        val sameKeys = upbitMap.keys.intersect(binanceMap.keys)

        val combinedMap = sameKeys.associateWith { ticker ->
            Pair(upbitMap[ticker], binanceMap[ticker])
        }

        combinedMap.forEach { (key, pair) ->
            val upbit = pair.first!!
            val binance = pair.second!!
            val premium = (upbit.tradePrice)/(binance.price.toDouble()*exc)*100-100
            val data = KPremiumEntity(
                ticker = key,
                koreanName = upbit.koreanName,
                englishName = upbit.englishName,
                upbitPrice = upbit.tradePrice,
                binancePrice = binance.price.toDouble() * exc,
                kPremium = premium,
                exchangeRate = exc
            )
            list.add(data)
        }
        return list
    }

}
package com.librarydevloperjo.cointracker.data

import android.util.Log
import com.librarydevloperjo.cointracker.api.CoinService
import com.librarydevloperjo.cointracker.data.gson.Coins
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

const val refreshInterval = 1500L
const val refreshInterval10minute = 600000L

class CoinRepository @Inject constructor(
    private val service: CoinService,
    private val externalScope: CoroutineScope,
    ) {

    private val _coinPricesTickFlow = MutableSharedFlow<Coins>(replay = 0)
    val coinPricesTickFlow: SharedFlow<Coins> = _coinPricesTickFlow

    init {
        externalScope.launch {
            while(true) {
                try {
                    _coinPricesTickFlow.emit(service.getAllCoin().result)
                } catch (e: Exception){
                    throw Exception("$e")
                }
                delay(refreshInterval)
            }
        }
    }

}
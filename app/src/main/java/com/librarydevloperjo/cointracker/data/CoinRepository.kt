package com.librarydevloperjo.cointracker.data

import com.librarydevloperjo.cointracker.network.CoinService
import com.librarydevloperjo.cointracker.data.gson.PlatformData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

const val refreshInterval = 1500L
const val refreshInterval10minute = 600000L

@Singleton
class CoinRepository @Inject constructor(
    private val service: CoinService,
    private val externalScope: CoroutineScope,
) {

    private val _platformDataTickFlow = MutableSharedFlow<PlatformData>(replay = 0)
    val platformDataTickFlow: SharedFlow<PlatformData> = _platformDataTickFlow

    init {
        externalScope.launch(Dispatchers.IO) {
            while(true) {
                try {
                    _platformDataTickFlow.emit(service.getPlatformData().body)
                } catch (e: Exception){
                    throw Exception("Network error : $e")
                }
                delay(refreshInterval)
            }
        }
    }

}
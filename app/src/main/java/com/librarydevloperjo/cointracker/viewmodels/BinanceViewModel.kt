package com.librarydevloperjo.cointracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.librarydevloperjo.cointracker.data.CoinRepository
import com.librarydevloperjo.cointracker.data.gson.KimchiPremiumItem
import com.librarydevloperjo.cointracker.data.room.KPremiumEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BinanceViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
): ViewModel() {

    private val _sortState = MutableLiveData(SortState.PRICE_DESCENDING)
    val sortState get() = _sortState

    private val _binanceList = MutableLiveData<List<KimchiPremiumItem>>(emptyList())
    val binanceList get()= _binanceList

    private var unSortedList: List<KimchiPremiumItem> = emptyList()

    init {
        collectBinanceData()
    }

    private fun collectBinanceData() {
        viewModelScope.launch {
            while (isActive) {
                coinRepository.kpDataTickFlow.collect { response ->
                    unSortedList = response
                    sortBinanceList()
                }
            }
        }
    }

    private fun sortBinanceList(){
        val sortedList = when (_sortState.value) {
            SortState.PRICE_DESCENDING -> unSortedList.sortedByDescending { it.binanceData.price }
            SortState.PRICE_ASCENDING -> unSortedList.sortedBy { it.binanceData.price }
            else -> unSortedList
        }
        _binanceList.value = sortedList
    }

    fun toggleSortState() {
        val newState = when (sortState.value) {
            SortState.PRICE_DESCENDING -> SortState.PRICE_ASCENDING
            SortState.PRICE_ASCENDING -> SortState.PRICE_DESCENDING
            else -> SortState.PRICE_DESCENDING
        }
        _sortState.value = newState
        sortBinanceList()
    }

}
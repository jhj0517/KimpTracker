package com.librarydevloperjo.cointracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.librarydevloperjo.cointracker.data.CoinRepository
import com.librarydevloperjo.cointracker.data.gson.KimchiPremiumItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject



enum class UpbitSortCriteria{
    PRICE, CHANGE_RATE
}

@HiltViewModel
class UpbitViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
): ViewModel() {
    private val _sortState = MutableLiveData(SortState.PRICE_DESCENDING)
    val sortState get() = _sortState

    private val _upbitList = MutableLiveData<List<KimchiPremiumItem>>(emptyList())
    val upbitList get()= _upbitList

    private var unSortedList: List<KimchiPremiumItem> = emptyList()

    init {
        collectUpbitData()
    }
    private fun collectUpbitData() {
        viewModelScope.launch {
            while (isActive) {
                coinRepository.kpDataTickFlow.collect { response ->
                    unSortedList = response
                    sortUpbitList()
                }
            }
        }
    }

    private fun sortUpbitList(){
        val sortedList = when (_sortState.value) {
            SortState.PRICE_DESCENDING -> unSortedList.sortedByDescending { it.upbitData.price }
            SortState.PRICE_ASCENDING -> unSortedList.sortedBy { it.upbitData.price }
            SortState.CHANGE_RATE_DESCENDING -> unSortedList.sortedByDescending { it.upbitData.changeRate }
            SortState.CHANGE_RATE_ASCENDING -> unSortedList.sortedBy { it.upbitData.changeRate }
            else -> unSortedList
        }
        _upbitList.value = ArrayList(sortedList)
    }

    fun toggleSortState(criteria: UpbitSortCriteria) {
        val newState = when (criteria) {
            UpbitSortCriteria.PRICE ->
                if (sortState.value == SortState.PRICE_DESCENDING) SortState.PRICE_ASCENDING
                else SortState.PRICE_DESCENDING
            UpbitSortCriteria.CHANGE_RATE ->
                if (sortState.value == SortState.CHANGE_RATE_DESCENDING) SortState.CHANGE_RATE_ASCENDING
                else SortState.CHANGE_RATE_DESCENDING
        }
        _sortState.value = newState
        sortUpbitList()
    }

}
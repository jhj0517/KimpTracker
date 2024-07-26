package com.librarydevloperjo.cointracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.librarydevloperjo.cointracker.data.CoinRepository
import com.librarydevloperjo.cointracker.data.gson.KimchiPremiumItem
import com.librarydevloperjo.cointracker.data.room.KDataDAO
import com.librarydevloperjo.cointracker.data.room.KPremiumEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SortState{
    PRICE_DESCENDING,
    PRICE_ASCENDING,
    KIMP_DESCENDING,
    KIMP_ASCENDING,
    CHANGE_RATE_DESCENDING,
    CHANGE_RATE_ASCENDING
}

enum class KPremiumSortCriteria{
    PRICE,
    KIMP
}

@HiltViewModel
class KPremiumViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    private val kDataDAO: KDataDAO
): ViewModel() {
    private val _sortState = MutableLiveData(SortState.PRICE_DESCENDING)
    val sortState get() = _sortState

    private val _kPremiumList = MutableLiveData(arrayListOf<KPremiumEntity>())
    val kPremiumList get()= _kPremiumList

    private var unSortedList: List<KimchiPremiumItem> = emptyList()

    init {
        collectKPremiumData()
    }
    private fun collectKPremiumData() {
        viewModelScope.launch {
            while (isActive) {
                coinRepository.kpDataTickFlow.collect { response ->
                    unSortedList = response.items
                    sortKPremiumList()
                }
            }
        }
    }

    private fun sortKPremiumList(){
        val sortedList = when (_sortState.value) {
            SortState.PRICE_DESCENDING -> unSortedList.sortedByDescending { it.upbitData.price }
            SortState.PRICE_ASCENDING -> unSortedList.sortedBy { it.upbitData.price }
            SortState.KIMP_DESCENDING -> unSortedList.sortedByDescending { it.kimchiPremium }
            SortState.KIMP_ASCENDING -> unSortedList.sortedBy { it.kimchiPremium }
            else -> unSortedList
        }
        val entities = sortedList.map { item -> item.toEntity() }
        _kPremiumList.value = ArrayList(entities)
    }

    fun toggleSortState(criteria: KPremiumSortCriteria) {
        val newState = when (criteria) {
            KPremiumSortCriteria.PRICE ->
                if (sortState.value == SortState.PRICE_DESCENDING) SortState.PRICE_ASCENDING
                else SortState.PRICE_DESCENDING
            KPremiumSortCriteria.KIMP ->
                if (sortState.value == SortState.KIMP_DESCENDING) SortState.KIMP_ASCENDING
                else SortState.KIMP_DESCENDING
        }
        _sortState.value = newState
        sortKPremiumList()
    }

    fun insertBookMark(data: KPremiumEntity){
        viewModelScope.launch {
            kDataDAO.insertBookMark(data)
            sortKPremiumList()
        }
    }
    fun deleteBookMark(coinName:String){
        viewModelScope.launch {
            kDataDAO.deleteBookMark(coinName)
            sortKPremiumList()
        }
    }

    fun queryBookMarks(coinName:String) = kDataDAO.queryBookMarks(coinName)
    private fun getAllBookMarks() = kDataDAO.getAllBookMarks()

}
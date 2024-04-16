package com.librarydevloperjo.cointracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.librarydevloperjo.cointracker.data.*
import com.librarydevloperjo.cointracker.data.room.KPremiumData
import com.librarydevloperjo.cointracker.data.gson.BinanceCoin
import com.librarydevloperjo.cointracker.data.gson.UpbitCoin
import com.librarydevloperjo.cointracker.data.room.KDataDAO
import com.librarydevloperjo.cointracker.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

enum class SortState{
    PRICE_DESCENDING,
    PRICE_ASCENDING,
    KIMP_DESCENDING,
    KIMP_ASCENDING,
    CHANGE_RATE_DESCENDING,
    CHANGE_RATE_ASCENDING
}

@HiltViewModel
class CoinsViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    private val kDataDAO: KDataDAO
) : ViewModel() {
    private val _kPremiumList = MutableLiveData(arrayListOf<KPremiumData>())
    val kPremiumList get() = _kPremiumList

    private val _upbitList = MutableLiveData(arrayListOf<UpbitCoin>())
    val upbitList get() = _upbitList

    private val _binanceList = MutableLiveData(arrayListOf<BinanceCoin>())
    val binanceList get()= _binanceList

    private val _excRate = MutableLiveData<String>()
    val excRate get() = _excRate

    val sortState = MutableLiveData(SortState.KIMP_DESCENDING)

    init {
        collectCoinPrices()
    }
    private fun collectCoinPrices() {
        viewModelScope.launch {
            while (isActive) {
                coinRepository.coinPricesTickFlow.collect {
                    val exc = it.exc[0].openingPrice
                    val binance = it.binance
                    val upbits = it.upbit
                    val unSorted = PremiumCalculator.calculate(upbits,binance,exc)
                    val sorted = sortKimpByState(sortState.value!!, unSorted)
                    _kPremiumList.value = sorted
                    _binanceList.value = binance
                    _upbitList.value = upbits
                    _excRate.value = NumberFormat.getNumberInstance(Locale.US).format(exc)
                }
            }
        }
    }

    fun sortKimpByState(state:SortState, unSorted:ArrayList<KPremiumData>): ArrayList<KPremiumData>{
        val sorted = when (state){
            SortState.PRICE_DESCENDING -> unSorted.sortedByDescending { it.upbitPrice }
            SortState.PRICE_ASCENDING -> unSorted.sortedBy { it.upbitPrice }
            SortState.KIMP_ASCENDING -> unSorted.sortedBy { it.kPremium }
            else -> unSorted.sortedByDescending { it.kPremium }
        }
        val bookmarks = getAllBookMarks().onEach { it.isBookmark = true }
        val array = ArrayList(sorted)
        array.removeIf { data ->
            bookmarks.any { bookmark -> bookmark.ticker == data.ticker}
        }
        return ArrayList(bookmarks+array)
    }

    fun sortBinanceByState(state:SortState, unSorted: ArrayList<BinanceCoin>):ArrayList<BinanceCoin>{
        val sorted = when (state){
            SortState.PRICE_ASCENDING -> unSorted.sortedBy { it.price }
            else -> unSorted.sortedByDescending { it.price }
        }
        return ArrayList(sorted)
    }

    fun sortUpbitByState(state:SortState, unSorted: ArrayList<UpbitCoin>):ArrayList<UpbitCoin>{
        val sorted = when (state){
            SortState.CHANGE_RATE_DESCENDING -> unSorted.sortedByDescending { it.changeRate }
            SortState.CHANGE_RATE_ASCENDING -> unSorted.sortedBy { it.changeRate }
            SortState.PRICE_ASCENDING -> unSorted.sortedBy { it.tradePrice }
            else -> unSorted.sortedByDescending { it.tradePrice }
        }
        return ArrayList(sorted)
    }

    fun insertBookMark(data: KPremiumData){
        viewModelScope.launch {
            kDataDAO.insertBookMark(data)
            refreshKData()
        }
    }
    fun deleteBookMark(coinName:String){
        viewModelScope.launch {
            kDataDAO.deleteBookMark(coinName)
            refreshKData()
        }
    }

    fun queryBookMarks(coinName:String) = kDataDAO.queryBookMarks(coinName)
    private fun getAllBookMarks() = kDataDAO.getAllBookMarks()

    private fun refreshKData(){
        _kPremiumList.value = _kPremiumList.value
    }
}
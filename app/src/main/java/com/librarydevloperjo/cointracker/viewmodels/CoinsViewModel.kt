package com.librarydevloperjo.cointracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.librarydevloperjo.cointracker.data.*
import com.librarydevloperjo.cointracker.data.room.KPremiumData
import com.librarydevloperjo.cointracker.data.gson.BinanceCoin
import com.librarydevloperjo.cointracker.data.gson.Coins
import com.librarydevloperjo.cointracker.data.gson.UpbitCoin
import com.librarydevloperjo.cointracker.data.room.KDataDAO
import com.librarydevloperjo.cointracker.util.KIMP_ASCENDING
import com.librarydevloperjo.cointracker.util.KIMP_DESCENDING
import com.librarydevloperjo.cointracker.util.PRICE_ASCENDING
import com.librarydevloperjo.cointracker.util.PRICE_DESCENDING
import com.librarydevloperjo.cointracker.util.PremiumCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CoinsViewModel @Inject constructor(
    private val coinRepository: CoinRepository,
    private val kDataDAO: KDataDAO
) : ViewModel() {

    val coinPricesTickFlow: SharedFlow<Coins> get() = coinRepository.coinPricesTickFlow

    val kPremiumSortState = MutableLiveData(-1)
    val upbitSortState = MutableLiveData(-1)
    val binanceSortState = MutableLiveData(-1)

    private val _kPremiumList = MutableLiveData(arrayListOf<KPremiumData>())
    val kPremiumList get() = _kPremiumList

    private val _upbitList = MutableLiveData(mutableListOf<UpbitCoin>())
    val upbitList get() = _upbitList

    private val _binanceList = MutableLiveData(mutableListOf<BinanceCoin>())
    val binanceList get()= _binanceList

    private val _excRate = MutableLiveData<String>()
    val excRate get() = _excRate

    init {
        collectCoinPrices()
    }
    private fun collectCoinPrices() {
        viewModelScope.launch {
            while (isActive) {
                coinRepository.coinPricesTickFlow.collect {
                    val exc = it.exc.get(0).openingPrice
                    val binance = it.binance
                    val upbits = it.upbit
                    val unSorted = PremiumCalculator.calculate(upbits,binance,exc)
                    val sorted = sortKimpByState(kPremiumSortState.value!!, unSorted)
                    _kPremiumList.value = sorted
                    _excRate.value = NumberFormat.getNumberInstance(Locale.US).format(exc)
                }
            }
        }
    }

    fun sortKimpByState(state:Int, unSorted:ArrayList<KPremiumData>): ArrayList<KPremiumData>{
        val sorted = when (state){
            PRICE_DESCENDING -> unSorted.sortedByDescending { it.upbitPrice }
            PRICE_ASCENDING -> unSorted.sortedBy { it.upbitPrice }
            KIMP_DESCENDING -> unSorted.sortedByDescending { it.kPremium }
            KIMP_ASCENDING -> unSorted.sortedBy { it.kPremium }
            else -> unSorted.sortedByDescending { it.upbitPrice }
        }
        val bookmarks = getAllKData().onEach { it.isBookmark = true }
        val array = ArrayList(sorted)
        array.removeIf { data ->
            bookmarks.any { bookmark -> bookmark.ticker == data.ticker}
        }
        return ArrayList(bookmarks+array)
    }

    fun insertKData(data: KPremiumData){
        viewModelScope.launch {
            kDataDAO.insertKData(data)
            refreshKData()
        }
    }
    fun deleteKData(coinName:String){
        viewModelScope.launch {
            kDataDAO.deleteKData(coinName)
            refreshKData()
        }
    }
    fun getAllKData() = kDataDAO.getAllKData()
    fun searchKData(coinName:String) = kDataDAO.searchKData(coinName)

    private fun refreshKData(){
        _kPremiumList.value = _kPremiumList.value
    }
}
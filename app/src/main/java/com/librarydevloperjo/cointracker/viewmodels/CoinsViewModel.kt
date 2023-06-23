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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

    val kPremiumList = MutableLiveData(arrayListOf<KPremiumData>())
    val upbitList = MutableLiveData(mutableListOf<UpbitCoin>())
    val binanceList = MutableLiveData(mutableListOf<BinanceCoin>())
    val excRate = MutableLiveData<String>()

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
        kPremiumList.value = kPremiumList.value
    }
}
package com.librarydevloperjo.cointracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UIViewModel @Inject constructor(

):ViewModel() {
    val isInfoFragment = MutableLiveData(false)
}
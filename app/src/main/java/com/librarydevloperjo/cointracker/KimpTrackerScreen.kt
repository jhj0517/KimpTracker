package com.librarydevloperjo.cointracker

sealed interface KimpTrackerScreen {
    data object Kimp : KimpTrackerScreen

    data class Inf : KimpTrackerScreen
}
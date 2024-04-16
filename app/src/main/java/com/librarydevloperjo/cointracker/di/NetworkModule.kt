package com.librarydevloperjo.cointracker.di

import com.librarydevloperjo.cointracker.network.CoinService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideCoinService(): CoinService {
        return CoinService.create()
    }
}
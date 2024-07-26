package com.librarydevloperjo.cointracker.network

import com.librarydevloperjo.cointracker.BuildConfig
import com.librarydevloperjo.cointracker.data.gson.BaseResponse
import com.librarydevloperjo.cointracker.data.gson.BinanceResponse
import com.librarydevloperjo.cointracker.data.gson.ExchangeRateResponse
import com.librarydevloperjo.cointracker.data.gson.KimchiPremiumResponse
import com.librarydevloperjo.cointracker.data.gson.UpbitResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CoinService {

    @GET("/binance")
    suspend fun fetchBinanceData(): BinanceResponse

    @GET("/upbit")
    suspend fun fetchUpbitData(): UpbitResponse

    @GET("/exchange-rate")
    suspend fun fetchExchangeRateData(): ExchangeRateResponse

    @GET("/kimchi-premium")
    suspend fun fetchKimchiPremiumData(): KimchiPremiumResponse

    companion object {
        private const val BASE_URL = BuildConfig.BaseURL

        fun create(): CoinService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CoinService::class.java)
        }
    }

}
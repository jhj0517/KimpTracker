package com.librarydevloperjo.cointracker.network

import com.librarydevloperjo.cointracker.BuildConfig
import com.librarydevloperjo.cointracker.data.gson.BaseResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CoinService {

    @GET("price")
    suspend fun getPlatformData(): BaseResponse

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
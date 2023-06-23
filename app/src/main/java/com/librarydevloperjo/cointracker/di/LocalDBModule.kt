package com.librarydevloperjo.cointracker.di

import android.content.Context
import com.librarydevloperjo.cointracker.data.room.AppDatabase
import com.librarydevloperjo.cointracker.data.room.KDataDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalDBModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideKDataDao(appDatabase: AppDatabase): KDataDAO {
        return appDatabase.kdataDao()
    }
}

package com.librarydevloperjo.cointracker.data.room

import androidx.room.*

@Dao
interface KDataDAO {
    @Query("SELECT * FROM kdata")
    fun getAllKData():List<KPremiumData>

    @Query("SELECT * FROM kdata WHERE coinName = :coinName")
    fun searchKData(coinName:String):List<KPremiumData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKData(kdata: KPremiumData)

    //@Delete
    @Query("DELETE FROM kdata WHERE coinName = :coinName")
    suspend fun deleteKData(coinName:String)
}
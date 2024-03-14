package com.librarydevloperjo.cointracker.data.room

import androidx.room.*

@Dao
interface KDataDAO {
    @Query("SELECT * FROM kdata")
    fun getAllBookMarks():List<KPremiumData>

    @Query("SELECT * FROM kdata WHERE ticker = :ticker")
    fun queryBookMarks(ticker:String):List<KPremiumData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookMark(kdata: KPremiumData)

    //@Delete
    @Query("DELETE FROM kdata WHERE ticker = :ticker")
    suspend fun deleteBookMark(ticker:String)
}
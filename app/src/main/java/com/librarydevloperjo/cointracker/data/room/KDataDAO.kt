package com.librarydevloperjo.cointracker.data.room

import androidx.room.*

@Dao
interface KDataDAO {
    @Query("SELECT * FROM kdata")
    fun getAllBookMarks():List<KPremiumEntity>

    @Query("SELECT * FROM kdata WHERE ticker = :ticker")
    fun queryBookMarks(ticker:String):List<KPremiumEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookMark(kdata: KPremiumEntity)

    //@Delete
    @Query("DELETE FROM kdata WHERE ticker = :ticker")
    suspend fun deleteBookMark(ticker:String)
}
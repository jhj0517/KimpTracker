package com.librarydevloperjo.cointracker.data.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "kdata"
)
@Parcelize
data class KPremiumEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id:Long=0,
    @ColumnInfo(name = "koreanName") val koreanName:String?,
    @ColumnInfo(name = "englishName") val englishName:String?,
    @ColumnInfo(name = "ticker") val ticker:String,
    @ColumnInfo(name = "upbitPrice") val upbitPrice:Double,
    @ColumnInfo(name = "binancePrice") val binancePrice:Double,
    @ColumnInfo(name = "kPremium") val kPremium:Double,
    var isBookmark:Boolean?=false,
):Parcelable

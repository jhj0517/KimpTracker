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
data class KPremiumData(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val kdataid:Long=0,
    @ColumnInfo(name = "textview_name") var textview_name:String?,
    @ColumnInfo(name = "korean_name") val korean_name:String?,
    @ColumnInfo(name = "english_name") val english_name:String?,
    @ColumnInfo(name = "coinName") val coinName:String,
    @ColumnInfo(name = "upbitPrice") val upbitPrice:Double,
    @ColumnInfo(name = "binancePrice") val binancePrice:Double,
    @ColumnInfo(name = "kPremium") val kPremium:Double,
    var isBookmark:Boolean?=false,
):Parcelable // Parcelable to pass as argument to Dialog. See also : https://stackoverflow.com/questions/57637693/putting-kotlin-parcelable-data-class-in-java-bundle-doesnt-work

package com.librarydevloperjo.cointracker.coinwidget
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.RemoteViews
import com.librarydevloperjo.cointracker.MainActivity
import com.librarydevloperjo.cointracker.R
import com.librarydevloperjo.cointracker.data.CoinRepository
import com.librarydevloperjo.cointracker.data.room.KPremiumData
import com.librarydevloperjo.cointracker.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class WidgetProvider:AppWidgetProvider(){

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }
}
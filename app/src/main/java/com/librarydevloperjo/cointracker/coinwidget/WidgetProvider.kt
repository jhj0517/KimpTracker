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

@AndroidEntryPoint
class WidgetProvider:AppWidgetProvider(){

    @Inject
    lateinit var repository: CoinRepository
    @Inject
    lateinit var preference:PreferenceManager

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate +job)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            val pendingIntent: PendingIntent = Intent(context, MainActivity::class.java)
                .let { intent ->
                    PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                }

            val views = RemoteViews(
                context.packageName,
                R.layout.app_widget
            )

            coroutineScope.launch {
                repository.coinPricesTickFlow.collectLatest {
                    val ticker = preference.getString(WIDGET_COIN_KEY)

                    if( ticker != PreferenceManager.DEFAULT_VALUE_STRING) {
                        val list = PremiumCalculator.calculate(it.upbit,it.binance,it.exc.get(0).openingPrice)
                        val data = list.filter { it.ticker == ticker }.single()

                        val updatedViews = updateWidget(views, data)
                        appWidgetManager.updateAppWidget(appWidgetId, updatedViews)
                    }
                }
            }
        }
    }

    private fun updateWidget(views: RemoteViews, data: KPremiumData): RemoteViews{
        val upbitPrice = nFormat.format(data.upbitPrice)
        val binancePrice = nFormat.format(data.binancePrice)
        val kimp = nFormat.format(data.kPremium) + " %"

        val textColor = if (data.kPremium > 0) "#E8B53333" else if (data.kPremium < 0) "#416DD8" else "#000000"
        views.setTextColor(R.id.tv_kimprate_widget, Color.parseColor(textColor))
        views.setTextColor(R.id.tv_upbitprice_widget, Color.parseColor(textColor))

        views.setTextViewText(R.id.tv_coin_widget, data.ticker)
        views.setTextViewText(R.id.tv_upbitprice_widget, upbitPrice)
        views.setTextViewText(R.id.tv_binanceprice_widget, binancePrice)
        views.setTextViewText(R.id.tv_kimprate_widget, kimp)
        return views
    }

    override fun onDisabled(context: Context?) {
        job.cancel()
        super.onDisabled(context)
    }

    companion object {
        val nFormat = NumberFormat.getNumberInstance(Locale.US)
    }
}
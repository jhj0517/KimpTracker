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
    @Inject
    lateinit var matchcoins:MatchCoins

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

            val views: RemoteViews = RemoteViews(
                context.packageName,
                R.layout.app_widget
            )

            coroutineScope.launch {
                repository.coinPricesTickFlow.collectLatest {
                    val ticker = preference.getString(WIDGET_COIN_KEY)

                    if( ticker != PreferenceManager.DEFAULT_VALUE_STRING) {
                        val list = matchcoins.match(it.upbit,it.binance,it.exc.get(0).deal_bas_r)
                        val data = list.filter { it.coinName == ticker }.single()

                        val upibtpirce = nFormat.format(data.upbitPrice)
                        val binancePrice = nFormat.format(data.binancePrice)
                        val kimp = nFormat.format(data.kPremium) + " %"

                        if(data.kPremium>0){
                            views.setTextColor(R.id.tv_kimprate_widget, Color.parseColor("#E8B53333"))
                            views.setTextColor(R.id.tv_upbitprice_widget, Color.parseColor("#E8B53333"))
                        }else if(data.kPremium<0){
                            views.setTextColor(R.id.tv_kimprate_widget, Color.parseColor("#416DD8"))
                            views.setTextColor(R.id.tv_upbitprice_widget, Color.parseColor("#416DD8"))
                        }

                        views.setTextViewText(R.id.tv_coin_widget,data.textview_name)
                        views.setTextViewText(R.id.tv_upbitprice_widget,upibtpirce)
                        views.setTextViewText(R.id.tv_binanceprice_widget,binancePrice)
                        views.setTextViewText(R.id.tv_kimprate_widget,kimp)
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                }
            }
        }
    }

    companion object {
        val nFormat = NumberFormat.getNumberInstance(Locale.US)
    }
}
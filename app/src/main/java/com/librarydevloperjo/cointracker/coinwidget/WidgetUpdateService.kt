package com.librarydevloperjo.cointracker.coinwidget

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.librarydevloperjo.cointracker.MainActivity
import com.librarydevloperjo.cointracker.R
import com.librarydevloperjo.cointracker.data.CoinRepository
import com.librarydevloperjo.cointracker.data.room.KPremiumEntity
import com.librarydevloperjo.cointracker.util.PreferenceManager
import com.librarydevloperjo.cointracker.util.PremiumCalculator
import com.librarydevloperjo.cointracker.util.WIDGET_COIN_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

object WidgetColors {
    const val POSITIVE = "#E8B53333"
    const val NEGATIVE = "#416DD8"
    const val NEUTRAL = "#000000"
}

@AndroidEntryPoint
class WidgetUpdateService: LifecycleService() {
    private val nFormat = NumberFormat.getNumberInstance(Locale.US)

    @Inject
    lateinit var repository: CoinRepository
    @Inject
    lateinit var preference: PreferenceManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        lifecycleScope.launch {
            repository.kpDataTickFlow.collectLatest {
                val ticker = preference.getString(WIDGET_COIN_KEY)

                if( ticker != PreferenceManager.DEFAULT_VALUE_STRING) {
                    val list = it.map { item -> item.toEntity() }
                    val data = list.single { entity -> entity.ticker == ticker }

                    updateWidget(data)
                }
            }
        }
    }

    override fun onDestroy() {
        stopSelf()
        super.onDestroy()
    }

    private fun startForeground() {
        try {
            val notification = createNotification()
            ServiceCompat.startForeground(
                /* service = */ this,
                /* id = */ 100,
                /* notification = */ notification,
                /* foregroundServiceType = */ FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } catch (e: Exception) {
            Log.d("FruitWidgetService", "${e}")
        }
    }

    private fun updateWidget(data: KPremiumEntity) {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val widget = ComponentName(this, WidgetProvider::class.java)
        val allWidgetIds = appWidgetManager.getAppWidgetIds(widget)

        // There might be many widgets, so update each of them
        for (widgetId in allWidgetIds) {
            val newView = updateView(data)
            appWidgetManager.updateAppWidget(widgetId, newView)
        }
    }

    private fun updateView(data: KPremiumEntity): RemoteViews {
        val views = RemoteViews(
            this.packageName,
            R.layout.app_widget
        )

        val kPremium = data.kPremium.toBigDecimal()
        val textColor = if (kPremium > BigDecimal.ZERO) WidgetColors.POSITIVE
                        else if (kPremium < BigDecimal.ZERO) WidgetColors.NEGATIVE
                        else WidgetColors.NEUTRAL
        views.setTextColor(R.id.tv_kimprate_widget, Color.parseColor(textColor))
        views.setTextColor(R.id.tv_upbitprice_widget, Color.parseColor(textColor))

        views.setTextViewText(R.id.tv_coin_widget, data.ticker)
        views.setTextViewText(R.id.tv_upbitprice_widget, nFormat.format(data.upbitPrice))
        views.setTextViewText(R.id.tv_binanceprice_widget, nFormat.format(data.binancePrice))
        views.setTextViewText(R.id.tv_kimprate_widget, nFormat.format(data.kPremium) + " %")
        return views
    }

    private fun createNotification(): Notification {
        // build Notification by Android API versions
        val channelId = "CHANNEL ID"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                channelId,
                "KimpTracker Widget",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "KimpTracker Widget is updating data"
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("KimpTracker Widget")
            .setContentText("KimpTracker Widget is updating data")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.kimp_logo_b)
            .build()
    }
}
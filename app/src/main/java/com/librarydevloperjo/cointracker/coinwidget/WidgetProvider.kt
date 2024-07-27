package com.librarydevloperjo.cointracker.coinwidget
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context

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
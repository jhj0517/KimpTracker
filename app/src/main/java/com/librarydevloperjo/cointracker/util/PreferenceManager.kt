package com.librarydevloperjo.cointracker.util

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceManager @Inject constructor(
    private val context:Context
){

    companion object {
        const val PREFERENCES_NAME = "shared_preference_v1"
        const val DEFAULT_VALUE_STRING = ""
        const val DEFAULT_VALUE_BOOLEAN = false
        const val DEFAULT_VALUE_INT = -1
        const val DEFAULT_VALUE_LONG = -1L
        const val DEFAULT_VALUE_FLOAT = -1f
    }

    fun getPreferences(): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    /**
     *
     * Save String
     *
     * @param context
     *
     * @param key
     *
     * @param value
     */
    fun setString(key: String?, value: String?) {
        val prefs = getPreferences()
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.commit()
    }

    /**
     *
     * Save boolean
     *
     * @param context
     *
     * @param key
     *
     * @param value
     */
    fun setBoolean(key: String?, value: Boolean) {
        val prefs = getPreferences()
        val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.commit()
    }

    /**
     *
     * Save int
     *
     * @param context
     *
     * @param key
     *
     * @param value
     */
    fun setInt(key: String?, value: Int) {
        val prefs = getPreferences()
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.commit()
    }

    /**
     *
     * Save long
     *
     * @param context
     *
     * @param key
     *
     * @param value
     */
    fun setLong(key: String?, value: Long) {
        val prefs = getPreferences()
        val editor = prefs.edit()
        editor.putLong(key, value)
        editor.commit()
    }

    /**
     *
     * Save float
     *
     * @param context
     *
     * @param key
     *
     * @param value
     */
    fun setFloat(key: String?, value: Float) {
        val prefs = getPreferences()
        val editor = prefs.edit()
        editor.putFloat(key, value)
        editor.commit()
    }

    /**
     *
     * Load String
     *
     * @param context
     *
     * @param key
     *
     * @return
     */
    fun getString(key: String?): String? {
        val prefs = getPreferences()
        //val default = context.resources.configuration.locales.get(0).toString()
        return prefs.getString(key, DEFAULT_VALUE_STRING)
    }


    /**
     *
     * Load boolean
     *
     * @param context
     *
     * @param key
     *
     * @return
     */
    fun getBoolean( key: String?): Boolean {
        val prefs = getPreferences()
        return prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN)
    }

    /**
     *
     * Load int
     *
     * @param context
     *
     * @param key
     *
     * @return
     */
    fun getInt( key: String?): Int {
        val prefs = getPreferences()
        return prefs.getInt(key, DEFAULT_VALUE_INT)
    }

    /**
     *
     * Load long
     *
     * @param context
     *
     * @param key
     *
     * @return
     */
    fun getLong(key: String?): Long {
        val prefs = getPreferences()
        return prefs.getLong(key, DEFAULT_VALUE_LONG)
    }

    /**
     *
     * Load float
     *
     * @param context
     *
     * @param key
     *
     * @return
     */
    fun getFloat( key: String?): Float {
        val prefs = getPreferences()
        return prefs.getFloat(key, DEFAULT_VALUE_FLOAT)
    }

    /**
     *
     * Remove Key
     *
     * @param context
     *
     * @param key
     */
    fun removeKey(key: String?) {
        val prefs = getPreferences()
        val edit = prefs.edit()
        edit.remove(key)
        edit.commit()
    }

    /**
     *
     * Remove All Data
     *
     * @param context
     */
    fun clear() {
        val prefs = getPreferences()
        val edit = prefs.edit()
        edit.clear()
        edit.commit()
    }
}
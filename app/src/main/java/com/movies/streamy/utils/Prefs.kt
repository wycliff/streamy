package com.movies.streamy.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class Prefs @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        Constants.APP_SHARED_PREFRENCES_NAME,
        Context.MODE_PRIVATE
    )


//    var countryIndex: Int
//        get() = sharedPreferences.getInt("countryIndex", 0)
//        set(check) = sharedPreferences.edit().putInt("countryIndex", check).apply()


    fun setPreference(key: String?, newValue: Any?) {
        when (newValue) {
            is Boolean -> sharedPreferences.edit().putBoolean(key, newValue).apply()
            is String -> sharedPreferences.edit().putString(key, newValue as String?).apply()
            is Int -> sharedPreferences.edit().putInt(key, newValue).apply()
            is Long -> sharedPreferences.edit().putLong(key, newValue).apply()
            else -> throw IllegalArgumentException("Unsupported type: newValue")
        }
    }

    fun getPreference( key: String?, defaultValue: Any?): Any? {
        return if (defaultValue is Boolean) sharedPreferences.getBoolean(key, defaultValue)
        else if (defaultValue is String) sharedPreferences.getString(key, defaultValue as String?)
        else if (defaultValue is Int) sharedPreferences.getInt(key, defaultValue)
        else if (defaultValue is Long) sharedPreferences.getLong(key, defaultValue)
        else throw IllegalArgumentException("Unsupported type of arg newValue")
    }
}
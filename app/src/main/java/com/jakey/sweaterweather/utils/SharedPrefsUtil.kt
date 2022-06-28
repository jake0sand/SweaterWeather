package com.jakey.sweaterweather.utils

import android.content.Context
import android.content.SharedPreferences
import com.jakey.sweaterweather.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefsUtil @Inject constructor(@ApplicationContext context : Context){
    private val prefs: SharedPreferences = context.getSharedPreferences(
    "${BuildConfig.APPLICATION_ID}_shared_preferences", Context.MODE_PRIVATE)

    private val PREF_TAG = "saved_location"
    fun getLocation(): String {
        return prefs.getString(PREF_TAG, "")!!
    }
    fun setLocation(query: String) {
        prefs.edit().putString(PREF_TAG, query).apply()
    }
}
package com.jakey.sweaterweather.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext val context: Context
) {

    suspend fun readLocation(): String {
        val preferences = context.dataStore.data.first()
        return preferences[LOCATION].toString()
    }

    suspend fun saveLocation(value: String) {
        context.dataStore.edit { settings ->
            settings[LOCATION] = value
        }
    }

    companion object {

        private val LOCATION = stringPreferencesKey("location")

        private val Context.dataStore by preferencesDataStore(
            name = "datastore_prefs"
        )
    }
}
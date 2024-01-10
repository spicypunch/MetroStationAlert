package com.example.metrostationalert

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreModule(private val context: Context) {

    companion object {
        // DataSotre 생성
        private val Context.dataStore by preferencesDataStore(name = "dataStore")

        private val latitudeKey = doublePreferencesKey("latitude")
        private val longitudeeKey = doublePreferencesKey("longitude")
    }

    val getLatitude: Flow<Double> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            preference[latitudeKey] ?: 0.0
        }

    val getLongitude: Flow<Double> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            preference[longitudeeKey] ?: 0.0
        }

    suspend fun saveLatitude(latitude: Double) {
        context.dataStore.edit { preference ->
            preference[latitudeKey] = latitude
        }
    }

    suspend fun saveLongitude(longitude: Double) {
        context.dataStore.edit { preference ->
            preference[longitudeeKey] = longitude
        }
    }
}
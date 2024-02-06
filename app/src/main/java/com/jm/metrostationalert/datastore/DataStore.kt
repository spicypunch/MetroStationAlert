package com.jm.metrostationalert.datastore

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStore(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore(name = "dataStore")
        private val stationNameKey = stringPreferencesKey("stationName")
        private val latitudeKey = doublePreferencesKey("latitude")
        private val longitudeeKey = doublePreferencesKey("longitude")
        private val distanceKey = floatPreferencesKey("distance")
        private val notiTitleKey = stringPreferencesKey("notiTitle")
        private val notiContentKey = stringPreferencesKey("notiContent")
    }

    val getStationName: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            preference[stationNameKey] ?: ""
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

    val getDistance: Flow<Float> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            preference[distanceKey] ?: 1.0f
        }

    val getNotiTitle: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            preference[notiTitleKey] ?: "도착!"
        }

    val getNotiContent: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            preference[notiContentKey] ?: "내릴 준비!!!!!"
        }

    suspend fun saveStationName(stationName: String) {
        context.dataStore.edit { preference ->
            preference[stationNameKey] = stationName
        }
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

    suspend fun saveDistance(distance: Float) {
        context.dataStore.edit { preference ->
            preference[distanceKey] = distance
        }
    }

    suspend fun saveNotiTitle(notiTitle: String) {
        context.dataStore.edit { preference ->
            preference[notiTitleKey] = notiTitle
        }
    }

    suspend fun saveNotiContent(notiContent: String) {
        context.dataStore.edit { preference ->
            preference[notiContentKey] = notiContent
        }
    }
}
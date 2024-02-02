package com.example.metrostationalert.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
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
        private val locationUpdateStartedKey = booleanPreferencesKey("locationUpdateStarted")
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

    val getLocationUpdateStarted: Flow<Boolean> = context.dataStore.data
        .catch {exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preference ->
            preference[locationUpdateStartedKey] ?: false

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

    suspend fun saveLocationUpdateStarted(locationUpdateStarted: Boolean) {
        context.dataStore.edit { preference ->
            preference[locationUpdateStartedKey] = locationUpdateStarted
        }
    }
}
package com.jm.metrostationalert.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jm.metrostationalert.data.entity.LatLngEntity

@Database(entities = [LatLngEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): Dao
}
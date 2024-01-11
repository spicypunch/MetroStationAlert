package com.example.metrostationalert.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.metrostationalert.data.entitiy.LatLngEntity

@Database(entities = [LatLngEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): Dao
}